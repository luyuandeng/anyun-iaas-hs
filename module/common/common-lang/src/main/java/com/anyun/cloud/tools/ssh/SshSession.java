/*
 *
 *      SshSession.java
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

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;

/**
 * Created by TwitchGG on 9/25/15.
 */
public class SshSession {
    private Session session;

    public SshSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void scp(String src,String dest) throws Exception{
        Sftp sshService = new Sftp();
        sshService.put(session,src,dest);
    }

    public int exec(String command,ChannelExec channelExec) throws Exception{
        return new SshExec().exec(session, "/usr/bin/top", channelExec);
    }

    public ChannelExec getExecSessionChannle() throws Exception{
        ChannelExec channel = (ChannelExec)session.openChannel("exec");
        channel.setErrStream(System.err);
        channel.setPty(true);
        channel.setEnv("TERM", "xterm-256color");
        channel.setEnv("PATH","/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin");
        channel.setEnv("LANG","en_US.UTF-8");
        channel.setEnv("LANGUAGE","en_US");
        channel.setEnv("LC_CTYPE","utf-8");
        return channel;
    }
}
