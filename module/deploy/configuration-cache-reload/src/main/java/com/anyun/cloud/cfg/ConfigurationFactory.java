/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.cloud.cfg;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/12/16
 */
public class ConfigurationFactory {
    private static ConfigurationFactory factory;

    private ConfigurationFactory() {
    }

    public static ConfigurationFactory getFactory() {
        synchronized (ConfigurationFactory.class) {
            if(factory == null)
                factory = new ConfigurationFactory();
        }
        return factory;
    }


}
