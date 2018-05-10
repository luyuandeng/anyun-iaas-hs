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

package com.anyun.cloud.host.startup.daemon;

import com.anyun.cloud.agent.common.Constant;
import com.anyun.cloud.agent.common.Env;
import com.anyun.cloud.agent.common.Utils;
import com.anyun.cloud.agent.common.sys.HostInfoEntity;
import com.anyun.cloud.tools.FileUtil;
import com.anyun.cloud.tools.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/25/16
 */
public class EnvironmentVariableInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentVariableInitialization.class);
    public static void init() throws Exception {
        File managementNetworkConfigFile = new File(Constant.PATH_CFG_SYSTEM);         //读取文件
        if (!managementNetworkConfigFile.exists())
            throw new Exception("file [PATH_CFG_NET_INTERFACE_MGR] not exist");
        String content = FileUtil.cat(managementNetworkConfigFile, "utf-8");  //获取文件数据
        if (StringUtils.isEmpty(content))
            throw new Exception("file [PATH_CFG_NET_INTERFACE_MGR] not exist");
        Properties properties = new Properties();
        properties.load(new FileInputStream(managementNetworkConfigFile));
        Env.init(properties);                                                         //将文件数据初始化进内存
        String serialNumber = Utils.buildSerialNumber();                              //获取所有网卡的信息
        LOGGER.debug("Serial number [{}]",serialNumber);
        Env.export(Constant.ENV_SERIAL_NUMBER,serialNumber);
        HostInfoEntity entity = new HostInfoEntity().build();
        LOGGER.debug("System info [\n{}\n]",entity.asJson());
    }
}
