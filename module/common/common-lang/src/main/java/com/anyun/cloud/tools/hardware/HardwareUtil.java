/*
 *
 *      HardwareUtil.java
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

package com.anyun.cloud.tools.hardware;

import com.anyun.cloud.tools.EncryptUtils;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;
import com.anyun.cloud.tools.hardware.impl.LshwCpuInfoParser;
import com.anyun.cloud.tools.hardware.impl.LshwEthernetCardInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by TwitchGG on 9/23/15.
 */
public class HardwareUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareUtil.class);

    public static String getSerial(List<CpuInfoEntity> cpuInfoEntities, List<EthernetCardInfoEntity> ethernetCardInfoEntities) {
        Collections.sort(cpuInfoEntities, new Comparator<CpuInfoEntity>() {
            @Override
            public int compare(CpuInfoEntity o1, CpuInfoEntity o2) {
                int level1 = Integer.valueOf(StringUtils.getSplitValues(o1.getBusinfo(), "@")[1]);
                int level2 = Integer.valueOf(StringUtils.getSplitValues(o2.getBusinfo(), "@")[1]);
                return level1 - level2;
            }
        });
        Collections.sort(ethernetCardInfoEntities, new Comparator<EthernetCardInfoEntity>() {
            @Override
            public int compare(EthernetCardInfoEntity o1, EthernetCardInfoEntity o2) {
                String bus1_num = StringUtils.getSplitValues(o1.getBusinfo(), "@")[1];
                String bus2_num = StringUtils.getSplitValues(o2.getBusinfo(), "@")[1];
                int bus1_0 = Integer.valueOf(StringUtils.getSplitValues(bus1_num, ":")[0]);
                int bus2_0 = Integer.valueOf(StringUtils.getSplitValues(bus2_num, ":")[0]);
                if (bus1_0 != bus2_0) {
                    return bus1_0 - bus2_0;
                }
                int bus1_1 = Integer.valueOf(StringUtils.getSplitValues(bus1_num, ":")[1]);
                int bus2_1 = Integer.valueOf(StringUtils.getSplitValues(bus2_num, ":")[1]);
                if (bus1_1 != bus2_1) {
                    return bus1_1 - bus2_1;
                }
                float bus1_2 = 0.0F;
                try {
                    bus1_2 = Float.valueOf(StringUtils.getSplitValues(bus1_num, ":")[2]);
                } catch (Exception ex) {
                    bus1_2 = 0.0F;
                }

                float bus2_2 = 0.0F;
                try {
                    bus2_2 = Float.valueOf(StringUtils.getSplitValues(bus2_num, ":")[2]);
                } catch (Exception ex) {
                    bus2_2 = 0.0F;
                }
                return (int) (bus1_2 - bus2_2);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (CpuInfoEntity cpuInfoEntity : cpuInfoEntities) {
            sb.append(cpuInfoEntity.asJson());
        }
        for (EthernetCardInfoEntity ethernetCardInfoEntity : ethernetCardInfoEntities) {
            sb.append(ethernetCardInfoEntity.asJson());
        }
        return EncryptUtils.getMD5ofStr(sb.toString().trim());
    }

    public static String getSerial() {
        LshwCpuInfoParser cpu = new LshwCpuInfoParser();
        LshwEthernetCardInfoParser ether = new LshwEthernetCardInfoParser();
        return getSerial(cpu.getHardwareInfoEntity(), ether.getHardwareInfoEntity());
    }

    public static String buildSerialNumber() throws Exception {
        File file = new File("/sys/class/net/");
        StringBuilder sb = new StringBuilder();
        for (File iface : file.listFiles()) {
            if (iface.getName().contains(":"))
                continue;
            String command = "ethtool -i " + iface.getName() + " | grep bus-info:";
            BashCommand linuxCommand = new BashCommand(command);
            String result = linuxCommand.exec().trim();
            if (linuxCommand.getException() != null)
                continue;
            if (linuxCommand.getResult().getExitValue() != 0)
                continue;
            String[] netInfo = StringUtils.getSplitValues(result, ":");
            if (netInfo.length < 2)
                continue;
            if (StringUtils.isEmpty(netInfo[1]) || netInfo[1].trim().contains("N/A"))
                continue;
            command = "ip link show " + iface.getName() + " | grep link | awk -F ' ' '{print $2}'";
            linuxCommand = new BashCommand(command);
            result = linuxCommand.exec().trim();
            if (linuxCommand.getException() != null)
                continue;
            if (linuxCommand.getResult().getExitValue() != 0)
                continue;
            if (result.equals("00:00:00:00:00:00"))
                continue;
            sb.append(result);
            LOGGER.debug("Interface [{}] Mac [{}]", iface.getName(), result);
        }
        return EncryptUtils.getMD5ofStr(sb.toString());
    }
}
