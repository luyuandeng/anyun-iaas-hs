/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.cloud.host.startup.daemon;

import com.anyun.cloud.agent.common.*;
import com.anyun.cloud.tools.EncryptUtils;
import com.anyun.cloud.tools.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/25/16
 */
public class ZookeeperInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperInitialization.class);
    private static Inet4Address managementIfaceAddress = null;
    private static String managementIfaceMac = null;
    public static void init() throws Exception {
        String zkString = Env.env(Constant.ENV_ZOOKEEPER_CONNECT_STRING);                   //获取zookeeper服务器信息
        int timeout = Env.env(Integer.class, Constant.ENV_ZOOKEEPER_TIMEOUT);               //获取zookeeper超时时间
        ZKConnector zookeeperConnector = new ZKConnector();                                 //初始化
        Env.export(ZKConnector.class, zookeeperConnector);
        connect(zkString, timeout);                                                         //连接zookeeper服务器
        zookeeperConnector.createIfNodeNotExist(Constant.ZK_PATH_HOST, null, CreateMode.PERSISTENT);  //创建永久节点
        String managamentIface = Env.env(Constant.ENV_NET_INTERFACE_MGR);                   //获取相关信息
        getManagementAddress(managamentIface);                                              //返回网卡的IP地址   demo: /172.17.0.1
        String managementIp = managementIfaceAddress.getHostAddress();                      //文本化IP地址       demo:  172.17.0.1
        LOGGER.debug("Management network interface address [{}]", managementIp);
        String hostPath = Constant.ZK_PATH_HOST + "/" + managementIfaceAddress.getHostAddress();
        StringBuilder dataBuilder = new StringBuilder(Env.env(Constant.ENV_SERIAL_NUMBER));
        dataBuilder.append(":").append(managementIp);
        LOGGER.debug("Host info data [{}]",dataBuilder.toString());
        zookeeperConnector.createIfNodeNotExist(hostPath, dataBuilder.toString(), CreateMode.EPHEMERAL);  //创建zookeeper节点
    }

    /**
     * 获取实际物理网卡的IP地址
     * @param iface
     * @throws Exception
     */
    private static void getManagementAddress(String iface) throws Exception {
        Enumeration<NetworkInterface> nets = null;
        nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            LOGGER.debug("Find iface name [{}]",netint.getDisplayName());
            if (!netint.getDisplayName().equals(iface))
                continue;
            managementIfaceMac = Utils.getMacAddress(netint.getHardwareAddress());
            for (InterfaceAddress address : netint.getInterfaceAddresses()) {
                if (address.getAddress().isLoopbackAddress())                   //检查地址是否为回环地址
                    continue;
                if (!(address.getAddress() instanceof Inet4Address))
                    continue;
                managementIfaceAddress = (Inet4Address) address.getAddress();
                return;
            }
        }
        throw new Exception("not find iface [" + iface + "]");
    }

    /**
     * 链接zookeeper
     * @param zkString
     * @param timeout
     * @throws Exception
     */
    private static void connect(String zkString, int timeout) throws Exception {
        ZKConnector zkConnector = Env.env(ZKConnector.class);
        zkConnector.setConnectLostListener(new ZkConnectLostListener() {        //添加监听事件
            @Override
            public void afterConnectLost(ZKConnector connector) {               //如果zookeeper丢失，则自动触发监听，尝试重新链接
                try {
                    connect(zkString, timeout);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        zkConnector.connect(zkString, timeout);                                 //链接zookeeper服务器
        LOGGER.info("Anyun cloud scheduling server [{}] connected", zkString);
    }

//    public static void main(String[] args) throws Exception {
//        getManagementAddress("lo");
//        System.out.println(managementIfaceAddress.getHostAddress());
//    }
}
