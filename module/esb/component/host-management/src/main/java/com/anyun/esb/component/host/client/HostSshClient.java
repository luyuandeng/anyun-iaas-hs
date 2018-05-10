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

package com.anyun.esb.component.host.client;

import com.anyun.cloud.tools.ssh.SshExec;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class HostSshClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostSshClient.class);
    private String host;
    private int port = 22;
    private JSch jsch;
    private Session session;
    private String user = "root";


    /**
     *  ssh 用户名密码  登录
     * @param  USER  用户名
     * @param  HOST  主机
     * @param  DEFAULT_SSH_PORT ssh端口
     * @@param PASSWORD  ssh 密码
     */
    public HostSshClient(String USER,String HOST,int  DEFAULT_SSH_PORT,String PASSWORD) throws Exception {
        jsch=new JSch();
        session = jsch.getSession(USER,HOST,DEFAULT_SSH_PORT);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(5000);
    }


    public HostSshClient(String key) throws Exception {
        jsch = new JSch();
        jsch.addIdentity(key);
    }

    public void connect() throws Exception {
        session = jsch.getSession(user, host, port);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(5000);
    }

    public String exec(String command) throws Exception {
        ChannelExec channel = null;
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setErrStream(System.err);
            channel.setPty(true);
            channel.setEnv("TERM", "xterm-256color");
            channel.setEnv("PATH", "/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
            channel.setEnv("LANG", "en_US.UTF-8");
            channel.setEnv("LANGUAGE", "en_US");
            channel.setEnv("LC_CTYPE", "utf-8");
            SshExec.ExecResult execResult = new SshExec().execResult(session, command, channel);
            String result = execResult.getResult().trim();
            LOGGER.debug("RESULT [{}]", result);
            if (execResult.getExitCode() != 0) {
                throw new Exception(result);
            }
            return result;
        } catch (Exception ex) {
            throw ex;
        } finally {
            channel.disconnect();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Session getSession() throws Exception {
        return session;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
