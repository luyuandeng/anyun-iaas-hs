/*
 *
 *      DaemonStartup.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.anyun.cloud.host.startup;

import com.anyun.cloud.host.startup.daemon.EnvironmentVariableInitialization;
import com.anyun.cloud.host.startup.daemon.JettyRestServer;
import com.anyun.cloud.host.startup.daemon.RestartBridgeServer;
import com.anyun.cloud.host.startup.daemon.ZookeeperInitialization;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.startup.StartupOption;
import org.apache.commons.lang.SystemUtils;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author ray
 */
public class DaemonStartup implements StartupClass {
    private static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DaemonStartup.class);
    private final CountDownLatch connectedSignal = new CountDownLatch(1);
    @Override
    public void startup() throws Exception {
        System.setProperty("java.net.preferIPv4Stack", "true");
        if (SystemUtils.IS_OS_WINDOWS) {
            LOGGER.warn("System is '{}',many features are disable", "Microsoft Windows");
        }

        /**
         * 获取启动信息中对应的数据，start or stop；
         */
        String daemonOperate = StartupOption.getOption().getValue("daemon.type");
        if (StringUtils.isEmpty(daemonOperate)) {
            return;
        }
        switch (daemonOperate) {
            case "start":
                EnvironmentVariableInitialization.init();  //初始化配置信息
                RestartBridgeServer.restartBridge();       //初始化OVS
                ZookeeperInitialization.init();            //初始化zookeeper
                new JettyRestServer().start();             //创建jetty容器
                connectedSignal.await();                   //阻塞线程，直到主线程运行结束，才开始执行其他线程
                break;
            case "stop":
                break;
        }
    }
}
