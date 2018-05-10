/*
 *
 *      NetWorkShowCommand.java
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anyun.cloud.tools.bash;

import com.anyun.cloud.tools.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ray
 */
public class NetWorkShowCommand implements LinuxCommand {

    public static String SC_SHOW = "ip link | grep -v 'link/' | awk -F ':' '{print $2}' | sed 's/\\ //g' | grep -v '-' | grep -v '^lo$'";

    @Override
    public String exec() throws Exception {
        throw new UnsupportedOperationException();
    }

    public List<String> getNetworks() {
        List<String> networks = new ArrayList<>();
        BashCommand bc = new BashCommand(SC_SHOW);
        String result = bc.exec();
        if (bc.getException() != null || StringUtils.isEmpty(result)) {
            return networks;
        }
        return StringUtils.readStringLines(result);
    }

    public String getEtherIpv4Address(String name) {
        String sc = "ip addr | grep " + name + " | grep inet | awk -F ' ' '{print $2}'";
        BashCommand bc = new BashCommand(sc);
        String result = bc.exec();
        if (bc.getException() != null || StringUtils.isEmpty(result)) {
            return null;
        }
        return result.trim();
    }

    public String getNetworkInfo(String name) {
        String sc = "ip addr show dev " + name + " | grep -v '" + name + ":' | grep -v 'valid_lft' | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//'";
        BashCommand bc = new BashCommand(sc);
        String result = bc.exec();
        if (bc.getException() != null) {
            return null;
        }
        return result;
    }

    public String getRtx(String ether) {
        String script = "cat /proc/net/dev | grep '^" + ether +":' | sed 's/://g' | awk -F ' ' '{print $1,$2,$10}'";
        BashCommand bc = new BashCommand(script);
        String result = bc.exec();
        if(StringUtils.isEmpty(result))
            return "";
        return result;
    }
    
    public int getSpeed(String ether) {
        String script = "/sbin/ethtool " + ether + " | grep \"Speed:\" |awk -F ':' '{print $2}' | sed 's/^[ \\t]*//g' |sed 's/ \\+/ /g'";
        BashCommand cmd = new BashCommand(script);
        try {
            String result = cmd.exec().trim();
            if(cmd.getException() != null)
                return 0;
            return Integer.valueOf(result.replace("Mb/s", ""));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public boolean exist(String dev) {
        for (String name : getNetworks()) {
            if (dev.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
