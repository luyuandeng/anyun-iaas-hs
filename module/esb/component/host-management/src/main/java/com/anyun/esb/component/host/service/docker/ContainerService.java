package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.*;

import java.util.List;

/**
 * Created by gaopeng on 16-6-7.
 */
public interface ContainerService {
    /**
     * 创建容器  指定宿主机
     *
     * @param param
     * @return
     */
    ContainerDto  createDefaultContainer(ContainerCreateByConditionParam param) throws Exception;

    /**
     * 启动容器  根据 id
     *
     * @param id
     * @return
     */
    void startContainer(String id) throws Exception;

    /**
     * 停止容器 根据 id
     *
     * @param id
     * @return
     */
    void stopContainer(String id) throws Exception;

    /**
     * 删除容器  根据id
     *
     * @param id
     * @return
     */
    void deleteContainer(String id) throws Exception;

    /**
     * 删除容器 根据项目
     *
     * @param project
     * @return
     */
    void deleteContainerByProject(String project) throws Exception;

    /**
     * 根据 id  查询容器
     *
     * @return ContainerDto
     * @Param id
     */
    ContainerDto queryContainerById(String id) throws Exception;

    /**
     * 根据 id  查询容器
     *
     * @param type
     * @return ContainerDto
     * @Param id
     */
    ContainerDto queryContainerById(String id, int type) throws Exception;


    /**
     * 根据 project 查询容器
     *
     * @param type
     * @return ContainerDto
     * @Param project
     */
    List<ContainerDto> queryContainerByProject(String project, int type) throws Exception;

    /**
     * 根据  label 查询容器
     *
     * @param type
     * @return ContainerDto
     * @Param netLabel
     */
    List<ContainerDto> queryContainerByNetLabel(String label, int type) throws Exception;

    /**
     * 根据  label 查询容器
     *
     * @return ContainerDto
     * @Param netLabel
     */
    List<ContainerDto> queryContainerByNetLabel(String label) throws Exception;

    /**
     * 查询待发布的WEB容器
     *
     * @param type
     * @return ContainerDto
     * @Param project
     * @Param purpose
     */
    List<ContainerDto> queryContainerToBePublished(String project, String purpose, int type) throws Exception;

    /**
     * 根据  image 查询容器
     *
     * @param type
     * @return ContainerDto
     * @Param image
     */
    List<ContainerDto> queryContainerByImage(String image, int type) throws Exception;


    /**
     * 创建负载容器
     *
     * @param host           宿主机 Id
     * @param managementIp   宿主机管理Ip
     * @param templateMirror 模板镜像
     * @param containerDto   模板容器Dto
     * @return ContainerIpInfoDto
     */
    ContainerIpInfoDto createLoadContainer(String host, String managementIp, String templateMirror, ContainerDto containerDto) throws Exception;


    /**
     * 创建NG容器
     *
     * @param host         宿主机 Id
     * @param managementIp 宿主机管理Ip
     * @param nginxConfig  nginx  配置文件
     * @param application  应用Id
     * @param label        bridge 类型标签
     * @param ip           nginx  ip
     * @param port         nginx  port
     * @param privateKey   服务器 私钥
     * @param certificate  服务器 证书
     * @return
     * @apram templateContainerDto  模板容器Dto
     */
    String createNginxContainer(String host, String managementIp, String nginxConfig, String application, String label, String ip, int port, ContainerDto templateContainerDto, String privateKey, String certificate) throws Exception;

    /**
     * 停止容器
     *
     * @param id
     * @param type
     * @return
     */
    void stopContainer(String id, String type) throws Exception;


    /**
     * 查询宿主机器上所有容器
     *
     * @param host
     * @return List<ContainerDto>
     */
    List<ContainerDto> queryAllContainerByHost(String host) throws Exception;


    /**
     * 获取宿主机的运行时间
     *
     * @param host
     * @return SystemUptimeDto
     */
    SystemUptimeDto getHostSystemUptime(String host) throws Exception;

    /**
     * 获取容器的运行时间
     *
     * @param container
     * @return SystemUptimeDto
     */
    SystemUptimeDto getContainerSystemUptime(String container) throws Exception;

    /**
     * 查询容器
     *
     * @param userUniqueId
     * @param subMethod
     * @param subParameters
     * @return List<ContainerDto>
     */
    List<ContainerDto> containerQuery(String userUniqueId, String subMethod, String subParameters) throws Exception;

    /**
     * 查询 宿主上的容器信息
     *
     * @param host 宿主机序列号
     * @return List<ContainerOnHostDto>
     */
    ContainerOnHostDto getContainerOnHostDtoByHost(String host) throws Exception;

    /**
     * 查询容器列表
     */
    PageDto<ContainerDto> containerlistquery(CommonQueryParam param) throws Exception;

    void operationContainer(ContainerOpParam param) throws Exception;

    /**
     * @param id 容器Id
     * @param op 操作
     *           start：启动
     *           restart：重启
     *           stop：停止
     *           pause：暂停
     *           unpause：解除暂停
     *           delete：删除
     *           kill  : 结束进程
     */
    void operationContainer(String id, String op) throws Exception;

    List<String> selectContianerByHostId(String id);

    ContainerDto queryContainerDtoById(String id) throws Exception;


    /**
     * 创建模板镜像
     *
     * @param host              宿主机 Id
     * @param managementIp      宿主机管理Ip
     * @param application       应用Id
     * @param templateContainer 模板容器Id   @return 模板镜像
     */
    String createTemplateImage(String host, String managementIp, String application, String templateContainer) throws Exception;


    /**
     * 批量创建
     */
    List<ContainerDto> batchCreate(List<ContainerCreateByConditionParam> param)throws Exception;

    /**
     * 容器更改磁盘方案
     */
    void changeDiskScheme(ContainerChangeDiskSchemeParam param) throws Exception;

    /**
     * 容器更改计算方案
     */
    void changeCalculationScheme(ContainerChangeCalculationSchemeParam param) throws Exception;
}