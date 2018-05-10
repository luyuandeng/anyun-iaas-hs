/*
 *
 *      StartupOption.java
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
package com.anyun.cloud.tools.startup;

import com.anyun.cloud.tools.execption.BootParamsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public class StartupOption {

    public static String WORK_DIR = "work.dir";
    public static String SERVER_BIND = "server.bind";
    public static String VOLUME_IMAGE = "volume.image";
    public static String NET_BR_VM = "net.brigade.vm";
    public static String NET_QOS_VM = "net.qos.vm";
    public static String STARTUP_TYPE = "startup.type";
    
    private static StartupOption option;
    private OptionProcess process;
    private Map<String, String> options;

    private StartupOption() {
        options = new HashMap();
    }

    public static StartupOption getOption() {
        synchronized (StartupOption.class) {
            if (option == null) {
                option = new StartupOption();
            }
        }
        return option;
    }

    public void init(String[] args) throws BootParamsException {
        process = new OptionProcess(args);
        options = process.getOptions();
    }

    public String getValue(String name) {
        return process.getOptions().get(name);
    }

    public boolean exist(String opt) {
        for (String _arg : process.getOptList()) {
            if(opt.equals(_arg))
                return true;
        }
        return false;
    }

    public List<String> getOpts() {
        return process.getOptList();
    }
    public void printArgs() {
        for (Map.Entry<String, String> arg : options.entrySet()) {
            System.out.println("Param:" + arg.getKey() + "    Value:" + arg.getValue());
        }
        for (String _arg : process.getOptList()) {
            System.out.println("Option:" + _arg);
        }
    }
}
