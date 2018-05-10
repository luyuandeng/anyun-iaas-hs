/*
 *
 *      ConfigFactory.java
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

package com.anyun.cloud.tools.config;

import java.util.HashMap;
import java.util.Map;

/**
 * WEB爬虫通用配置工厂<br>
 * 包含了数据库配置
 * 
 * @date 2015/1/28
 * @author TwitchGG
 * @version 1.0
 */
public class ConfigFactory {
    private static ConfigFactory factory;
    private final Map<String,ConfigEntry> configsMap;
    private ConfigFactory() {
        configsMap = new HashMap<>();
    }
    
    public static ConfigFactory getFactory() {
        synchronized(ConfigFactory.class) {
            if(factory == null)
                factory = new ConfigFactory();
        }
        return factory;
    }
    
    public void addConfig(String name,ConfigEntry config) {
        configsMap.put(name, config);
    }
    
    public ConfigEntry getConfig(String name) {
        return configsMap.get(name);
    }
}
