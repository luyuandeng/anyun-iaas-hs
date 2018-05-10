/*
 *
 *      MakeDirCommand.java
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
public class MakeDirCommand implements LinuxCommand {

    private String path;

    public MakeDirCommand() {

    }

    public MakeDirCommand(String path) {
        this.path = path;
    }

    @Override
    public String exec() throws Exception {
        String script = "mkdir -p " + path;
        BashCommand command = new BashCommand(script);
        String result = command.exec();
        if (command.getException() != null) {
            throw command.getException();
        }
        return result;
    }

    public void mkdir(String pathName) {
        String script = "mkdir -p " + pathName;
        BashCommand command = new BashCommand(script);
        command.exec();
        this.path = pathName;
    }

    public String getPath() {
        return path;
    }
}
