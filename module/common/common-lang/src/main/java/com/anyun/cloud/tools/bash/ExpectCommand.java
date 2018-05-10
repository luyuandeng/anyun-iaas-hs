/*
 *
 *      ExpectCommand.java
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

import com.anyun.cloud.tools.log.Log;
import java.io.ByteArrayOutputStream;
import org.buildobjects.process.ProcBuilder;
import org.buildobjects.process.ProcResult;

/**
 *
 * @author TwitchGG
 * @date 2015-5-1
 * @version 1.0
 */
public class ExpectCommand implements LinuxCommand {

    private final String path;
    private ProcResult result;
    private final String[] args;

    public ExpectCommand(String path, String args[]) {
        this.path = path;
        this.args = args;
    }

    @Override
    public String exec() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        StringBuilder opts = new StringBuilder();
        for (String arg : args) {
            opts.append(" ").append(arg);
        }
        Log.getLog().log("EXEPECT: " + path + " " + opts.toString().trim());
        result = new ProcBuilder("/usr/bin/expect")
                .withArgs("-f",path,opts.toString().trim())
                .withOutputStream(output)
                .withTimeoutMillis(Long.MAX_VALUE)
                .run();
        return output.toString();
    }

    public String getPath() {
        return path;
    }

    public ProcResult getResult() {
        return result;
    }

    public String[] getArgs() {
        return args;
    }
}
