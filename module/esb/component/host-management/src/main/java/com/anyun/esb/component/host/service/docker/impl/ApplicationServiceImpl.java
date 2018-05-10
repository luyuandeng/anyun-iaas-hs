package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.ApplicationCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ContainerOpParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.common.ScpClient;
import com.anyun.esb.component.host.dao.*;
import com.anyun.esb.component.host.dao.impl.*;
import com.anyun.esb.component.host.service.TemplateService;
import com.anyun.esb.component.host.service.docker.*;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.RemoveImageCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sxt on 16-10-25.
 */
public class ApplicationServiceImpl implements ApplicationService {
    final static Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    private ApplicationDao applicationDao = new ApplicationDaoImpl();
    private TagDao tagDao = new TagDaoImpl();

    @Override
    public ApplicationInfoDto applicationCreate(ApplicationCreateParam param) throws Exception {
        HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
        ContainerService containerService = new ContainerServiceImpl();
        ProjectService projectService = new ProjectServiceImpl();
        NetService netService = new NetServiceImpl();
        NetLabelInfoDao netLabelInfoDao = new NetLabelInfoDaoImpl();
        ContainerIpInfoDto containerIpInfoDto = netLabelInfoDao.selectContainerIpInfoByIp(param.getIp());

        //ip校验
        if (containerIpInfoDto != null && containerIpInfoDto.getIp().equals(param.getIp()))
            throw new Exception("The ip:[" + param.getIp() + "] already exists");
        ContainerDto templateContainerDto = containerService.queryContainerById(param.getTemplateContainer());

        //容器校验
        if (templateContainerDto == null || StringUtils.isEmpty(templateContainerDto.getId()))
            throw new Exception("The templateContainer[" + param.getTemplateContainer() + "] does not exists");

        //模板容器类型校验
        if (!"WEB".equals(templateContainerDto.getPurpose()))
            throw new Exception("The purpose is not WEB");

        //项目校验
        ProjectDto projectDto = projectService.queryProjectById(templateContainerDto.getProjectId());
        if (projectDto == null || StringUtils.isEmpty(projectDto.getId()))
            throw new Exception("The project[" + projectDto.getId() + "] does not exists");
        NetLabelInfoDto openvswitch = netService.netLabelInfoQueryByLabel(projectDto.getPlatFormNetworkId());

        //openvswitch 校验
        if (openvswitch == null || StringUtils.isEmpty(openvswitch.getLabel()))
            throw new Exception("project openvswitch  label does not exists");
        //bridge 校验
        NetLabelInfoDto bridge = netService.netLabelInfoQueryByLabel(param.getLabel());
        if (bridge == null || StringUtils.isEmpty(bridge.getLabel()))
            throw new Exception("The label{" + param.getLabel() + "} does not exists");
        if (!"bridge".equals(bridge.getType()))
            throw new Exception("The label type {" + bridge.getType() + "}  is incorrect");
        //应用 主键
        String application = StringUtils.uuidGen();

        String host = templateContainerDto.getHostId();
        String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();

        // 创建模板镜像
        String templateMirror = containerService.createTemplateImage(host, managementIp, application, param.getTemplateContainer());
        LOGGER.debug("Template mirror:[{}]", templateMirror);
        if (StringUtils.isEmpty(templateMirror))
            throw new Exception("templateMirror is empty");

        //创建 负载
        LOGGER.debug("loadsTotal:[{}]", param.getLoadsTotal());
        List<String> containersIp = new ArrayList<>();

        int loadsTotal = param.getLoadsTotal();
        for (int i = 0; i < param.getLoadsTotal(); i++) {
            ContainerIpInfoDto c1 = containerService.createLoadContainer(host, managementIp, templateMirror, templateContainerDto);
            if (c1 == null) {
                loadsTotal--;
                continue;
            }
            ApplicationInfoLoadDto applicationInfoLoadDto = new ApplicationInfoLoadDto();
            applicationInfoLoadDto.setApplication(application);
            applicationInfoLoadDto.setLoadContainer(c1.getContainer());
            applicationDao.loadInsert(applicationInfoLoadDto);
            //查询 ip
            containersIp.add("server" + "   " + c1.getIp());
        }
        LOGGER.debug("containersIp:[{}]------loadsTotal:[{}]", JsonUtil.toJson(containersIp), loadsTotal);

        //生成nginx 配置
        TemplateService templateService = new TemplateServiceImpl();
        LOGGER.debug("containersIp:[{}]  ,NGPort:[{}] , LoadPort:[{}] ", containersIp, param.getNginxPort(), param.getLoadPort());
        String nginxConfig = templateService.getNginxConfigutation(containersIp, param.getLoadPort(), param.getNginxPort(), param.getWeightType());
        LOGGER.debug("nginxConfig:[{}]", nginxConfig);
        if (StringUtils.isEmpty(nginxConfig))
            throw new Exception("nginxConfig is empty");

        //创建nginx 容器
        String nginx = containerService.createNginxContainer(host, managementIp, nginxConfig, application, param.getLabel(), param.getIp(), param.getNginxPort(), templateContainerDto, param.getPrivateKey(), param.getCertificate());

        String accessPath = "";
        if (param.getNginxPort() == 80) {
            accessPath = "http://" + param.getIp();
        } else {
            accessPath = "https://" + param.getIp();
        }

        ApplicationInfoDto applicationInfoDto = new ApplicationInfoDto();
        applicationInfoDto.setId(application);
        applicationInfoDto.setName(param.getName());
        applicationInfoDto.setDescription(param.getDescription());
        applicationInfoDto.setType(param.getType());
        applicationInfoDto.setWeightType(param.getWeightType());
        applicationInfoDto.setIp(param.getIp());
        applicationInfoDto.setLabel(param.getLabel());
        applicationInfoDto.setAccessPath(accessPath);
        applicationInfoDto.setTemplateContainer(param.getTemplateContainer());
        applicationInfoDto.setNginxContainer(nginx);
        applicationInfoDto.setLoadsTotal(loadsTotal);
        applicationInfoDto.setNginxPort(param.getNginxPort());
        applicationInfoDto.setLoadPort(param.getLoadPort());
        applicationInfoDto.setCreateDate(new Date());
        return applicationDao.applicationInsert(applicationInfoDto);
    }

    @Override
    public void applicationDeleteById(String id) throws Exception {
        ContainerServiceImpl containerService = new ContainerServiceImpl();
        ApplicationInfoDto app = applicationQueryById(id);

        if (app == null || StringUtils.isEmpty(app.getId())) {
            LOGGER.debug("The  application:[{}]  does not exists", id);
        } else {
            //删除nginx 容器
            containerService.operationContainer(app.getNginxContainer(), "delete");
        }

        //删除load 容器
        List<ApplicationInfoLoadDto> loadDtos = queryLoadByApplication(id);
        if (loadDtos != null && !loadDtos.isEmpty() && loadDtos.size() > 0) {
            for (ApplicationInfoLoadDto dto : loadDtos) {
                if (dto == null)
                    continue;
                if (StringUtils.isEmpty(dto.getLoadContainer()))
                    continue;
                containerService.operationContainer(dto.getLoadContainer(), "delete");
            }
        }

        //删除负载表
        applicationDao.deleteLoadByApplication(id);
        // 删除关联表tag
        tagDao.tagDeleteByResourceId(id);
        //删除应用表
        applicationDao.applicationDeleteById(id);

        //获取模板容器Dto
        ContainerDto containerDto = containerService.queryContainerById(app.getTemplateContainer());
        HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
        String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId()).getIpAddress();

        DockerClient client = Env.getDockerClient(managementIp);
        String images = "apps/" + app.getId() + "/" + app.getTemplateContainer() + ":1.0";

        LOGGER.debug("Template mirror:[{}]", images);
        try {
            RemoveImageCmd removeImageCmd = client.removeImageCmd(images).withForce(true);
            // 删除模板镜像
            removeImageCmd.exec();
            LOGGER.debug("Delete template mirror successfully!");
        } catch (Exception e) {
            LOGGER.debug("Deleting template image failed", e);
        }
    }

    public List<ApplicationInfoLoadDto> queryLoadByApplication(String id) throws Exception {
        return applicationDao.selectLoadDtoByApplication(id);
    }

    @Override
    public PageDto<ApplicationInfoLoadDto> applicationLoadQueryByConditions(CommonQueryParam param) throws Exception {
        PageDto<ApplicationInfoLoadDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.application_info_load.* FROM  anyuncloud.application_info_load  left   join   anyuncloud.tag_info   on  anyuncloud.application_info_load.application  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.application_info_load";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ApplicationInfoLoadDto> list = JdbcUtils.getList(ApplicationInfoLoadDto.class, sql);
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
            List<ApplicationInfoLoadDto> list = JdbcUtils.getList(ApplicationInfoLoadDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<ApplicationInfoLoadDto> l = JdbcUtils.getList(ApplicationInfoLoadDto.class, sql);
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
    public List<ApplicationInfoLoadDto> addLoad(String id, int amount) throws Exception {
        ContainerService containerService = new ContainerServiceImpl();
        HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
        ProjectService projectService = new ProjectServiceImpl();
        ApplicationInfoDto appDto = applicationQueryById(id);
        if (appDto == null)
            throw new Exception("The Application[{" + id + "}] does not exist");

        String appId = appDto.getId(); //应用Id
        String templateContainer = appDto.getTemplateContainer();//模板容器
        String nginxContainer = appDto.getNginxContainer();//nginx 容器

        ContainerDto templateContainerDto = containerService.queryContainerById(appDto.getTemplateContainer());//获取模板容器 Dto
        String projectId = templateContainerDto.getProjectId();//项目ID

        String hostId = templateContainerDto.getHostId();//宿主机Id
        String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(hostId).getIpAddress();//宿主机管理ip

        // 创建模板镜像
        String templateMirror = containerService.createTemplateImage(hostId, managementIp, appId, templateContainer);

        List<ApplicationInfoLoadDto> list = new ArrayList<>();
        //创建 负载
        for (int i = 0; i < amount; i++) {
            ContainerIpInfoDto c = containerService.createLoadContainer(hostId, managementIp, templateMirror, templateContainerDto);
            ApplicationInfoLoadDto applicationInfoLoadDto = new ApplicationInfoLoadDto();
            applicationInfoLoadDto.setApplication(appId);
            applicationInfoLoadDto.setLoadContainer(c.getContainer());
            ApplicationInfoLoadDto loadDto = applicationDao.loadInsert(applicationInfoLoadDto);
            list.add(loadDto);
        }

        List<String> containersIp = new ArrayList<>();
        List<ApplicationInfoLoadDto> l = applicationDao.selectLoadDtoByApplication(appId);
        ProjectDto projectDto = projectService.queryProjectById(projectId);

        NetService netService = new NetServiceImpl();
        for (ApplicationInfoLoadDto a : l) {
            ContainerIpInfoDto containerIpInfoDto = netService.queryContainerIpInfoByCondition(a.getLoadContainer(), projectDto.getPlatFormNetworkId());
            if (containerIpInfoDto != null) {
                containersIp.add("server" + " " + containerIpInfoDto.getIp());
            }
        }
        LOGGER.debug("containersIp:[{}]", JsonUtil.toJson(containersIp));

        //生成nginx 配置
        TemplateService templateService = new TemplateServiceImpl();
        LOGGER.debug("containersIp:[{}]  ,NGPort:[{}] , LoadPort:[{}] ", containersIp, appDto.getNginxPort(), appDto.getLoadPort());
        String nginxConfig = templateService.getNginxConfigutation(containersIp, appDto.getLoadPort(), appDto.getNginxPort(), appDto.getWeightType());
        LOGGER.debug("nginxConfig:[{}]", nginxConfig);
        if (StringUtils.isEmpty(nginxConfig))
            throw new Exception("nginxConfig is empty");

        //更新nginx 配置
        String confDir = "/apps/" + projectId + "/nginx/" + appId + "/etc";
        String conf = confDir + "/nginx.conf";

        //写入  配置
        File configFile = File.createTempFile("nginx", ".conf");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write(nginxConfig);
        bufferedWriter.close();

        HostSshClient hostSshClient = Env.getSshClient(hostId + ":" + managementIp);

        //copy file
        ScpClient scpClient = new ScpClient(hostSshClient.getSession());
        scpClient.scp(configFile, conf);

        try {
            //重载 ng
            String command = "docker exec -d" + " " + nginxContainer + " " + "/etc/init.d/nginx reload";
            LOGGER.debug("reload  command:[{}]", command);
            hostSshClient.exec(command);
        } catch (Exception r) {
            LOGGER.debug("reload  fail:[{}]", r);
        }

        //更新应用表负载总数量
        applicationDao.updateLoadsTotal(id, containersIp.size());
        return list;
    }


    @Override
    public void operationLoad(ContainerOpParam param) throws Exception {
        String id = param.getId();
        String op = param.getOp();
        if (op.equals("delete")) {
            deleteLoad(id);
        } else {
            ContainerService containerService = new ContainerServiceImpl();
            containerService.operationContainer(id, op);
        }

    }

    public void deleteLoad(String id) throws Exception {
        HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
        ProjectService projectS = new ProjectServiceImpl();
        ContainerService containerS = new ContainerServiceImpl();
        ApplicationInfoLoadDto loadDto = applicationDao.selectLoadDtoById(id);//获取负载Dto
        ContainerDto loadContainerDto = containerS.queryContainerById(id);//获取负载  对应  容器Dto

        if (loadDto == null) {
            LOGGER.debug("The load:[{}] does not exist", id);
            applicationDao.deleteLoad(id); //从负载表删除记录
            if (loadContainerDto != null) //如果 对应 容器存在
                containerS.operationContainer(id, "delete");//删除对应容器

        } else if (loadContainerDto == null) {//如果 对应的容器不存在
            LOGGER.debug("The load container:[{}] does not exist", id);
            applicationDao.deleteLoad(id); //从负载表删除记录

        } else if (applicationDao.applicationSelectById(loadDto.getApplication()) == null) {
            LOGGER.debug("The application:[{}] does not exist", loadDto.getApplication());
            containerS.operationContainer(id, "delete");//删除对应容器
            applicationDao.deleteLoad(loadDto.getLoadContainer());//从负载表中删除记录
            List<ApplicationInfoLoadDto> loadDtoList = applicationDao.selectLoadDtoByApplication(loadDto.getApplication());
            if (loadDtoList != null) {
                for (ApplicationInfoLoadDto loadD : loadDtoList) {
                    if (loadD == null)
                        continue;
                    deleteLoad(loadD.getLoadContainer());
                }
            }
        } else {
            NetService netS = new NetServiceImpl();
            String appId = loadDto.getApplication();//应用Id
            ApplicationInfoDto appDto = applicationDao.applicationSelectById(appId);//应用Dto
            String nginxContainer = appDto.getNginxContainer();//nginx 容器Id
            String projectId = loadContainerDto.getProjectId();//获取项目Id
            ProjectDto projectDto = projectS.queryProjectById(projectId);//获取项目Dto
            String hostId = loadContainerDto.getHostId(); //获取宿主机Id
            String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(hostId).getIpAddress();//宿主机管理Ip
            containerS.operationContainer(id, "delete"); //从表中删除记录
            applicationDao.deleteLoad(id);
            List<String> containersIp = new ArrayList<>();
            List<ApplicationInfoLoadDto> l = applicationDao.selectLoadDtoByApplication(appDto.getId());

            for (ApplicationInfoLoadDto a : l) {
                ContainerIpInfoDto containerIpInfoDto = netS.queryContainerIpInfoByCondition(a.getLoadContainer(), projectDto.getPlatFormNetworkId());
                if (containerIpInfoDto != null) {
                    containersIp.add("server" + " " + containerIpInfoDto.getIp());
                }
            }
            LOGGER.debug("containersIp:[{}]", JsonUtil.toJson(containersIp));

            //生成nginx 配置
            TemplateService templateService = new TemplateServiceImpl();
            LOGGER.debug("containersIp:[{}]  ,NGPort:[{}] , LoadPort:[{}] ", containersIp, appDto.getNginxPort(), appDto.getLoadPort());
            String nginxConfig = templateService.getNginxConfigutation(containersIp, appDto.getLoadPort(), appDto.getNginxPort(), appDto.getWeightType());
            LOGGER.debug("nginxConfig:[{}]", nginxConfig);
            if (StringUtils.isEmpty(nginxConfig))
                throw new Exception("nginxConfig is empty");

            //更新nginx 配置
            String confDir = "/apps/" + projectId + "/nginx/" + appId + "/etc";
            String conf = confDir + "/nginx.conf";

            //写入  配置
            File configFile = File.createTempFile("nginx", ".conf");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
            bufferedWriter.write(nginxConfig);
            bufferedWriter.close();

            HostSshClient hostSshClient = Env.getSshClient(hostId + ":" + managementIp);

            //copy file
            ScpClient scpClient = new ScpClient(hostSshClient.getSession());
            scpClient.scp(configFile, conf);

            try {
                //重载 ng
                String command = "docker exec -d" + " " + nginxContainer + " " + "/etc/init.d/nginx reload";
                LOGGER.debug("reload  command:[{}]", command);
                hostSshClient.exec(command);
            } catch (Exception r) {
                LOGGER.debug("reload  fail:[{}]", r);
            }
            applicationDao.updateLoadsTotal(appDto.getId(), containersIp.size());
        }

    }

    @Override
    public ApplicationInfoDto applicationQueryById(String id) throws Exception {
        return applicationDao.applicationSelectById(id);
    }

    @Override
    public List<ApplicationInfoDto> applicationQueryByProject(String project) throws Exception {
        return applicationDao.applicationSelectByProject(project);
    }

    @Override
    public void applicationDeleteByContainer(String container) throws Exception {
        ApplicationInfoDto applicationInfoDto = queryApplicationDtoByTemplateContainer(container);
        if (applicationInfoDto != null)
            applicationDeleteById(applicationInfoDto.getId());
    }

    private ApplicationInfoDto queryApplicationDtoByTemplateContainer(String container) throws Exception {
        return applicationDao.selectApplicationDtoByTemplateContainer(container);
    }

    @Override
    public PageDto<ApplicationInfoDto> applicationQueryByConditions(CommonQueryParam param) throws Exception {
        PageDto<ApplicationInfoDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.application_info_base.* FROM  anyuncloud.application_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.application_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.application_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ApplicationInfoDto> list = JdbcUtils.getList(ApplicationInfoDto.class, sql);
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
            List<ApplicationInfoDto> list = JdbcUtils.getList(ApplicationInfoDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<ApplicationInfoDto> l = JdbcUtils.getList(ApplicationInfoDto.class, sql);
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
    public ApplicationInfoDto republish(String appId) throws Exception {
        /*
        * 步骤：
        * 1、 删除负载
        * 2、 删除旧模板镜像
        * 3、 创建新镜像镜像
        * 3、 创建负载
        * 4、 生成新配置 更新配置 重载nginx服务
        */
        ApplicationInfoDto appDto = applicationDao.applicationSelectById(appId);// 应用Dto
        if (appDto == null)
            throw new Exception("The app does not exist");

        List<ApplicationInfoLoadDto> loadDtoList = applicationDao.selectLoadDtoByApplication(appId);//负载Dto
        if (loadDtoList == null)
            throw new Exception("loadDtoList is empty");

        ContainerService containerS = new ContainerServiceImpl();

        //删除负载
        LOGGER.debug("-----------------delete load start----------------------");
        for (ApplicationInfoLoadDto lDto : loadDtoList) {
            if (lDto == null)
                continue;
            if (lDto.getLoadContainer() == null)
                continue;
            if ("".equals(lDto.getLoadContainer()))
                continue;
            containerS.operationContainer(lDto.getLoadContainer(), "delete");//删除负载容器
            applicationDao.deleteLoad(lDto.getLoadContainer()); //从负载表中删除记录
            tagDao.tagDeleteByResourceId(lDto.getLoadContainer());// 删除关联表tag
            LOGGER.debug("-----------------delete load:[{}] success------", lDto.getLoadContainer());
        }
        applicationDao.deleteLoadByApplication(appId); //删除残余数据
        LOGGER.debug("-----------------delete load  end-------------------------");


        HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
        String templateContainer = appDto.getTemplateContainer();
        ContainerDto templateContainerDto = containerS.queryContainerById(templateContainer);// 模板容器Dto
        int loadsTotal = appDto.getLoadsTotal();//获取负载负载数量
        String projectId = templateContainerDto.getProjectId();//项目Id
        String hostId = templateContainerDto.getHostId();//宿主机Id
        String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(hostId).getIpAddress();

        //生成新的模板镜像
        DockerClient client = Env.getDockerClient(managementIp);
        String images = "apps/" + appId + "/" + templateContainer + ":1.0";
        LOGGER.debug("Template mirror:[{}]", images);
        try {
            RemoveImageCmd removeImageCmd = client.removeImageCmd(images).withForce(true);
            // 删除旧模板镜像
            removeImageCmd.exec();
            LOGGER.debug("------Delete template mirror successfully!");
            CommitCmd commitCmd = client.commitCmd(templateContainer).withRepository("apps/" + appId + "/" + templateContainer).withTag("1.0");
            // 创建新的模板镜像
            String str = commitCmd.exec();
            LOGGER.debug("------create template mirror successfully! :[{}]", str);
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }


        //创建负载
        List<String> containersIp = new ArrayList<>();
        for (int i = 0; i < loadsTotal; i++) {
            ContainerIpInfoDto c1 = containerS.createLoadContainer(hostId, managementIp, images, templateContainerDto);
            if (c1 == null)
                continue;
            ApplicationInfoLoadDto applicationInfoLoadDto = new ApplicationInfoLoadDto();
            applicationInfoLoadDto.setApplication(appId);
            applicationInfoLoadDto.setLoadContainer(c1.getContainer());
            applicationDao.loadInsert(applicationInfoLoadDto);
            containersIp.add("server" + "   " + c1.getIp());
        }

        //生成nginx 配置
        TemplateService templateService = new TemplateServiceImpl();
        LOGGER.debug("containersIp:[{}]  ,NGPort:[{}] , LoadPort:[{}] ", containersIp, appDto.getNginxPort(), appDto.getLoadPort());
        String nginxConfig = templateService.getNginxConfigutation(containersIp, appDto.getLoadPort(), appDto.getNginxPort(), appDto.getWeightType());
        LOGGER.debug("nginxConfig:[{}]", nginxConfig);
        if (StringUtils.isEmpty(nginxConfig))
            throw new Exception("nginxConfig is empty");


        //更新nginx 配置
        String confDir = "/apps/" + projectId + "/nginx/" + appId + "/etc";
        String conf = confDir + "/nginx.conf";

        //写入  配置
        File configFile = File.createTempFile("nginx", ".conf");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write(nginxConfig);
        bufferedWriter.close();

        HostSshClient hostSshClient = Env.getSshClient(hostId + ":" + managementIp);

        //copy file
        ScpClient scpClient = new ScpClient(hostSshClient.getSession());
        scpClient.scp(configFile, conf);

        String nginxContainer = appDto.getNginxContainer();//nginx 容器Id
        try {
            //重载 nginx 服务
            String command = "docker exec -d" + " " + nginxContainer + " " + "/etc/init.d/nginx reload";
            LOGGER.debug("reload  command:[{}]", command);
            hostSshClient.exec(command);
        } catch (Exception r) {
            LOGGER.debug("reload  fail:[{}]", r);
        }
        return applicationDao.applicationSelectById(appId);
    }
}
