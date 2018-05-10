package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DatabaseCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.common.ScpClient;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.DatabaseDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.DatabaseDaoImpl;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.NetLabelInfoDaoImpl;
import com.anyun.esb.component.host.service.TemplateService;
import com.anyun.esb.component.host.service.docker.*;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 7/4/17.
 */
public class DatabaseServiceImpl implements DatabaseService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseServiceImpl.class);
    private DatabaseDao databaseDao = new DatabaseDaoImpl();


    @Override
    public DatabaseDto getDatabaseDtoById(String id, String userUniqueId) throws Exception {
        return databaseDao.selectDatabaseDtoById(id, userUniqueId);
    }

    @Override
    public PageDto<DatabaseDto> getPageDtoByCondition(CommonQueryParam param) throws Exception {
        PageDto<DatabaseDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.database_info_base.* FROM  anyuncloud.database_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.database_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.database_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<DatabaseDto> list = JdbcUtils.getList(DatabaseDto.class, sql);
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
            List<DatabaseDto> list = JdbcUtils.getList(DatabaseDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<DatabaseDto> l = JdbcUtils.getList(DatabaseDto.class, sql);
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
    public void deleteById(String id) throws Exception {
        List<DatabaseContainerDto> list = databaseDao.selectDatabaseContainerDtoListByDatabaseId(id);
        ContainerService containerS = new ContainerServiceImpl();
        if (list != null && list.size() > 0) {
            for (DatabaseContainerDto dc : list) {
                if (dc == null || StringUtils.isEmpty(dc.getContainerId()))
                    continue;
                String containerId = dc.getContainerId();
                containerS.operationContainer(containerId, "delete");
                databaseDao.deleteDbContainerByContainerId(containerId);
            }
            databaseDao.deleteDbContainerByDatabaseId(id);
        }
        DatabaseDto databaseDto = getDatabaseDtoById(id, "");
        if (databaseDto != null)
            databaseDao.deleteById(id);
    }

    @Override
    public DatabaseDto createDatabase(DatabaseCreateParam param) throws Exception {
        LOGGER.debug("----------param:[{}]", param.asJson());
        String projectId = param.getProjectId();//项目ID
        String userUniqueId = param.getUserUniqueId();//用户唯一标识
        String name = param.getName();//名称
        String describe = param.getDescribe(); //描述
        String type = param.getType();//类型 single：单点   cluster：集群
        String bridgeL3 = param.getBridgeL3();// bridge 类型的l3网络标签
        String bridgeL3Ip = param.getBridgeL3Ip(); //bridge 类型的l3网络标签 分配的 ip
        String userName = param.getUserName();  //数据库用户名(root，111111 为默认 用户名和密码)
        String password = param.getPassword(); //数据库密码
        int sqldNodeTotal = param.getSqldNodeTotal();// mysqld节点(和NDBD节点共用容器) 数量  如果type 为 cluster ，则 数量大于等于2 ,其他不做判断

        //非空参数校验
        if (StringUtils.isEmpty(projectId))
            throw new Exception("projectId is empty");
        if (StringUtils.isEmpty(bridgeL3))
            throw new Exception("bridgeL3 is empty");
        if (StringUtils.isEmpty(bridgeL3Ip))
            throw new Exception("bridgeL3Ip is empty");
        if (StringUtils.isEmpty(type))
            throw new Exception("type is empty");

        //进一步验证参数
        ProjectService pService = new ProjectServiceImpl();
        ProjectDto pDto = pService.queryProjectById(projectId);
        if (pDto == null)
            throw new Exception("The project does not exist");
        String platFormNetworkId = pDto.getPlatFormNetworkId();
        if (StringUtils.isEmpty(platFormNetworkId))
            throw new Exception("platFormNetworkId is empty");
        NetService ntService = new NetServiceImpl();
        NetLabelInfoDto nDto = ntService.netLabelInfoQueryByLabel(platFormNetworkId);
        if (nDto == null)
            throw new Exception("The platFormNetworkId does not exist");
        NetLabelInfoDto l3Dto = ntService.netLabelInfoQueryByLabel(bridgeL3);
        if (l3Dto == null)
            throw new Exception("The bridgeL3 does not exist");
        if (!l3Dto.getType().equals("bridge"))
            throw new Exception("The bridgeL3 type  error");

        NetLabelInfoDao nDao = new NetLabelInfoDaoImpl();
        ContainerIpInfoDto cIpDto = nDao.selectContainerIpInfoByIp(bridgeL3Ip);
        if (cIpDto != null)
            throw new Exception("bridgeL3Ip already exists");

        if (StringUtils.isNotEmpty(userName)) { //用户名非空
            if ("root".equals(userName)) //用户名是root
                throw new Exception("userName [root] is default,Please create another user");
            else {
                if (StringUtils.isEmpty(password)) //密码为空
                    throw new Exception("password is empty");
            }
        }

        DatabaseDto databaseDto = null; //定义返回参数
        VSwitchService vSwitchService = new VSwitchServiceImpl();
        DockerHostService dockerHostService = new DockerHostServiceImpl();
        DockerClient client = null;  //创建连接
        HostSshClient hostSshClient = null;
        List<String> hostIpList = dockerHostService.findAllActiveHosts();//获取 zk所有宿主机信息
        LOGGER.debug("Find action host:[{}]", hostIpList);
        if (hostIpList.isEmpty())
            throw new Exception("Does not  find  action host");
        String host = hostIpList.get(0);          //zk  里面取出第一个宿主机信息
        String hostId = host.split(":")[0];   //    宿主机Id
        String hostIp = host.split(":")[1]; // 宿主机管理ip
        LOGGER.debug("取第一个-----host:[{}]", host);
        LOGGER.debug("-----------hostId:[{}]", hostId);
        LOGGER.debug("-----------hostIp:[{}]", hostIp);
        ContainerDao cDao = new ContainerDaoImpl();
        HostBaseInfoDao hDao = new HostBaseInfoDaoImpl();
        HostExtInfoDto hExtDto = hDao.selectHExtDtoByHostId(hostId);
        String cpuFamily = hExtDto.getCpuModel();
        InspectImageResponse inspectImageResponse = null;
        String databaseId = StringUtils.uuidGen();

        switch (type) {
            case "single": //单节点
                String mysqlImage = "imagehub.anyuncloud.com/database/mysql:5.5.46";//定义mysql镜像
                String imageId = "C1A058B16C9194F7D48AFAADB7BE3CE8";//镜像id

                LOGGER.debug("----------mysqlImage:[{}]", mysqlImage);
                client = Env.getDockerClient(hostIp);
                try {
                    inspectImageResponse = client.inspectImageCmd(mysqlImage).exec();
                    LOGGER.debug("----The images exists");
                } catch (Exception e) {
                    LOGGER.debug("宿主机器[{}]：没有镜像：[{}]", hostIp, mysqlImage);
                    if (inspectImageResponse == null) {
                        LOGGER.debug("pull  images .............");
                        PullImageResultCallback callback = new PullImageResultCallback();
                        client.pullImageCmd(mysqlImage).exec(callback);
                        callback.awaitSuccess();
                        LOGGER.debug("this:[{}]  imageId:[{}] pull success！", hostIp, mysqlImage);
                    }
                }
                hostSshClient = Env.getSshClient(host);
                Hashids hashids = new Hashids(StringUtils.uuidGen());
                String softId = hashids.encode(new Date().getTime());
                String createCommand = "docker run  -idt   --name" + " " + softId + " " + "--ip" + " " + bridgeL3Ip + "  " + "--net" + " " + bridgeL3 + " " + mysqlImage;//创建mysql 命令
                String response = hostSshClient.exec(createCommand);
                LOGGER.debug("------------response:[{}]", response);

                //保存容器
                Date date = new Date(); //获取当前时间
                ContainerDto cDto = new ContainerDto();
                cDto.setId(response);
                cDto.setProjectId(projectId);
                cDto.setName("mysql");
                cDto.setHostName("mysql");
                cDto.setShortId(softId);
                cDto.setHostId(hostId);
                cDto.setCpuCoreLimit(0);
                cDto.setCpuFamily(cpuFamily);
                cDto.setStatus(2);
                cDto.setType(3);
                cDto.setPurpose("MYSQL-SINGLE");
                cDto.setImageId(imageId);
                cDto.setPrivileged(0);
                cDto.setMemoryLimit(0);
                cDto.setMemorySwapLimit(0);
                cDto.setCreateTime(date);
                cDto.setLastModifyTime(date);
                cDao.insertContainer(cDto);

                Map<String, Object> m1 = vSwitchService.queryContainerIpAddress(bridgeL3, response);
                String ip = m1.get("ip").toString();
                String mac = m1.get("mac").toString();
                nDao.insertContainerNetIpInfo(response, bridgeL3, ip, mac);//插入到关联表

                //连上项目网络
                String connectOVSCommand = "docker network connect" + " " + platFormNetworkId + " " + response;
                hostSshClient.exec(connectOVSCommand);
                Map<String, Object> m2 = vSwitchService.queryContainerIpAddress(platFormNetworkId, response);
                ip = m2.get("ip").toString();
                mac = m2.get("mac").toString();
                nDao.insertContainerNetIpInfo(response, platFormNetworkId, ip, mac);//插入到关联表
                String mysqlSingleDir = "/db/mysql/single/" + databaseId;
                String createFolderCommand = "mkdir -p" + " " + mysqlSingleDir; //创建mysql db 文件夹
                hostSshClient.exec(createFolderCommand);


                if (StringUtils.isEmpty(userName)) {
                    hostSshClient.exec("docker exec -d" + " " + response + " " + "service mysql start");//启动mysql服务
                } else {
                    hostSshClient.exec("docker exec" + " " + response + " " + "service mysql start");//启动mysql服务
                    hostSshClient.exec("  touch -f" + " " + mysqlSingleDir + "/client.cnf"); //在宿主机上创建文件 本地文件   client.cnf
                    hostSshClient.exec("  echo  '[client]' >" + " " + mysqlSingleDir + "/client.cnf"); // 写入  内容
                    hostSshClient.exec("  echo   " + "'" + "password='111111'" + "'" + ">>" + " " + mysqlSingleDir + "/client.cnf"); // 写入   内容
                    hostSshClient.exec("  echo   " + "'" + "user='root' " + "'" + ">>" + " " + mysqlSingleDir + "/client.cnf"); // 写入   内容
                    hostSshClient.exec("docker cp" + " " + mysqlSingleDir + "/client.cnf" + " " + response + ":/etc/mysql/"); //cp 文件到容器
                    LOGGER.debug("--------The command of cat /etc/mysql/client.cnf  get  result is:[{}]", hostSshClient.exec("docker exec " + " " + response + " " + "cat /etc/mysql/client.cnf"));
                    hostSshClient.exec("docker exec" + " " + response + " " + "service mysql reload");//重置  mysql
                    String create = "CREATE USER" + " " + "'" + userName + "'@'%' IDENTIFIED BY" + " " + "'" + password + "'"; //创建新用户和密码
                    String createUserCommand = "docker exec" + " " + response + " " + "mysql --defaults-file=/etc/mysql/client.cnf  -u root  -e  \" " + create + "  \"  ";
                    LOGGER.debug("-------create user password command:[{}]", createUserCommand);
                    hostSshClient.exec(createUserCommand);
                    hostSshClient.exec("docker exec -d" + " " + response + " " + "service mysql reload");//重置  mysql
                }

                //保存节点
                DatabaseContainerDto dbC = new DatabaseContainerDto();
                dbC.setContainerId(response);
                dbC.setDatabaseId(databaseId);
                databaseDao.insertDbContainer(dbC);

                //保存 数据库服务
                DatabaseDto dDto = new DatabaseDto();
                dDto.setId(databaseId);
                dDto.setProjectId(projectId);
                dDto.setType(type);
                dDto.setName(name);
                dDto.setDescribe(describe);
                dDto.setBridgeL3(bridgeL3);
                dDto.setBridgeL3Ip(bridgeL3Ip);
                dDto.setCreateTime(new Date());
                databaseDto = databaseDao.insertDatabase(dDto);
                break;

            case "cluster"://集群
                if (sqldNodeTotal < 2)
                    throw new Exception("sqldNodeTotal < 2");
                if (sqldNodeTotal % 2 != 0)
                    throw new Exception("SqldNodeTotal Must be even numbers");
                String mysqlClusterImage = "imagehub.anyuncloud.com/cluster/mysql:latest";
                String mysqlClusterImageId = "7B5E95A5CCF9E365B96AA719704BF0A0";

                String haproxyImage = "imagehub.anyuncloud.com/load/haproxy:latest";
                String haproxyImageId = "70032426D726B5799C78495C5EB15DC4";


                hostSshClient = Env.getSshClient(host);
                client = Env.getDockerClient(hostIp);

                LOGGER.debug("到宿主机上查询镜像 并 pull------------------------------------------------------------------------------------------------");
                String listAllImagesCommand = "docker images | awk -F ' ' '{printf \"%s:%s\\n\", $1,$2}'";
                LOGGER.debug("-----listAllImagesCommand:[{}]", listAllImagesCommand);
                String result = hostSshClient.exec(listAllImagesCommand); //列出宿主机所有镜像  format  REPOSITORY:TAG
                List<String> imagesList = StringUtils.readStringLines(result);
                if (imagesList.contains(mysqlClusterImage))
                    LOGGER.debug("[{}]  exists", mysqlClusterImage);
                else {
                    LOGGER.debug("pull:[{}].............", mysqlClusterImage);
                    PullImageResultCallback callback = new PullImageResultCallback();
                    client.pullImageCmd(mysqlClusterImage).exec(callback);
                    callback.awaitSuccess();
                    LOGGER.debug("mysqlClusterImage:[{}] pull success！", mysqlClusterImage);
                }

                if (imagesList.contains(haproxyImage))
                    LOGGER.debug("[{}] exists", haproxyImage);
                else {
                    LOGGER.debug("pull  [{}].............", haproxyImage);
                    PullImageResultCallback callback = new PullImageResultCallback();
                    client.pullImageCmd(haproxyImage).exec(callback);
                    callback.awaitSuccess();
                    LOGGER.debug("haproxyImage:[{}] pull success！", haproxyImage);
                }


                //定义 通用变量
                String soft, resp, createCmd, mIp, mMac, mgmIp, haproxyId, mgmId;
                ContainerDto dto;
                Date da;
                Map<String, Object> m;
                DatabaseContainerDto dbContainer;
                File configFile;
                BufferedWriter bufferedWriter;
                List<String> mysqldIps = new ArrayList<>();
                List<String> mysqlIdList = new ArrayList<>();

                LOGGER.debug("创建mysqld 容器   start----------------------------------------------------------------------------------------------------------------------------------------");
                for (int i = 0; i < sqldNodeTotal; i++) {
                    soft = new Hashids(StringUtils.uuidGen()).encode(new Date().getTime());
                    createCmd = "docker run  -idt  --name" + " " + soft + " " + "  " + "--net" + " " + platFormNetworkId + " " + mysqlClusterImage + " " + "/bin/bash";
                    resp = hostSshClient.exec(createCmd);
                    if (i == sqldNodeTotal - 1 && StringUtils.isNotEmpty(userName)) {
                        hostSshClient.exec("docker exec" + " " + resp + " " + "/etc/init.d/mysql.server start");              //启动mysql服务
                    } else {
                        hostSshClient.exec("docker exec -d" + " " + resp + " " + "/etc/init.d/mysql.server start");              //启动mysql服务
                    }
                    LOGGER.debug("------------resp:[{}]", resp);
                    mysqlIdList.add(resp);
                    //保存容器
                    da = new Date(); //获取当前时间
                    dto = new ContainerDto();
                    dto.setId(resp);
                    dto.setProjectId(projectId);
                    dto.setName("mysqld");
                    dto.setHostName("mysqld");
                    dto.setShortId(soft);
                    dto.setHostId(hostId);
                    dto.setCpuCoreLimit(0);
                    dto.setCpuFamily(cpuFamily);
                    dto.setStatus(2);
                    dto.setType(6);
                    dto.setPurpose("MYSQL-CLUSTER");
                    dto.setImageId(mysqlClusterImageId);
                    dto.setPrivileged(0);
                    dto.setMemoryLimit(0);
                    dto.setMemorySwapLimit(0);
                    dto.setCreateTime(da);
                    dto.setLastModifyTime(da);
                    cDao.insertContainer(dto);
                    m = vSwitchService.queryContainerIpAddress(platFormNetworkId, resp);
                    mIp = m.get("ip").toString();
                    mMac = m.get("mac").toString();
                    nDao.insertContainerNetIpInfo(resp, platFormNetworkId, mIp, mMac);//插入到关联表
                    //保存节点
                    dbContainer = new DatabaseContainerDto();
                    dbContainer.setContainerId(resp);
                    dbContainer.setDatabaseId(databaseId);
                    databaseDao.insertDbContainer(dbContainer);
                    mysqldIps.add(mIp);
                }
                LOGGER.debug("创建mysqld 容器 end ------------------------------------------------------------------------------------------------------------------------------------");


                LOGGER.debug("创建 haproxy 容器 start----------------------------------------------------------------------------------------------------------------------------------");
                soft = new Hashids(StringUtils.uuidGen()).encode(new Date().getTime());
                createCmd = "docker run  -idt   --name" + " " + soft + " " + "--ip" + " " + bridgeL3Ip + "  " + "--net" + " " + bridgeL3 + " " + haproxyImage + " " + "/bin/bash";
                resp = hostSshClient.exec(createCmd);
                LOGGER.debug("------------resp:[{}]", resp);
                haproxyId = resp;
                //保存容器
                da = new Date(); //获取当前时间
                dto = new ContainerDto();
                dto.setId(resp);
                dto.setProjectId(projectId);
                dto.setName("haproxy");
                dto.setHostName("haproxy");
                dto.setShortId(soft);
                dto.setHostId(hostId);
                dto.setCpuCoreLimit(0);
                dto.setCpuFamily(cpuFamily);
                dto.setStatus(2);
                dto.setType(4);
                dto.setPurpose("MYSQL-CLUSTER");
                dto.setImageId(haproxyImageId);
                dto.setPrivileged(0);
                dto.setMemoryLimit(0);
                dto.setMemorySwapLimit(0);
                dto.setCreateTime(da);
                dto.setLastModifyTime(da);
                cDao.insertContainer(dto);
                m = vSwitchService.queryContainerIpAddress(bridgeL3, resp);
                mIp = m.get("ip").toString();
                mMac = m.get("mac").toString();
                nDao.insertContainerNetIpInfo(resp, bridgeL3, mIp, mMac);//插入到关联表
                //连上项目网络
                hostSshClient.exec("docker network connect" + " " + platFormNetworkId + " " + resp);
                m = vSwitchService.queryContainerIpAddress(platFormNetworkId, resp);
                mIp = m.get("ip").toString();
                mMac = m.get("mac").toString();
                nDao.insertContainerNetIpInfo(resp, platFormNetworkId, mIp, mMac);//插入到关联表
                //保存节点
                dbContainer = new DatabaseContainerDto();
                dbContainer.setContainerId(resp);
                dbContainer.setDatabaseId(databaseId);
                databaseDao.insertDbContainer(dbContainer);
                LOGGER.debug("创建 haproxy 容器 end----------------------------------------------------------------------------------------------------------------------------------");

                LOGGER.debug("创建mgm 容器 start------------------------------------------------------------------------------------------------------------------------------------------------------");
                soft = new Hashids(StringUtils.uuidGen()).encode(new Date().getTime());
                createCmd = "docker run  -idt  --name" + " " + soft + " " + "  " + "--net" + " " + platFormNetworkId + " " + mysqlClusterImage + " " + "/bin/bash";
                resp = hostSshClient.exec(createCmd);
                LOGGER.debug("------------resp:[{}]", resp);
                mgmId = resp;
                //保存容器
                da = new Date(); //获取当前时间
                dto = new ContainerDto();
                dto.setId(resp);
                dto.setProjectId(projectId);
                dto.setName("mgm");
                dto.setHostName("mgm");
                dto.setShortId(soft);
                dto.setHostId(hostId);
                dto.setCpuCoreLimit(0);
                dto.setCpuFamily(cpuFamily);
                dto.setStatus(2);
                dto.setType(5);
                dto.setPurpose("MYSQL-CLUSTER");
                dto.setImageId(mysqlClusterImageId);
                dto.setPrivileged(0);
                dto.setMemoryLimit(0);
                dto.setMemorySwapLimit(0);
                dto.setCreateTime(da);
                dto.setLastModifyTime(da);
                cDao.insertContainer(dto);
                m = vSwitchService.queryContainerIpAddress(platFormNetworkId, resp);
                mIp = m.get("ip").toString();
                mMac = m.get("mac").toString();
                mgmIp = mIp;
                nDao.insertContainerNetIpInfo(resp, platFormNetworkId, mIp, mMac);//插入到关联表
                //保存节点
                dbContainer = new DatabaseContainerDto();
                dbContainer.setContainerId(resp);
                dbContainer.setDatabaseId(databaseId);
                databaseDao.insertDbContainer(dbContainer);
                LOGGER.debug("创建mgm 容器 end------------------------------------------------------------------------------------------------------------------------------------------------------");


                LOGGER.debug("生成配置文件---------------------------------------------------------------------------------------------------------------------------------------------");
                TemplateService templateService = new TemplateServiceImpl();
                String mysqldConf = templateService.getMysqldConfiguation(mgmIp);
                String haproxyConf = templateService.getHaproxyConfigutation(mysqldIps);
                String mgmConf = templateService.getMgmConfiguation(mgmIp, mysqldIps);
                if (StringUtils.isEmpty(mgmConf))
                    throw new Exception("mgmConf is  empty");
                if (StringUtils.isEmpty(mysqldConf))
                    throw new Exception("mysqldConf is  empty");
                if (StringUtils.isEmpty(haproxyConf))
                    throw new Exception("haproxyConf is  empty");

                String mysqlClusterDir = "/db/mysql/cluster/" + databaseId;
                String createFolderCmd = "mkdir -p" + " " + mysqlClusterDir; //创建mysql db 文件夹
                hostSshClient.exec(createFolderCmd);
                ScpClient scpClient = new ScpClient(hostSshClient.getSession());
                configFile = File.createTempFile("mgm", ".conf");
                bufferedWriter = new BufferedWriter(new FileWriter(configFile));
                bufferedWriter.write(mgmConf);  // 写入 mgm 配置信息
                bufferedWriter.close();
                //copy 到宿主机
                scpClient.scp(configFile, mysqlClusterDir + "/mgm.conf");
                try {//删除临时文件
                    if (configFile.exists())
                        configFile.delete();
                } catch (Exception d) {
                    LOGGER.debug("configFile delete  fail:[{}]", d);
                }


                configFile = File.createTempFile("haproxy", ".conf");
                bufferedWriter = new BufferedWriter(new FileWriter(configFile));
                bufferedWriter.write(haproxyConf);//写入 haproxy 配置
                bufferedWriter.close();
                //copy 到宿主机
                scpClient.scp(configFile, mysqlClusterDir + "/haproxy.conf");
                try {//删除临时文件
                    if (configFile.exists())
                        configFile.delete();
                } catch (Exception d) {
                    LOGGER.debug("configFile delete  fail:[{}]", d);
                }


                configFile = File.createTempFile("mysqld", ".conf");
                bufferedWriter = new BufferedWriter(new FileWriter(configFile));
                bufferedWriter.write(mysqldConf);//写入 mysqld 配置
                bufferedWriter.close();
                //copy 到宿主机
                scpClient.scp(configFile, mysqlClusterDir + "/mysqld.conf");
                try {//删除临时文件
                    if (configFile.exists())
                        configFile.delete();
                } catch (Exception d) {
                    LOGGER.debug("configFile delete  fail:[{}]", d);
                }

                if (StringUtils.isNotEmpty(userName)) {
                    hostSshClient.exec("  touch " + " " + mysqlClusterDir + "/client.cnf"); //在宿主机上创建文件  client.cnf
                    hostSshClient.exec("  echo  '[client]'  >" + " " + mysqlClusterDir + "/client.cnf"); // 写入  内容
                    hostSshClient.exec("  echo   " + "'" + "password='111111'" + "'" + ">>" + " " + mysqlClusterDir + "/client.cnf"); // 写入   内容
                    hostSshClient.exec("  echo   " + "'" + "user='root' " + "'" + ">>" + " " + mysqlClusterDir + "/client.cnf"); // 写入   内容
                    for (String ss : mysqlIdList) {
                        hostSshClient.exec("docker cp" + " " + mysqlClusterDir + "/client.cnf" + " " + ss + ":/root"); //cp 文件到容器
                        String create = "CREATE USER" + " " + "'" + userName + "'@'%' IDENTIFIED BY" + " " + "'" + password + "'"; //创建新用户和密码
                        String createUserCommand = "docker exec" + " " + ss + " " + "/usr/local/mysql/bin/mysql  --defaults-file=/root/client.cnf  -u root  -e  \" " + create + "  \"  ";
                        LOGGER.debug("-------create user password command:[{}]", createUserCommand);
                        hostSshClient.exec(createUserCommand);
                        hostSshClient.exec("docker exec -d" + " " + ss + " " + "/etc/init.d/mysql.server reload");//重启 mysql  生效
                    }
                }

                //cp 到容器  并启动相性服务
                hostSshClient.exec("docker cp" + " " + mysqlClusterDir + "/mgm.conf " + " " + mgmId + ":/usr/local/mysql/mysql-cluster/config.ini");
                hostSshClient.exec("docker exec" + " " + mgmId + " " + "/usr/local/bin/ndb_mgmd -f /usr/local/mysql/mysql-cluster/config.ini --initial");

                for (String s : mysqlIdList) {
                    hostSshClient.exec("docker cp" + " " + mysqlClusterDir + "/mysqld.conf " + " " + s + ":/etc/my.cnf");
                    hostSshClient.exec("docker exec -d" + " " + s + " " + "/usr/local/bin/ndbd --initial");
                }

                for (String ss : mysqlIdList)
                    hostSshClient.exec("docker exec" + " " + ss + " " + "/usr/local/mysql/bin/mysqld_safe --user=mysql &");

                hostSshClient.exec("docker cp" + " " + mysqlClusterDir + "/haproxy.conf " + " " + haproxyId + ":/etc/haproxy/haproxy.cfg");
                hostSshClient.exec("docker exec -d" + " " + haproxyId + " " + "service haproxy start");

                //保存 数据库服务
                DatabaseDto dbDto = new DatabaseDto();
                dbDto.setId(databaseId);
                dbDto.setProjectId(projectId);
                dbDto.setType(type);
                dbDto.setName(name);
                dbDto.setDescribe(describe);
                dbDto.setBridgeL3(bridgeL3);
                dbDto.setBridgeL3Ip(bridgeL3Ip);
                dbDto.setCreateTime(new Date());
                databaseDto = databaseDao.insertDatabase(dbDto);
                break;
            default:
                throw new Exception("This type:[" + type + "] is not supported");
        }
        return databaseDto;
    }


    @Override
    public void deleteDbByProjectId(String projectId) throws Exception {
        List<DatabaseDto> l = selectDatabaseDtoListByProjectId(projectId);
        if (l != null) {
            for (DatabaseDto dDto: l) {
                if (dDto == null)
                    continue;
                if (StringUtils.isEmpty(dDto.getId()))
                    continue;
                deleteById(dDto.getId());
            }
        }
    }

    @Override
    public List<DatabaseDto> selectDatabaseDtoListByProjectId(String projectId) throws Exception {
        return databaseDao.selectDatabaseDtoListByProjectId(projectId);
    }
}