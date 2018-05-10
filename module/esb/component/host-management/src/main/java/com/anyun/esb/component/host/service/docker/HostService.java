package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostTailDto;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.SSHFileCopy;

import java.util.List;

/**
 * Created by zy on 17-3-28.
 */
public interface HostService {
    /**
     * 创建宿主机
     * @param param
     * @throws Exception
     */
    HostTailDto createHost(HostCreateParam param) throws Exception;

    /**
     * 添加DNS域名服务器
     * @param hostSshClient
     * @throws Exception
     */
    void hostAddDns(HostSshClient hostSshClient,String nameServer) throws Exception;

    /**
     * 创建ovs网桥
     * @param hostSshClient
     * @param newHostSshClient
     * @throws Exception
     */
    void hostBuildInternetBridge(HostSshClient hostSshClient,HostSshClient newHostSshClient,String hostIp) throws Exception;

    /**
     * docker加入zk集群
     * @param targetClient
     * @param copy
     * @throws Exception
     */
    void hostDockerAddZookeeper(HostSshClient targetClient, SSHFileCopy copy) throws Exception;

    /**
     * 初始化ovs集群
     * @param hostSshClient
     * @param hostip
     * @throws Exception
     */
    void hostInitializeOvsCluster(HostSshClient hostSshClient,String hostip) throws Exception;

    /**
     * 初始化存储
     * @param hostSshClient
     * @throws Exception
     */
    void hostInitializeStorage(HostSshClient hostSshClient) throws Exception;

    /**
     * 修改host状态启用或禁用
     * @param param
     * @throws Exception
     */
    void updateHostStatus(HostStatusUpdateParam param) throws Exception;

    /**
     * 根据ip查询宿主机详情
     * @param ip
     * @return
     * @throws Exception
     */
    HostTailDto selectHostInfoByDescription(String ip) throws Exception;

    /**
     * 删除宿主机
     * @param id
     * @throws Exception
     */
    void deleteHost(String id,String ip) throws Exception;

    List<HostBaseInfoDto> queryHostByCluster(String id)throws Exception;

}
