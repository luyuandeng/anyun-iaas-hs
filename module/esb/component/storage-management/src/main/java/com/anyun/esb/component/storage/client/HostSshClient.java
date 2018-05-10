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

package com.anyun.esb.component.storage.client;

import com.anyun.cloud.tools.ssh.SshExec;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class HostSshClient {
    private String host;
    private int port = 22;
    private JSch jsch;
    private Session session;
    private String user = "root";

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
            if (execResult.getExitCode() != 0) {
                channel.disconnect();
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
