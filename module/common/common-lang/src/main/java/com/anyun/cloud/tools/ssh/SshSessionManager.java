/*
 *
 *      SshSessionManager.java
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

package com.anyun.cloud.tools.ssh;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by TwitchGG on 9/25/15.
 */
public class SshSessionManager {

    private static SshSessionManager manager;

    private JSch jsch;
    private String privateKey;

    private SshSessionManager() {
        jsch = new JSch();
    }

    public static SshSessionManager getManager() {
        synchronized (SshSessionManager.class) {
            if (manager == null)
                manager = new SshSessionManager();
        }
        return manager;
    }

    public SshSession getSession(String host, int port, String userName) throws Exception {
        Session session = jsch.getSession(userName, host, port);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(5000);
        SshSession sshSession = new SshSession(session);
        return sshSession;
    }

    public void addKey(String privateKey) throws Exception {
        if(this.privateKey == null) {
            this.privateKey = privateKey;
            jsch.addIdentity(privateKey);
        }
    }
}
