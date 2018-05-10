package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ConnectToNetParam;
import com.anyun.cloud.param.DisconnectFromNetParam;
import com.anyun.cloud.param.NetLabelInfoCreateParam;

import java.util.List;

/**
 * Created by hwt on 16-7-22.
 */
public interface NetService {

    /**
     * 容器连上到网络
     *
     * @param param
     */
    void containerConnectToNetwork(ConnectToNetParam param) throws Exception;

    /**
     * 从容器上断掉网络
     *
     * @param param
     */
    void containerDisconnectFromNetwork(DisconnectFromNetParam param) throws Exception;


    /**
     * 查询所有bridge类型网络标签
     */
    List<NetLabelInfoDto> netLabelInfoQueryAllBridge() throws Exception;


    /**
     * 删除网络标签(根据 label)
     *
     * @param label
     */
    void netLabelInfoDeleteByLabel(String label) throws Exception;

    /**
     * 创建一个项目的网络标签(根据 label)
     *
     * @param param
     */
    void insertNetLabelInfo(NetLabelInfoCreateParam param) throws Exception;

    /**
     * 查询 一条网络标签信息
     *
     * @param label
     * @return NetLabelInfoDto
     */
    NetLabelInfoDto netLabelInfoQueryByLabel(String label) throws Exception;

    /**
     * 根据容器查询网络标签
     *
     * @param container
     * @return List<NetLabelInfoDto>
     */
    List<NetLabelInfoDto> netLabelInfoQueryByContainer(String container) throws Exception;

    ;

    /**
     * 根据项目查询网络标签
     *
     * @param project
     * @return NetLabelInfoDto
     */
    NetLabelInfoDto netLabelInfoQueryProject(String project) throws Exception;


    /**
     * 根据项目 删除 网络标签
     *
     * @param project
     */
    void netLabelInfoDeleteByProject(String project) throws Exception;


    /**
     * 根据条件查询容器 ip 信息
     *
     * @param label
     * @param container
     */
    List<ContainerIpInfoDto> queryContainerIpByCondition(String label, String container) throws Exception;


    /**
     * 根据关键字 查询容器 ip Mac等
     *
     * @param keyword
     * @return List<ContainerNetIpMacDto>
     */
    List<ContainerNetIpMacDto> queryContainerNetIpMacByKeyword(String keyword) throws Exception;


    /**
     * 根据 安全组 查询容器网络信息
     *
     * @param securityGroup
     * @return List<ContainerIpInfoDto>
     */
    List<ContainerIpInfoDto> containerIpInfoQueryBySecurityGroup(String securityGroup) throws Exception;


    /**
     * 根据 安全组  查询  为添加到安全组的容器网络信息
     *
     * @param securityGroup
     * @return List<ContainerIpInfoDto>
     */
    List<ContainerIpInfoDto> containerIpInfoQueryNotAddBySecurityGroup(String securityGroup) throws Exception;

    /**
     * 根据条件查询   ContainerIpInfoDto
     *
     * @param container
     * @param netLabel
     * @return ContainerIpInfoDto
     */
    ContainerIpInfoDto queryContainerIpInfoByCondition(String container, String netLabel) throws Exception;

    /**
     * 查询所有可用briage标签的IP
     *
     * @param label
     * @return
     * @throws Exception
     */
    List<IpDto> queryAvailabelIpByLabel(String label) throws Exception;


    /**
     * 查询 网络标签 有类型
     *
     * @param  type
     * @return List<NetLabelInfoDto>
     */
    List<NetLabelInfoDto> netLabelQueryByType(String type) throws Exception;


    /**
     * 查询 网络标签列表
     *
     * @param  subParameters
     * @parm   subMethod
     * @return List<NetLabelInfoDto>
     * @throws Exception
     */
    List<NetLabelInfoDto> netLabelQuery(String subMethod, String subParameters) throws Exception;

    /**
     * 查询 容器网络列表
     *
     * @param  subParameters
     * @parm   subMethod
     * @return List<NetLabelInfoDto>
     * @throws Exception
     */
    List<ContainerIpInfoDto> containerIpQuery(String subMethod, String subParameters) throws Exception;

    /**
     * 查询容器ip列表
     *
     */
    PageDto<ContainerIpInfoDto> containeriplistquery(CommonQueryParam param) throws Exception;


    /**
     * 查询网络标签信息列表
     *
     */
    PageDto<NetLabelInfoDto> NetLabelInfoQuery(CommonQueryParam param) throws Exception;

    ContainerIpInfoDto queryContainerIpDtoByContainerAndNetLabel(String containerId, String label)throws Exception;
}
