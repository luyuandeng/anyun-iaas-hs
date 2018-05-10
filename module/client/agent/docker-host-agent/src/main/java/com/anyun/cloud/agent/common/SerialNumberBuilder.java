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

package com.anyun.cloud.agent.common;

import com.anyun.cloud.tools.EncryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/23/16
 */
public class SerialNumberBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialNumberBuilder.class);

    public String build() throws Exception {
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> nets = null;
        nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets)) {
            if (netint.isLoopback())
                continue;
            LOGGER.debug("Interface Name [" + netint.getDisplayName() + "] is virtual [{}],is point to point [{}]"
                    ,netint.isVirtual(),netint.isPointToPoint());
            LOGGER.debug("Interface Parent [{}]",netint.getParent() == null ? "No parent" : netint.getParent().getDisplayName());
            byte[] hadrwareAddress = null;
            hadrwareAddress = netint.getHardwareAddress();
            if(hadrwareAddress == null)
                continue;
            String mac = getMacAddress(hadrwareAddress);
            sb.append(",").append(mac);
            LOGGER.debug("Ethernet card hardware address [{}]\n============================", mac);
        }

        return EncryptUtils.getMD5ofStr(sb.toString()).toLowerCase();
    }

    private String getMacAddress(byte[] mac) {
        if (mac == null)
            return "";
        StringBuilder sb = new StringBuilder(18);
        for (byte b : mac) {
            if (sb.length() > 0)
                sb.append(':');
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
