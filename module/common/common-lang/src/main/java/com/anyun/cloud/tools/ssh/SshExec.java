/*
 *
 *      SshExec.java
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
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by TwitchGG on 9/25/15.
 */
public class SshExec {
    public int exec(Session session, String command, ChannelExec channel) throws Exception {
        int exitStatus = 0;
        if (channel == null) {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setErrStream(System.err);
        }
        channel.setCommand(command);
        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                exitStatus = channel.getExitStatus();
                System.out.println("exit-status: " + channel.getExitStatus());
                break;
            }
        }
        channel.disconnect();
        return exitStatus;
    }

    public ExecResult execResult(Session session, String command, ChannelExec channel) throws JSchException, IOException {
        StringBuilder sb = new StringBuilder();
        int exitStatus = 0;
        if (channel == null) {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setErrStream(System.err);
        }
        channel.setCommand(command);
        InputStream in = channel.getInputStream();
        channel.connect();
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                sb.append(new String(tmp, 0, i));
            }
            if (channel.isClosed()) {
                if (in.available() > 0) continue;
                exitStatus = channel.getExitStatus();
                break;
            }
        }
        channel.disconnect();
        ExecResult execResult = new ExecResult();
        execResult.setExitCode(exitStatus);
        execResult.setResult(sb.toString());
        return execResult;
    }

    public static class ExecResult {
        private int exitCode;
        private String result;

        public int getExitCode() {
            return exitCode;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
