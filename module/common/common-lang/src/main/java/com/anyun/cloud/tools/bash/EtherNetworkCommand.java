/*
 *
 *      EtherNetworkCommand.java
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
public class EtherNetworkCommand implements LinuxCommand {

    public static String SC_LINK_SHOW = "ip link show | grep -v \"link/\" | awk -F ' ' '{print $2}' | sed 's/://g'";

    @Override
    public String exec() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<String> getEthers() {
        BashCommand command = new BashCommand(SC_LINK_SHOW);
        String result = command.exec();
        List<String> ethers = new ArrayList<>();
        if (StringUtils.isEmpty(result)) {
            return ethers;
        }
        return StringUtils.readStringLines(result);
    }
}
