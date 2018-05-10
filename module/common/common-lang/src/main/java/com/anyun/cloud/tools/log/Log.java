/*
 *
 *      Log.java
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

package com.anyun.cloud.tools.log;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author TwitchGG
 * @date 2015-5-11
 * @version 1.0
 */
public class Log {
    public static String LOG_FILE = "/var/log/cloud_host.log";
    private static Log log;
    private BufferedWriter writer;
    public Log() {
        try {
            writer = new BufferedWriter(new FileWriter(LOG_FILE,true));
        } catch (Exception e) {
        }
    }
    
    public static Log getLog() {
        synchronized(Log.class) {
            if(log == null)
                log = new Log();
        }
        return log;
    }
    
    public void log(String content) {
        try {
            writer.write(content + "\n");
        } catch (Exception e) {
        }
    }
}
