/*
 *
 *      JGroupReceiver.java
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

package com.anyun.cloud.tools.broadcast;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.execption.ReceiverStartFailedException;
import org.jgroups.JChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 9/30/15
 */
public class JGroupReceiver implements Receiver {

    private static Logger LOGGER = LoggerFactory.getLogger(JGroupReceiver.class);
    private JChannel channel;
    private String clusterName;
    private URL configFile;

    public JGroupReceiver() {
    }

    public JGroupReceiver(String cluserName, URL configFile) {
        this.clusterName = cluserName;
        this.configFile = configFile;
    }

    @Override
    public void start() throws ReceiverStartFailedException {
        if (StringUtils.isEmpty(clusterName)) {
            LOGGER.error("cluster name is not set. code '{}'", 0x00000001);
            throw new ReceiverStartFailedException(0x00000001, "cluster name is not set");
        }
        if (configFile == null) {
            LOGGER.error("jgroup configure file is not set. code '{}'", 0x00000002);
            throw new ReceiverStartFailedException(0x00000002, "jgroup configure file is not set");
        }
        try {
            channel = new JChannel(configFile);
            LOGGER.debug("Set jgroup configure file to '{}'", configFile.toString());
            channel.setReceiver(new JsonReceiverHandler());
            LOGGER.debug("Set jgroup receiver handler to '{}'", JsonReceiverHandler.class.getName());
            channel.connect(clusterName);
            LOGGER.info("jgroup cluster '{}' connected", clusterName);
        } catch (Exception ex) {
            LOGGER.error("jgroup start error: '{}'. code '{}'", ex.getMessage(), 0x00000003);
            throw new ReceiverStartFailedException(0x00000003, "jgroup start error: " + ex.getMessage());
        }
    }

    public JChannel getChannel() {
        return channel;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public URL getConfigFile() {
        return configFile;
    }

    public void setConfigFile(URL configFile) {
        this.configFile = configFile;
    }
}