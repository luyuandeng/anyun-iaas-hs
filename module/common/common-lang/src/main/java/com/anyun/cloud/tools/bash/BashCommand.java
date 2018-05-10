/*
 *
 *      BashCommand.java
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

import java.io.ByteArrayOutputStream;
import org.buildobjects.process.ExternalProcessFailureException;
import org.buildobjects.process.ProcBuilder;
import org.buildobjects.process.ProcResult;
import org.buildobjects.process.StartupException;
import org.buildobjects.process.TimeoutException;

/**
 *
 * @author TwitchGG
 * @date 2015-4-24
 * @version 1.0
 */
public class BashCommand implements LinuxCommand {

    private final String command;
    private ProcResult result;
    private Exception exception;
    private long timeout = Long.MAX_VALUE;

    public BashCommand(String command) {
        this.command = command;
    }

    @Override
    public String exec() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            String export = "export PATH=$PATH:/bin:/sbin/:/usr/bin:/usr/sbin";
            ProcBuilder pb = new ProcBuilder("/bin/bash")
                    .withArgs("-c", export + " && " + command)
                    .withOutputStream(output)
                    .withTimeoutMillis(Long.MAX_VALUE);
            result = pb.run();
            return output.toString().trim();
        } catch (StartupException | TimeoutException | ExternalProcessFailureException e) {
            exception = e;
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getCommand() {
        return command;
    }

    public ProcResult getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}
