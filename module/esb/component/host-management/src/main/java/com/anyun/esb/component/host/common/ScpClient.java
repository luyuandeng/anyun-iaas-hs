/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.anyun.esb.component.host.common;

import com.jcraft.jsch.*;

import java.io.*;

/**
 * @author TwitchGG <ray@proxzone.com>
 * @since 1.0.0 on 9/8/16
 */
public class ScpClient {
    private Session session;

    //文件上传(本地 copy 到 远程 )
    public void scp(InputStream inputStream, String dest) throws JSchException, FileNotFoundException, SftpException {
        ChannelSftp channel = getChannel();
        channel.connect();
        channel.put(inputStream, dest);
        channel.disconnect();
    }

    public void scp(File src, String dest) throws JSchException, FileNotFoundException, SftpException {
        scp(new FileInputStream(src), dest);
    }

    public void scp(String src, String dest) throws JSchException, FileNotFoundException, SftpException {
        scp(new FileInputStream(src), dest);
    }


    //文件下载(远程 copy 到 本地 )
    public void scp(OutputStream outputStream, String src) throws JSchException, FileNotFoundException, SftpException {
        ChannelSftp channel = getChannel();
        channel.connect();
        channel.get(src, outputStream);
        channel.disconnect();
    }

    public void load(File src, String dest) throws JSchException, FileNotFoundException, SftpException {
        scp(new FileOutputStream(src), dest);
    }

    public void load(String src, String dest) throws JSchException, FileNotFoundException, SftpException {
        scp(new FileOutputStream(src), dest);
    }

    private ChannelSftp getChannel() throws JSchException {
        Channel channel = session.openChannel("sftp");
        ChannelSftp channelSftp = (ChannelSftp) channel;
        return channelSftp;
    }

    public ScpClient(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }
}
