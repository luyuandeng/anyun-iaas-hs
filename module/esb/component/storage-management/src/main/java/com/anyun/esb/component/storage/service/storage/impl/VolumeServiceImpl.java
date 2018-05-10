package com.anyun.esb.component.storage.service.storage.impl;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.storage.client.HostSshClient;
import com.anyun.esb.component.storage.common.Env;
import com.anyun.esb.component.storage.common.HostWebClient;
import com.anyun.esb.component.storage.common.JdbcUtils;
import com.anyun.esb.component.storage.dao.VolumeDao;
import com.anyun.esb.component.storage.dao.impl.VolumeDaoImpl;
import com.anyun.esb.component.storage.service.storage.VolumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gp on 17-4-27.
 */
public class VolumeServiceImpl implements VolumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeServiceImpl.class);
    private VolumeDao volumeDao = new VolumeDaoImpl();


    //find all action host
//    String[] hostInfoQueryAllActiveService() throws Exception {
//        ServiceInvoker invoker = new ServiceInvoker<>();
//        invoker.setComponent("anyun-host");
//        invoker.setService("hostInfo_query_all_active");
//        String json = invoker.invoke1(null, null);
//        LOGGER.debug("json:{}", json);
//        Response<String> response = JsonUtil.fromJson(Response.class, json);
//        String[] s = response.getContent().split(",");
//        return s;
//    }

    //获取一个有效的ip
//    public String getValidIP(String[] host) throws Exception {
//        for (String s : host) {
//            if (StringUtils.isNotEmpty(s) && s.split(":").length == 2) {
//                String ip = s.split(":")[1];
//                LOGGER.debug("找到一个有效的ip:[{}]", ip);
//                return ip;
//            }
//        }
//        throw new Exception("没有找到一个有效的 ip");
//    }

    //判断卷是否存在
//    public boolean queryVolumeWhetherExist(String id) throws Exception {
//        String[] host = hostInfoQueryAllActiveService();
//        LOGGER.debug("host:{}", JsonUtil.toJson(host));
//        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
//        configuration.setPlatformAddress(getValidIP(host));
//        HostWebClient hostWebClient = HostWebClient.build(configuration);
//        Map<String, Object> sendParam = new HashMap<>();
//        sendParam.put("id", id);
//        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
//        String json = hostWebClient.get("/volume/exit/" + id, sendParam);
//        LOGGER.debug("json:{}", json);
//        Response<Map<String, Object>> response = JsonUtil.fromJson(Response.class, json);
//        if (response.getContent() == null || response.getContent().get("status") == null ||
//                StringUtils.isEmpty(response.getContent().get("status").toString())) {
//            throw new Exception("response.getContent() is null [" + response.getException().getMessage() + "]");
//        }
//        String status = response.getContent().get("status").toString();
//        LOGGER.debug("Response status:[{}]", status);
//        if (status.equals("true")) {
//            return true;
//        } else
//            return false;
//        return true;
//    }

    //判断存储是否存在
//    public boolean queryStorageWhetherExist(String type) throws Exception {
//        String[] host = hostInfoQueryAllActiveService();
//        LOGGER.debug("host:{}", JsonUtil.toJson(host));
//        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
//        configuration.setPlatformAddress(getValidIP(host));
//        HostWebClient hostWebClient = HostWebClient.build(configuration);
//        Map<String, Object> sendParam = new HashMap<>();
//        sendParam.put("type", type);
//        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
//        String json = hostWebClient.get("/storage/list/" + type, sendParam);
//        LOGGER.debug("json :{}", json);
//        Response<List<Map<String, Object>>> response = JsonUtil.fromJson(Response.class, json);
//        if (response.getContent() != null) {
//            LOGGER.debug(JsonUtil.toJson(response.getContent()));
//            return true;
//        } else
//            return false;
//    }

    //判断某一台宿主机上是否有存储
//    public boolean queryStorageWhetherExist(String ip, String type) throws Exception {
//        LOGGER.debug("宿主机:[{}] 存储类型:[{}]", ip, type);
//        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
//        configuration.setPlatformAddress(ip);
//        HostWebClient hostWebClient = HostWebClient.build(configuration);
//        Map<String, Object> sendParam = new HashMap<>();
//        sendParam.put("type", type);
//        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
//        String json = hostWebClient.get("/storage/list/" + type, sendParam);
//        LOGGER.debug("json :{}", json);
//        Response<List<Map<String, Object>>> response = JsonUtil.fromJson(Response.class, json);
//        if (response.getContent() != null) {
//            LOGGER.debug(JsonUtil.toJson(response.getContent()));
//            return true;
//        } else
//            return false;
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    //创建卷
    @Override
    public VolumeDto volumeCreate(VolumeCreateParam param) throws Exception {
        VolumeDto volumeDto = new VolumeDto();
        volumeDto.setProject(param.getProject());
        volumeDto.setSpace(param.getSpace());
        volumeDto.setName(param.getName());
        volumeDto.setDescr(param.getDescr());
        volumeDto.setId(StringUtils.uuidGen());
        volumeDto.setStorageId(param.getStorageId());
        return volumeDao.insertVolume(volumeDto);
    }

    //修改卷
    @Override
    public VolumeDto volumeUpdate(VolumeUpdateParam param, boolean mark) throws Exception {
        VolumeDto volumeDto = new VolumeDto();
        volumeDto.setId(param.getId());
        volumeDto.setSpace(param.getSpace());
        volumeDto.setName(param.getName());
        volumeDto.setDescr(param.getDescr());
        return volumeDao.updateVolume(volumeDto);
    }

    //根据卷id 删除卷
    @Override
    public void volumeDeleteById(String id) throws Exception {
        volumeDao.deleteContainerVolumeByVolume(id);
        volumeDao.deleteVolumeById(id);
    }


    //根据卷id 查询卷
    @Override
    public VolumeDto volumeQueryById(String id) throws Exception {
        return volumeDao.selectVolumeById(id);
    }

    //根据项目 查询卷
    @Override
    public List<VolumeDto> volumeQueryByProject(String id) throws Exception {
        List<VolumeDto> l = volumeDao.selectVolumeByProject(id);
        for(VolumeDto  v : l){
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String createTime = date.format(v.getCreateDate());
            String lastModifyDate = date.format(v.getLastModifyDate());
            v.setScreateDate(createTime);
            v.setSlastModifyDate(lastModifyDate);
        }
        return l;
    }



    //根据容器 查询卷
    @Override
    public List<VolumeDto> volumeQueryByContainer(String id) throws Exception {
        return volumeDao.selectVolumeByContainer(id);
    }


    //容器 挂载卷
    @Override
    public void containerMountVolume(ContainerMountVolumeParam param) throws Exception {
        ContainerVolumeDto   cVDto = volumeDao.selectContainerMountVolumeInfoByCondition(param.getContainer(),param.getVolume());
        if(cVDto==null){
            ContainerVolumeDto containerVolumeDto = new ContainerVolumeDto();
            containerVolumeDto.setContainer(param.getContainer());
            containerVolumeDto.setVolume(param.getVolume());
            containerVolumeDto.setMountPath(param.getMountPath());
            volumeDao.insertContainerVolume(containerVolumeDto);
        }
    }

    //容器 卸载卷
    @Override
    public void containerUninstallVolume(ContainerUninstallVolumeParam param) throws Exception {
        ContainerVolumeDto containerVolumeDto = new ContainerVolumeDto();
        containerVolumeDto.setContainer(param.getContainer());
        containerVolumeDto.setVolume(param.getVolume());
        volumeDao.deleteContainerVolume(containerVolumeDto);
    }

    //根据项目 删除卷
    @Override
    public void volumeDeleteByProject(String id) throws Exception {
        List<VolumeDto> volumeDtoList = volumeDao.selectVolumeByProject(id);
        if (volumeDtoList.isEmpty()) {
            LOGGER.debug(" this [project:{}]no volume", id);
            return;
        }
        for (VolumeDto volume : volumeDtoList) {
            if (volume == null)
                continue;
            if (StringUtils.isEmpty(volume.getId()))
                continue;
            volumeDeleteById(volume.getId());
        }
    }

    //根据容器 卸载所有卷
    @Override
    public void containerUninstallAllVolumeByContainer(String id) throws Exception {
        List<VolumeDto> volumeDtoList = volumeQueryByContainer(id);
        if (volumeDtoList.isEmpty()) {
            LOGGER.debug("this [container]:{}  no  mount volume", id);
            return;
        }
        LOGGER.debug("volumeDtoList is:{}", JsonUtil.toJson(volumeDtoList));
        for (VolumeDto volume : volumeDtoList) {
                ContainerUninstallVolumeParam param = new ContainerUninstallVolumeParam();
                param.setVolume(volume.getId());
                param.setContainer(id);
                containerUninstallVolume(param);
        }
    }

    //根据项目和容器 查询卷
    @Override
    public List<VolumeDto> queryVolumeByProjectAndContainer(String project, String container) throws Exception {
        List<VolumeDto> list = new ArrayList<>();
        List<VolumeDto> list1 = volumeQueryByProject(project);
        List<VolumeDto> list2 = volumeQueryByContainer(container);
        if (list2 == null || list2.isEmpty()) {
            return list1;
        }
        for (VolumeDto volumeDto1 : list1) {
            if (volumeDto1 == null || StringUtils.isEmpty(volumeDto1.getId()))
                continue;
            for (VolumeDto volumeDto2 : list2) {
                if (volumeDto2 == null || StringUtils.isEmpty(volumeDto2.getId()))
                    continue;
                if (!volumeDto1.getId().equals(volumeDto2.getId()))
                    list.add(volumeDto1);
            }
        }
        return list;
    }

    @Override
    public List<VolumeDto> VolumeQuery(String userUniqueId, String subMethod, String subParameters) throws Exception {
        List<VolumeDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_ID":
                VolumeDto v = volumeQueryById(subParameters);
                if (v != null) {
                    l = new ArrayList<>();
                    l.add(v);
                }
                break;

            case "QUERY_BY_CONTAINER":
                l = volumeQueryByContainer(subParameters);
                break;

            case "QUERY_BY_PROJECT":
                ServiceInvoker<List> invoker = new ServiceInvoker<>();
                LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
                invoker.setComponent("anyun-storage");
                invoker.setService("volume_query_by_project");
                Map<String, String> param = new HashMap<>();
                param.put("userUniqueId", userUniqueId);
                param.put("id", subParameters);
                List respones = invoker.invoke(param, null);
                l = JsonUtil.fromJson(List.class, JsonUtil.toJson(respones));
                break;

            case "QUERY_UNMOUNTED_BY_PROJECT_CONTAINER":
                String[] array = subParameters.replace("|", ",").split(",");
                if (array.length != 2)
                    throw new Exception("subParameters format  error");
                if (StringUtils.isEmpty(array[0]))
                    throw new Exception("project is empty");
                if (StringUtils.isEmpty(array[1]))
                    throw new Exception("container is empty");
                String project = array[0];
                String container = array[1];
                ServiceInvoker<List> invoker1 = new ServiceInvoker<>();
                LOGGER.debug("Service invoke camel context [{}]", invoker1.getCamelContext());
                invoker1.setComponent("anyun-storage");
                invoker1.setService("volume_query_by_project_and_container_userUniqueId");
                Map<String, String> param1 = new HashMap<>();
                param1.put("userUniqueId", userUniqueId);
                param1.put("project", project);
                param1.put("container", container);
                List response1 = invoker1.invoke(param1, null);
                l = JsonUtil.fromJson(List.class, JsonUtil.toJson(response1));
                break;

            case "QUERY_BY_APPID":
                //查询应用
                ServiceInvoker<Map> invoker2 = new ServiceInvoker<>();
                LOGGER.debug("Service invoker2 camel context [{}]", invoker2.getCamelContext());
                invoker2.setComponent("anyun-host");
                invoker2.setService("application_query_by_id");
                Map<String, String> m = new HashMap<>();
                m.put("userUniqueId", userUniqueId);
                m.put("id", subParameters);
                Map map = invoker2.invoke(m, null);
                ApplicationInfoDto appDto = JsonUtil.fromJson(ApplicationInfoDto.class, JsonUtil.toJson(map));
                if (StringUtils.isEmpty(appDto.getId()))
                    throw new Exception("appDto is empty");
                //获取模板容器Id
                String templateContainer = appDto.getTemplateContainer();
                //根据容器查询卷列表
                l = volumeQueryByContainer(templateContainer);
                break;

            default:
                throw new Exception("subMethod" + subMethod + "  does not exist");
        }
        return l;
    }

    /**
     * 根据存储查询卷
     *
     * @param purpose
     * @return
     * @throws Exception
     */
//    @Override
//    public List<VolumeDto> queryVolumeByStorage(String purpose) throws Exception {
//        List<VolumeDto> list = volumeDao.queryAllVolume(purpose);
//        if (list != null && !list.isEmpty()) {
//            Iterator<VolumeDto> i = list.iterator();
//            while (i.hasNext()) {
//                VolumeDto v = i.next();
//            }
//        }
//        return list;
//    }



    /**
     * 查询卷信息列表
     *
     * @param commonQueryParam
     * @return
     * @throws Exception
     */
    @Override
    public PageDto<VolumeDto> queryVolumeList(CommonQueryParam commonQueryParam) throws Exception {
        PageDto<VolumeDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(commonQueryParam.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.volume_info_base.* FROM  anyuncloud.volume_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.volume_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.volume_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (commonQueryParam.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<VolumeDto> list = JdbcUtils.getList(VolumeDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(commonQueryParam.getLimit());
            pageDto.setPageNumber(commonQueryParam.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
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
            List<VolumeDto> list = JdbcUtils.getList(VolumeDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<VolumeDto> l = JdbcUtils.getList(VolumeDto.class, sql);
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
    public List<ContainerVolumeDto> getContainerVolumeDtoList(String container, String volume) throws Exception {
        return volumeDao.selectContainerVolumeDtoList(container, volume);
    }

    @Override
    public List<VolumeDto> volumeQueryByStorageId(String storageId) throws Exception {
        return  volumeDao.selectVolumeByStorageId(storageId);
    }
}