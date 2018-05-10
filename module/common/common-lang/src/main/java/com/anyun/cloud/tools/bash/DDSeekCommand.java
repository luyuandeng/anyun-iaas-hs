/*
 *
 *      DDSeekCommand.java
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
 * @author TwitchGG
 * @date 2015-5-16
 * @version 1.0
 */
public class DDSeekCommand implements LinuxCommand {

    public static String IF = "/dev/zero";
    public static String BS = "1M";
    private final String of;
    private final int size;

    public DDSeekCommand(int size, String of) {
        this.of = of;
        this.size = size;
    }

    @Override
    public String exec() throws Exception {
        String ddScript = "dd if=" + IF + " of=" + of + " count=0 bs=" + BS + " seek=" + (size * 1024);
        BashCommand command = new BashCommand(ddScript);
        String result = command.exec();
        if (command.getResult().getExitValue() != 0) {
            throw new Exception(result);
        }
        return result;
    }

    public String getOf() {
        return of;
    }

    public int getSize() {
        return size;
    }
}
