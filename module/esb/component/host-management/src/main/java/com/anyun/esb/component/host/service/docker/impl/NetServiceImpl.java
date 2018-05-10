package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ConnectToNetParam;
import com.anyun.cloud.param.DisconnectFromNetParam;
import com.anyun.cloud.param.NetLabelInfoCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.HostCommon;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.SecurityGroupDao;
import com.anyun.esb.component.host.dao.impl.NetLabelInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.dao.impl.SecurityGroupDaoImpl;
import com.anyun.esb.component.host.service.docker.*;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by hwt on 16-7-22.
 */
public class NetServiceImpl implements NetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetServiceImpl.class);
    private VSwitchService vSwitchService;
    private NetLabelInfoDao netLabelInfoDao;
    private ProjectDao projectDao;
    private ContainerService containerService;
    private SecurityGroupService securityGroupService;
    private SecurityGroupDao securityGroupDao;
    private TagService tagService;

    public NetServiceImpl() {
        vSwitchService = new VSwitchServiceImpl();
        projectDao = new ProjectDaoImpl();
        netLabelInfoDao = new NetLabelInfoDaoImpl();
        containerService = new ContainerServiceImpl();
        securityGroupService = new SecurityGroupServiceImpl();
        securityGroupDao = new SecurityGroupDaoImpl();
        tagService = new TagServiceImpl();
    }


    //根据子网掩码和IP地址计算网络地址
    public List<IpDto> calculateNetworkAddress(String subnet) throws Exception {
        LOGGER.debug("subnet:[{}]", subnet);
        if (StringUtils.isEmpty(subnet))
            throw new Exception("subnet is  empty");
        // 192.168.1.0/24
        String startIp = "192.168.1.200";
        String endIp = "192.168.1.254";
        List<IpDto> l = new ArrayList<>();
        Inet4Address start = (Inet4Address) Inet4Address.getByName(startIp);
        Inet4Address end = (Inet4Address) Inet4Address.getByName(endIp);
        List<byte[]> list = HostCommon.getRangeIps(null, start.getAddress(), end.getAddress());
        for (byte[] bs : list) {
            String ip = InetAddress.getByAddress(bs).getHostAddress();
            IpDto ipDto = new IpDto();
            ipDto.setIp(ip);
            l.add(ipDto);
        }
        return l;
    }


    @Override
    public void containerConnectToNetwork(ConnectToNetParam param) throws Exception {
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null)
            throw new Exception("param is empty");
        if (StringUtils.isEmpty(param.getContainer()))
            throw new Exception("container is empty");
        ContainerDto containerDto = containerService.queryContainerById(param.getContainer());
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
            throw new Exception("The  container:[" + param.getContainer() + "]  does  not  exist");
        if (StringUtils.isEmpty(param.getLabel()))
            throw new Exception("label is empty");
        NetLabelInfoDto netLabelInfoDto = netLabelInfoQueryByLabel(param.getLabel());
        if (netLabelInfoDto == null || StringUtils.isEmpty(netLabelInfoDto.getLabel()))
            throw new Exception("The label:[" + param.getLabel() + "]  does  not  exist");
        if (netLabelInfoDto.getType().equals("openvswitch"))
            vSwitchService.connectToNetwork(param.getContainer(), "openvswitch", param.getLabel());
        else {
            //判断ip
            if (StringUtils.isEmpty(param.getIp()))
                throw new Exception("Ip is empty");
            //判斷IP 格式
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//IP正则表达式
            if (!param.getIp().matches(regex)) {
                throw new Exception("Ip is ERROR IP");
            }
            ContainerIpInfoDto containerIpInfoDto = netLabelInfoDao.selectContainerIpInfoByIp(param.getIp());
            if (containerIpInfoDto != null && containerIpInfoDto.getIp().equals(param.getIp()))
                throw new Exception("The ip:[" + param.getIp() + "] already exists");
            vSwitchService.connectToNetwork(param.getContainer(), "bridge", param.getLabel(), param.getIp());
        }
        //启动容器
        containerService.startContainer(param.getContainer());
        Map<String, Object> m = vSwitchService.queryContainerIpAddress(param.getLabel(), param.getContainer());
        String ip = m.get("ip").toString();
        String mac = m.get("mac").toString();
        //插入到关联表
        netLabelInfoDao.insertContainerNetIpInfo(param.getContainer(), param.getLabel(), ip, mac);
    }

    @Override
    public void containerDisconnectFromNetwork(DisconnectFromNetParam param) throws Exception {
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null)
            throw new Exception("param is empty");
        if (StringUtils.isEmpty(param.getContainer()))
            throw new Exception("container is empty");
        ContainerDto containerDto = containerService.queryContainerById(param.getContainer());
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
            throw new Exception("The  container:[" + param.getContainer() + "]  does  not  exist");
        if (StringUtils.isEmpty(param.getLabel()))
            throw new Exception("label is empty");
        NetLabelInfoDto netLabelInfoDto = netLabelInfoQueryByLabel(param.getLabel());
        if (netLabelInfoDto == null || StringUtils.isEmpty(netLabelInfoDto.getLabel()))
            throw new Exception("The label:[" + param.getLabel() + "]  does  not  exists");

        if (param.getLabel().equals("bridge")) {
            //查询ContainerNetId
            ContainerIpInfoDto containerIpInfoDto =
                    netLabelInfoDao.selectContainerIpInfoByCondition(param.getContainer(), param.getLabel());
            securityGroupDao.deleteSecurityGroupIpByContainerNetId(containerIpInfoDto.getId());
        }
        vSwitchService.disconnectFromNetwork(param.getContainer(), netLabelInfoDto.getType(), param.getLabel());
        netLabelInfoDao.deleteContainerNetIpInfo(param.getContainer(), param.getLabel());
    }


    @Override
    public List<NetLabelInfoDto> netLabelInfoQueryAllBridge() throws Exception {
        List<NetLabelInfoDto> l = netLabelInfoDao.selectAllBridgeNetLabel();
        if (l == null || l.isEmpty()) {
            return l;
        }

        List<NetLabelInfoDto> list = new ArrayList<>();
        for (NetLabelInfoDto n : l) {
            if (vSwitchService.networkLabelExist(n.getLabel(), n.getType()))
                list.add(n);
        }
        return list;
    }

    @Override
    public void netLabelInfoDeleteByLabel(String label) throws Exception {
        NetLabelInfoDto netLabelInfoDto = netLabelInfoQueryByLabel(label);//获取网络标签信息
        if (netLabelInfoDto == null || StringUtils.isEmpty(netLabelInfoDto.getLabel()))
            throw new Exception("The label does not exists");
        String type = netLabelInfoDto.getType();
        LOGGER.debug("LabelType:[{}]", type);
        if (!type.equals("openvswitch"))
            throw new Exception("只能删除 'openvswitch' 类型网络标签");
        List<ContainerDto> list = containerService.queryContainerByNetLabel(label);
        if (list != null && !list.isEmpty()) {
            for (ContainerDto c : list) {
                DisconnectFromNetParam param = new DisconnectFromNetParam();
                param.setContainer(c.getId());
                param.setLabel(label);
                containerDisconnectFromNetwork(param);
            }
        }
        // 刪除資源關聯表
        tagService.tagDeleteByResourceId(label);

        vSwitchService.deleteOVNNetwork(label); //删除标签
        netLabelInfoDao.deleteNetLabelInfoByLabel(label);
    }


    @Override
    public void insertNetLabelInfo(NetLabelInfoCreateParam param) throws Exception {
        NetLabelInfoDto dto = new NetLabelInfoDto();
        Hashids hashids = new Hashids(StringUtils.uuidGen());
        String label = "anyun-" + hashids.encode(new Date().getTime());
        LOGGER.debug("netLabel" + label);
        vSwitchService.createOVNNetwork(label, param.getSubnet(), param.getGateway());
        //插入标签表中
        ProjectDto projectDto = new ProjectDto();
        dto.setName(param.getName());
        dto.setDescr(param.getDescr());
        dto.setGateway(param.getGateway());
        dto.setLabel(label);
        dto.setSubnet(param.getSubnet());
        netLabelInfoDao.insertNetLabelInfo(dto);
        //更新 网络标签 到项目表中
        projectDto.setId(param.getProject());
        projectDto.setPlatFormNetworkId(label);
        projectDao.updateProject(projectDto);
    }


    @Override
    public NetLabelInfoDto netLabelInfoQueryByLabel(String label) throws Exception {
        NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByLabel(label);
        if (netLabelInfoDto != null && StringUtils.isNotEmpty(netLabelInfoDto.getLabel())) {
            if (!vSwitchService.networkLabelExist(label, netLabelInfoDto.getType()))
                return netLabelInfoDto;
        }
        return netLabelInfoDto;
    }

    @Override
    public List<NetLabelInfoDto> netLabelInfoQueryByContainer(String container) throws Exception {
        List<NetLabelInfoDto> list = netLabelInfoDao.selectNetLabelInfoByContainer(container);
        if (list != null || !list.isEmpty()) {
            LOGGER.debug("list size:[{}]", list.size());
            for (int i = list.size() - 1; i >= 0; i--) {
                NetLabelInfoDto labelInfoDto = netLabelInfoQueryByLabel(list.get(i).getLabel());
                if (labelInfoDto == null || StringUtils.isEmpty(labelInfoDto.getLabel()))
                    list.remove(i);
            }
        }
        return list;
    }


    @Override
    public NetLabelInfoDto netLabelInfoQueryProject(String project) throws Exception {
        NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByProject(project);
        if (netLabelInfoDto != null) {
            if (vSwitchService.networkLabelExist(netLabelInfoDto.getLabel(), netLabelInfoDto.getType()))
                return netLabelInfoDto;
        }
        return null;
    }

    //根据项目 删除网络标签
    @Override
    public void netLabelInfoDeleteByProject(String id) throws Exception {
        NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByProject(id);
        if (netLabelInfoDto != null && !StringUtils.isEmpty(netLabelInfoDto.getLabel())) {
            tagService.tagDeleteByResourceId(netLabelInfoDto.getLabel());
            netLabelInfoDeleteByLabel(netLabelInfoDto.getLabel());
        }
    }

    @Override
    public List<ContainerIpInfoDto> queryContainerIpByCondition(String label, String container) {
        List<ContainerIpInfoDto> list = netLabelInfoDao.selectContainerIpByCondition(label, container);
        return list;
    }


    @Override
    public List<ContainerNetIpMacDto> queryContainerNetIpMacByKeyword(String keyword) throws Exception {
        List<ContainerNetIpMacDto> list = netLabelInfoDao.selectContainerNetIpMacByKeyword(keyword);
        if (list == null || list.isEmpty())
            return null;
        List<ContainerNetIpMacDto> l = new ArrayList<>();
        for (ContainerNetIpMacDto dto : list) {
            if (dto == null || StringUtils.isEmpty(dto.getId()))
                continue;
            ContainerDto containerDto = containerService.queryContainerById(dto.getContainer());
            if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
                continue;
            l.add(dto);
        }
        return l;
    }

    @Override
    public List<ContainerIpInfoDto> containerIpInfoQueryBySecurityGroup(String label) throws Exception {
        SecurityGroupDto securityDto = securityGroupService.securityGroupQueryByLabel(label); //获取安全组的dto
        if (securityDto == null)
            throw new Exception("The securityDto is empty");
        String projectId = securityDto.getProject();//项目Id
        ProjectDto projectDto = projectDao.selectProjectById(projectId);//获取项目Dto
        if (projectDto == null)
            throw new Exception("projectDto is empty");
        return netLabelInfoDao.selectContainerIpInfoBySecurityGroup(label);
    }

    @Override
    public List<ContainerIpInfoDto> containerIpInfoQueryNotAddBySecurityGroup(String label) throws Exception {
        SecurityGroupDto securityDto = securityGroupService.securityGroupQueryByLabel(label);
        if (securityDto == null)
            throw new Exception("The securityDto is empty");
        String projectId = securityDto.getProject();//项目Id
        ProjectDto projectDto = projectDao.selectProjectById(projectId);//项目Dto
        if (projectDto == null)
            throw new Exception("projectDto is empty");
        List<ContainerIpInfoDto> list = netLabelInfoDao.selectContainerIpInfoByProjectIdAndLabel(projectId, "anyun-in");//获取 指定项目和網絡容器ip
        if (list == null || list.isEmpty() || list.size() == 0)
            return null;
        Iterator<ContainerIpInfoDto> it = list.iterator();

        String securityGroupId = label;
        while (it.hasNext()) {
            ContainerIpInfoDto cIDto = it.next();
            if (cIDto == null) {
                it.remove();
                continue;
            }
            if (StringUtils.isEmpty(cIDto.getId())) {
                it.remove();
                continue;
            }
            String containerNetIpId = cIDto.getId();
            SecurityGroupIpDto sGIPDto = securityGroupService.querySecurityGroupIpDtoBySecurityGroupIdAndContainerNetIpId(securityGroupId, containerNetIpId);
            if (sGIPDto != null) { // 已经存在就移除
                it.remove();
            }
        }
        return list;
    }

    @Override
    public ContainerIpInfoDto queryContainerIpInfoByCondition(String container, String netLabel) throws Exception {
        return netLabelInfoDao.selectContainerIpInfoByCondition(container, netLabel);
    }

    @Override
    public List<IpDto> queryAvailabelIpByLabel(String label) throws Exception {
        List<IpDto> list = netLabelInfoDao.selectIpByLabel(label);
        LOGGER.debug("Ip:" + JsonUtil.toJson(list));
        NetLabelInfoDto netLabelInfoDto = netLabelInfoQueryByLabel(label);
        String subnet = netLabelInfoDto.getSubnet();
        List<IpDto> l = calculateNetworkAddress(subnet);
        if (list != null) {
            for (int j = l.size() - 1; j >= 0; j--) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getIp().equals(l.get(j).getIp())) {
                        l.remove(j);
                    }
                }
            }
        }
        return l;
    }


    @Override
    public List<NetLabelInfoDto> netLabelQueryByType(String type) throws Exception {
        LOGGER.debug("type:[{}]", type);
        if (StringUtils.isEmpty(type))
            throw new Exception("type is empty");
        if (!type.matches("(openvswitch)|(bridge)"))
            throw new Exception("Type :[" + type + "] unknown");
        List<NetLabelInfoDto> l = netLabelInfoDao.selectNetLabelInfoByType(type);
        Iterator<NetLabelInfoDto> it = l.iterator();
        while (it.hasNext()) {
            NetLabelInfoDto n = it.next();
            if (!vSwitchService.networkLabelExist(n.getLabel(), n.getType()))
                it.remove();
        }
        return l;
    }

    @Override
    public List<NetLabelInfoDto> netLabelQuery(String subMethod, String subParameters) throws Exception {

        List<NetLabelInfoDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_LABEL":
                NetLabelInfoDto n = netLabelInfoQueryByLabel(subParameters);
                if (n != null) {
                    l = new ArrayList<>();
                    l.add(n);
                }
                break;
            case "QUERY_BY_TYPE":
                l = netLabelQueryByType(subParameters);
                break;

            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }

    @Override
    public List<ContainerIpInfoDto> containerIpQuery(String subMethod, String subParameters) throws Exception {
        List<ContainerIpInfoDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_LABEL_CONTAINER":
                String[] array = subParameters.replace("|", ",").split(",");
                if (array.length != 2)
                    throw new Exception("subParameters format  error");
                if (StringUtils.isEmpty(array[0]))
                    throw new Exception("label is empty");
                if (StringUtils.isEmpty(array[1]))
                    throw new Exception("container is empty");
                String label = array[0];
                String container = array[1];
                ContainerIpInfoDto c = queryContainerIpInfoByCondition(container, label);
                if (c != null) {
                    l = new ArrayList<>();
                    l.add(c);
                }
                break;

            case "QUERY_ALREADY_ADDED_BY_LABEL":
                l = containerIpInfoQueryBySecurityGroup(subParameters);
                break;

            case "QUERY_NOT_ADD_BY_LABEL":
                l = containerIpInfoQueryNotAddBySecurityGroup(subParameters);
                break;

            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }


    @Override
    public PageDto<ContainerIpInfoDto> containeriplistquery(CommonQueryParam param) throws Exception {
        PageDto<ContainerIpInfoDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.container_net_ip_info.* FROM  anyuncloud.container_net_ip_info  left   join   anyuncloud.tag_info   on  anyuncloud.container_net_ip_info.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.container_net_ip_info";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ContainerIpInfoDto> list = JdbcUtils.getList(ContainerIpInfoDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(param.getLimit());
            pageDto.setPageNumber(param.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = param.getStart();//查询页码
            int limit = param.getLimit();//每页记录
            boolean replyWithCount = param.isReplyWithCount();//是否返回总条数
            String sortBy = param.getSortBy();//排序字段
            String sortDirection = param.getSortDirection();//排序规则

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
            List<ContainerIpInfoDto> list = JdbcUtils.getList(ContainerIpInfoDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<ContainerIpInfoDto> l = JdbcUtils.getList(ContainerIpInfoDto.class, sql);
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
    public PageDto<NetLabelInfoDto> NetLabelInfoQuery(CommonQueryParam param) throws Exception {
        PageDto<NetLabelInfoDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.net_label_info.* FROM  anyuncloud.net_label_info  left   join   anyuncloud.tag_info   on  anyuncloud.net_label_info  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.net_label_info";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ContainerDto> list = JdbcUtils.getList(NetLabelInfoDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(param.getLimit());
            pageDto.setPageNumber(param.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = param.getStart();//查询页码
            int limit = param.getLimit();//每页记录
            boolean replyWithCount = param.isReplyWithCount();//是否返回总条数
            String sortBy = param.getSortBy();//排序字段
            String sortDirection = param.getSortDirection();//排序规则

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
            List<NetLabelInfoDto> list = JdbcUtils.getList(NetLabelInfoDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<NetLabelInfoDto> l = JdbcUtils.getList(NetLabelInfoDto.class, sql);
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
    public ContainerIpInfoDto queryContainerIpDtoByContainerAndNetLabel(String containerId, String label) {
        return netLabelInfoDao.selectContainerIpDtoByContainerAndNetLabel(containerId, label);
    }
}
