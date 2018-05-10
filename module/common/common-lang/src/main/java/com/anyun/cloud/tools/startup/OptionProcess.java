/*
 *
 *      OptionProcess.java
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ray
 */
public class OptionProcess {
    private Map<String,String> options;
    private LinkedList<String> optList;
    private final String[] args;
    public OptionProcess(String[] args) {
        this.args = args;
    }
    
    public Map<String,String> getOptions() throws BootParamsException {
        options = new HashMap();
        optList = new LinkedList<>();
        if(args == null || args.length == 0)
            return options;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if(arg.substring(0, 1).equals("-") && !arg.substring(0, 2).equals("--")) {
                String key = arg;
                String value = args[i+1];
                if(value.startsWith("--")) {
                    throw new BootParamsException("参数 " + key.substring(1) + "没有设置值");
                } else {
                    options.put(key.substring(1), value);
                    i++;
                }
            } else if(arg.startsWith("--")) {
                if(!arg.contains("="))
                    throw new BootParamsException("参数 " + arg + "没有设置值");
                else {
                    String key = arg.substring(2, arg.indexOf('='));
                    String value = arg.substring(arg.indexOf('=') + 1);
                    options.put(key,value);
                }
            } else {
                optList.add(arg);
            }
        }
        return options;
    }

    public List<String> getOptList() {
        return optList;
    }
}
