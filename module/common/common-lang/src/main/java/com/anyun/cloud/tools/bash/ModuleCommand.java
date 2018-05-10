/*
 *
 *      ModuleCommand.java
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

package com.anyun.cloud.tools.bash;

import com.anyun.cloud.tools.StringUtils;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 8/31/15
 */
public class ModuleCommand implements LinuxCommand {
    @Override
    public String exec() throws Exception {
        throw new UnsupportedOperationException();
    }

    public boolean lsmod(String name) {
        String sc = "/sbin/lsmod | grep ^" + name + "\\ ";
        BashCommand bashCommand = new BashCommand(sc);
        String result = bashCommand.exec();
        return StringUtils.isNotEmpty(result);
    }

    public String findModule(String name) {
        String sc = "find /lib/modules/`uname -r` -type f -name " + name;
        BashCommand bashCommand = new BashCommand(sc);
        String result = bashCommand.exec();
        if(StringUtils.isEmpty(result))
            return null;
        return result.trim();
    }

    public void depmod() {
        new BashCommand("/sbin/depmod -a").exec();
    }

    public void modprobe(String name) throws Exception{
        BashCommand command = new BashCommand("/sbin/modprobe " + name);
        command.exec();
        if(command.getException() != null)
            throw command.getException();
    }
}
