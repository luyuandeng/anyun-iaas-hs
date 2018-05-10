/*
 *
 *      Sftp.java
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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

/**
 * Created by TwitchGG on 9/25/15.
 */
public class Sftp {
    private static Logger LOGGER = LoggerFactory.getLogger(Sftp.class);

    public void put(Session session, String src, String dest) throws Exception {
        if(!session.isConnected())
            session.connect(5000);
        Channel channel = session.openChannel("sftp");
        channel.connect(5000);
        ChannelSftp channelSftp = (ChannelSftp) channel;
        LOGGER.debug("Starting file upload.FROM [{}] TO [{}]", src, dest);
        channelSftp.put(new FileInputStream(src), dest);
        LOGGER.debug("File [{}] Upload complated", src);
        channelSftp.disconnect();
    }
}

