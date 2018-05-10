package com.anyun.esb.component.host.service.docker;

import java.util.List;
import java.util.Map;

/**
 * Created by twitchgg on 16-7-19.
 */
public interface VSwitchService {

    /**
     * 获取网络标签
     *
     * @param label
     * @param type (bridge,openvswitch)
     * @return
     * @throws Exception
     */
    String getNetworkLabel(String label, String type) throws Exception;


    /**
     * 获取所有网络标签
     *
     * @return   List<String>
     * @throws Exception
     */
    List<String>  getAllNetworkLabel() throws Exception;


    /**
     * 检查网络标签是否存在
     *
     * @param label
     * @param type (bridge,openvswitch)
     * @return
     * @throws Exception
     */
    boolean networkLabelExist(String label, String type) throws Exception;

    /**
     * 创建一个Docker网络标签
     *
     * @param label
     * @param subnet
     * @param gateway
     * @throws Exception
     */
    void createOVNNetwork(String label, String subnet, String gateway) throws Exception;

    /**
     * 删除OVN Docker网络标签
     *
     * @param label
     * @throws Exception
     */
    void deleteOVNNetwork(String label) throws Exception;

    /**
     * 将虚拟机绑定到网络标签
     *
     * @param containerId
     * @param type (openvswitch)
     * @param label
     * @return
     * @throws Exception
     */
    void connectToNetwork(String containerId, String type, String label) throws Exception;

    /**
     * 将虚拟机绑定到网络标签
     *
     * @param containerId
     * @param type (bridge)
     * @param label
     * @return
     * @throws Exception
     */
    void connectToNetwork(String containerId, String type, String label,String ip) throws Exception;

    /**
     * 获取容器IP
     *
     * @param label
     * @param containerId
     * @return
     * @throws Exception
     */
    Map<String,Object> queryContainerIpAddress(String label, String containerId) throws Exception;

    /**
     * 将虚拟机从OVN标签移除
     *
     * @param containerId
     * @param type
     * @param label
     * @throws Exception
     */
    void disconnectFromNetwork(String containerId, String type, String label) throws Exception;
}
