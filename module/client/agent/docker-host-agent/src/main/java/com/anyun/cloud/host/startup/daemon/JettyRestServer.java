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

import com.anyun.cloud.agent.api.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/17/16
 */
public class JettyRestServer {
    public void start() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api/v1.0");                //访问路径
        Server jettyServer = new Server(8080);         //访问端口
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", getProviderNames());
        jettyServer.start();
        jettyServer.join();
    }

    private String getProviderNames() {
        StringBuilder sb = new StringBuilder();
        sb.append(StorageApi.class.getCanonicalName()).append("\n");
        sb.append(VolumeApi.class.getCanonicalName()).append("\n");
        sb.append(OvsApi.class.getCanonicalName()).append("\n");
        return sb.toString();
    }
}
