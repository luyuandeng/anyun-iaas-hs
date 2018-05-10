/*
 *
 *      IfStatCommand.java
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
import org.buildobjects.process.ProcBuilder;

/**
 *
 * @author TwitchGG
 * @date 2015-4-24
 * @version 1.0
 */
public class IfStatCommand implements LinuxCommand {
    private final int count;
    private final int delay;
    public IfStatCommand(int delay,int count) {
        this.delay = delay;
        this.count = count;
    }
    
    @Override
    public String exec() {
       ByteArrayOutputStream output = new ByteArrayOutputStream();
        new ProcBuilder("bash")
        .withArgs("-c","ifstat " + delay +" " + count)
        .withOutputStream(System.out)
        .run();
        return output.toString();
    }
}
