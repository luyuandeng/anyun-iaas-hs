package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.common.HostWebClient;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.SecurityGroupDao;
import com.anyun.esb.component.host.dao.impl.SecurityGroupDaoImpl;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.TagService;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sxt on 16-10-17.
 */
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGroupServiceImpl.class);
    private SecurityGroupDao securityGroupDao = new SecurityGroupDaoImpl();
    private TagService tagService = new TagServiceImpl();

    public void test() throws Exception {
        String ip = "";
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        configuration.setPlatformAddress(ip);
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        Map<String, Object> sendParam = new HashMap<>();
        sendParam.put("bridge", "");
        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
        String json = hostWebClient.put("/ovs/addAllBridge/bridge", sendParam);
        LOGGER.debug("json:{}", json);
        Response<Map<String, Object>> response = JsonUtil.fromJson(Response.class, json);
        if (response.getContent() == null || response.getContent().get("status") == null ||
                StringUtils.isEmpty(response.getContent().get("status").toString())) {
            throw new Exception("response.getContent() is null [" + response.getException().getMessage() + "]");
        }
        String status = response.getContent().get("status").toString();
        LOGGER.debug("Response status:[{}]", status);
        if (status.equals("true")) {
            LOGGER.debug("true");
        } else
            LOGGER.debug("false");
    }


    public boolean SecurityGroupCall(List<String> list, List<SecurityGroupCallDto> l) {
        try {
            for (int j = 0; j < list.size(); j++) {
                String ip = list.get(j);
                HostWebClient.Configuration configuration = new HostWebClient.Configuration();
                configuration.setPlatformAddress(ip);
                HostWebClient hostWebClient = HostWebClient.build(configuration);
                List<OvsBridgeParam> ovsBridgeParams = new ArrayList<OvsBridgeParam>();
                String action = "normal";
                int nw_proto = 6;
                int priority = 10;
                if (l.size() > 90) {
                    throw new Exception("权重资源不足，priority 【10-100】");
                }
                LOGGER.debug("分配前权重priority:" + priority);
                for (SecurityGroupCallDto dto : l) {
                    if (ip.equals(dto.getHostip())) {
                        OvsBridgeParam param = new OvsBridgeParam();
                        param.setPriority(String.valueOf(priority));
                        param.setNw_proto(nw_proto);
                        param.setAction(action);
                        param.setNw_src(dto.getIpOrSegment());
                        param.setNw_dst(dto.getContainerIp());
                        param.setTp_dst(Integer.parseInt(dto.getPort()));
                        ovsBridgeParams.add(param);
                        priority++;
                    }
                }
                LOGGER.debug("分配后priority：" + priority);
                OvsParam p = new OvsParam();
                p.setParams(ovsBridgeParams);
                LOGGER.debug("Param:[{}]", JsonUtil.toJson(p));
                String json = hostWebClient.put("/ovs/addVisitLimit/bridge", p.asJson());
                LOGGER.debug("json:{}", json);
                Response<Map<String, Object>> response = JsonUtil.fromJson(Response.class, json);
                if (response.getContent() == null || response.getContent().get("status") == null ||
                        StringUtils.isEmpty(response.getContent().get("status").toString())) {
                    throw new Exception("response.getContent() is null [" + response.getException().getMessage() + "]");
                }
                String status = response.getContent().get("status").toString();
                LOGGER.debug("Response status:[{}]", status);
            }
            return true;
        } catch (Exception e) {
            LOGGER.debug("Exception:{}", e);
            return false;
        }
    }

    @Override
    public SecurityGroupDto securityGroupCreate(SecurityGroupCreateParam param) throws Exception {
        SecurityGroupDto securityGroupDto = new SecurityGroupDto();
        Hashids hashids = new Hashids(StringUtils.uuidGen());
        String label = "anyun-" + hashids.encode(new Date().getTime());
        LOGGER.debug("netLabel" + label);
        securityGroupDto.setLabel(label);
        securityGroupDto.setName(param.getName());
        securityGroupDto.setDescription(param.getDescription());
        securityGroupDto.setPort(param.getPort());
        securityGroupDto.setIpOrSegment(param.getIpOrSegment());
        securityGroupDto.setRule(param.getRule());
        securityGroupDto.setProject(param.getProject());
        return securityGroupDao.securityGroupInsert(securityGroupDto);
    }


    @Override
    public SecurityGroupDto securityGroupUpdate(SecurityGroupUpdateParam param) throws Exception {
        SecurityGroupDto securityGroupDto = new SecurityGroupDto();
        securityGroupDto.setLabel(param.getLabel());
        securityGroupDto.setName(param.getName());
        securityGroupDto.setDescription(param.getDescription());
        return securityGroupDao.securityGroupUpdate(securityGroupDto);
    }

    @Override
    public SecurityGroupDto securityGroupQueryByLabel(String label) throws Exception {
        return securityGroupDao.securityGroupSelectByLabel(label);
    }

    @Override
    public List<SecurityGroupDto> securityGroupQueryByProject(String project) throws Exception {
        return securityGroupDao.securityGroupSelectByProject(project);
    }


    //需要掉
    @Override
    public void securityGroupDeleteByLabel(String label) throws Exception {
        tagService.tagDeleteByResourceId(label);
        List<SecurityGroupCallDto> l = securityGroupDao.selectHostIp();
        List<String> list = new ArrayList<String>();
        if (l == null)
            return;
        for (int i = l.size() - 1; i >= 0; i--) {
            if (StringUtils.isEmpty(l.get(i).getHostip()))
                l.remove(i);
        }
        if (l == null)
            return;

        for (SecurityGroupCallDto dto : l) {
            list.add(dto.getHostip());
        }
        List<SecurityGroupDto> dto = securityGroupDao.securityGroupQueryAll();
        for (int i = dto.size() - 1; i >= 0; i--) {
            if (dto.get(i).getLabel().equals(label)) {
                dto.remove(i);
            }
        }
        boolean b = SecurityGroupCall(list, l);
        if (b == true) {
            securityGroupDao.securityGroupDeleteByLabel(label);
        }

    }


    @Override
    public void AddIpToSecurityGroup(AddIpToSecurityGroupParam param) throws Exception {
        if (StringUtils.isEmpty(param.getContainerNetIpId()))
            throw new Exception("param:containerNetIpId is empty");
        if (StringUtils.isEmpty(param.getSecurityGroupId()))
            throw new Exception("param:securityGroupId is empty");
        SecurityGroupIpDto securityGroupIpDto = new SecurityGroupIpDto();
        securityGroupIpDto.setId(StringUtils.uuidGen());
        securityGroupIpDto.setContainerNetIpId(param.getContainerNetIpId());
        securityGroupIpDto.setSecurityGroupId(param.getSecurityGroupId());
        List<SecurityGroupCallDto> l = securityGroupDao.selectHostIp();
        List<String> list = new ArrayList<String>();
        if (l == null)
            return;
        for (int i = l.size() - 1; i >= 0; i--) {
            if (StringUtils.isEmpty(l.get(i).getHostip()))
                l.remove(i);
        }
        if (l == null)
            return;

        for (SecurityGroupCallDto dto : l) {
            list.add(dto.getHostip());
        }
        List<SecurityGroupIpDto> dto = securityGroupDao.securityGroupIpQueryAll();
        dto.add(securityGroupIpDto);
        boolean b = SecurityGroupCall(list, l);
        if (b == true) {
            securityGroupDao.securityGroupIpInsert(securityGroupIpDto);
        }
    }

    @Override
    public void removeIpFromSecurityGroup(RemoveIpFromSecurityGroupParam param) throws Exception {

        if (StringUtils.isEmpty(param.getContainerNetIpId()))
            throw new Exception("param:containerNetIpId is empty");
        if (StringUtils.isEmpty(param.getSecurityGroupId()))
            throw new Exception("param:securityGroupId is empty");
        SecurityGroupIpDto securityGroupIpDto = new SecurityGroupIpDto();
        securityGroupIpDto.setContainerNetIpId(param.getContainerNetIpId());
        securityGroupIpDto.setSecurityGroupId(param.getSecurityGroupId());
        List<SecurityGroupCallDto> l = securityGroupDao.selectHostIp();
        List<String> list = new ArrayList<String>();
        if (l == null)
            return;
        for (int i = l.size() - 1; i >= 0; i--) {
            if (StringUtils.isEmpty(l.get(i).getHostip()))
                l.remove(i);
        }
        if (l == null)
            return;

        for (SecurityGroupCallDto dto : l) {
            list.add(dto.getHostip());
        }
        List<SecurityGroupIpDto> dto = securityGroupDao.securityGroupIpQueryAll();
        for (int j = dto.size() - 1; j >= 0; j--) {
            if (securityGroupIpDto.getContainerNetIpId().equals(dto.get(j).getContainerNetIpId())
                    && securityGroupIpDto.getSecurityGroupId().equals(dto.get(j).getSecurityGroupId())) {
                dto.remove(j);
            }
        }
        boolean b = SecurityGroupCall(list, l);
        if (b == true) {
            securityGroupDao.securityGroupIpDelete(securityGroupIpDto);
        }
    }

    @Override
    public List<SecurityGroupDto> securityGroupQuery(String userUniqueId, String subMethod, String subParameters) throws Exception {
        List<SecurityGroupDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_CONTAINERID":
                String containerId=subParameters;
                l=securityGroupDao.selectSecurityGroupByContainerId(containerId);
                break;
            case "QUERY_BY_PROJECTID":
                ServiceInvoker<List> invoker1 = new ServiceInvoker<>();
                LOGGER.debug("Service invoke camel context [{}]", invoker1.getCamelContext());
                invoker1.setComponent("anyun-host");
                invoker1.setService("securityGroup_query_by_project");
                Map<String, String> param1 = new HashMap<>();
                param1.put("userUniqueId", userUniqueId);
                param1.put("project", subParameters);
                List respones = invoker1.invoke(param1, null);
                l = JsonUtil.fromJson(List.class, JsonUtil.toJson(respones));
                break;
            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }

    @Override
    public PageDto<SecurityGroupDto> queryByConditions(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<SecurityGroupDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.security_group_info.* FROM  anyuncloud.security_group_info  left   join   anyuncloud.tag_info   on  anyuncloud.security_group_info.label  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.security_group_info";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (commonQueryParam.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<SecurityGroupDto> list = JdbcUtils.getList(SecurityGroupDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(commonQueryParam.getLimit());
            pageDto.setPageNumber(commonQueryParam.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else {
            //分页查询
            int start = commonQueryParam.getStart();//查询页码
            int limit = commonQueryParam.getLimit();//每页记录
            boolean replyWithCount = commonQueryParam.isReplyWithCount();//是否返回总条数
            String sortBy = commonQueryParam.getSortBy();//排序字段
            String sortDirection = commonQueryParam.getSortDirection();//排序规则

            // 分页参数校验
            if (start <= 0)
                throw new Exception("start is:{" + start + "}");
            if (limit <= 0)
                throw new Exception("limit is:{" + limit + "}");
            int i = (start - 1) * limit;
            String pagingStatement = "limit" + " " + i + "," + limit;  //分页 语句

            //排序 语句
            String sortingStatement = (sortBy != null && !"".equals(sortBy) ? "order by" + " " + sortBy + " " + sortDirection : "");

            sql = selectStatement + " " + whereStatement + " ";
            //查询全部记录
            List<SecurityGroupDto> list = JdbcUtils.getList(SecurityGroupDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            }else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<SecurityGroupDto> l = JdbcUtils.getList(SecurityGroupDto.class, sql);
                pageDto.setData(l);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = list.size();
            }
            if (replyWithCount)
                pageDto.setTotal(total);
            return pageDto;
        }
    }

    @Override
    public SecurityGroupIpDto querySecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(String securityGroupId, String containerNetIpId) throws Exception {
        return securityGroupDao.selectSecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(securityGroupId,containerNetIpId);
    }
}
 