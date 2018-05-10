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

package com.anyun.cloud.agent.common;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/25/16
 */
public class Constant {
    public static String PATH_ETC = "/etc/anyuncloud";
    public static String PATH_CFG_SYSTEM = PATH_ETC + "/system.properties";

    public static String ENV_NET_INTERFACE_MGR = "path.net.interface.management";
    public static String ENV_ZOOKEEPER_CONNECT_STRING = "zookeeper.connection.string";
    public static String ENV_ZOOKEEPER_TIMEOUT = "zookeeper.connection.timeout";
    public static String ENV_SERIAL_NUMBER = "hardware.serial";

    public static String ZK_PATH_HOST = "/anyuncloud/host";
}
