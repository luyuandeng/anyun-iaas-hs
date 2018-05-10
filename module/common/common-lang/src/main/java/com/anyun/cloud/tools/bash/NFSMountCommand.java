/*
 *
 *      NFSMountCommand.java
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

/**
 *
 * @author ray
 */
public class NFSMountCommand implements LinuxCommand {

    private final String ip;
    private final String src;
    private final String dest;

    public NFSMountCommand(String ip, String src, String dest) {
        this.ip = ip;
        this.src = src;
        this.dest = dest;
    }

    @Override
    public String exec() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("mount -t nfs ").append(ip).append(":").append(src).append(" ").append(dest);
        BashCommand command = new BashCommand(sb.toString());
        command.setTimeout(30 * 1000);
        String result = command.exec();
        if (command.getException() != null) {
            throw command.getException();
        }
        if (command.getResult().getExitValue() != 0) {
            throw new Exception(result);
        }
        return "success";
    }
}
