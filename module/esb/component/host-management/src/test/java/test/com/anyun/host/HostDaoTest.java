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

package test.com.anyun.host;

import com.anyun.cloud.dto.DockerHostInfoDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.esb.component.host.dao.DatabaseFactory;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class HostDaoTest extends BaseComponentTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostDaoTest.class);
    private HostBaseInfoDao hostBaseInfoDao;

    @Before
    public void setUp() throws Exception {
        DatabaseFactory.getFactory().test();
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Test
    public void testInsertRequest() throws Exception {
        hostBaseInfoDao.insertHostRequest("2940546772D03BBA90FBB068806C5CAF");
    }

    @Test
    public void testUpdateHostManagementIp() throws Exception {
        String host = "CC9322EC5DB47376D59566F83F2174A3";
        String ip = "172.20.0.3";
        hostBaseInfoDao.updateHostManagementIp(host,ip);
    }

    @Test
    public void testInsertHostManagementIp() throws Exception {
        String host = "CC9322EC5DB47376D59566F83F2174A3";
        String ip = "172.20.0.2";
        hostBaseInfoDao.insertHostManagementIp(host,ip);
    }

    @Test
    public void testSelectHostBaseInfo() throws Exception {
        String id = "CC9322EC5DB47376D59566F83F2174A3";
        HostBaseInfoDto hostBaseInfoDto = hostBaseInfoDao.selectById(id);
        System.out.println(JbiCommon.toJson(hostBaseInfoDto));
    }

    @Test
    public void testUpdateHostBaseInfo() throws Exception {
        String id = "CC9322EC5DB47376D59566F83F2174A3";
        HostBaseInfoDto hostBaseInfoDto = hostBaseInfoDao.selectById(id);
        hostBaseInfoDao.updateDockerHostInfo(hostBaseInfoDto.getDockerHostInfoDto());
    }
    @Test
    public void testAddDockerHostInfo() throws Exception {
        String id = "CC9322EC5DB47376D59566F83F2174A3";
        DockerHostInfoDto dto = new DockerHostInfoDto();
        dto.setHost(id);
        dto.setArch("amd64");
        dto.setGitCommit("5604cbe");
        dto.setGoVersion("go1.5.4");
        dto.setKernelVersion("3.19.0-29-generic");
        dto.setOperatingSystem("linux");
        dto.setVersion("1.11.1");
        hostBaseInfoDao.insertDockerHostInfo(dto);
    }
}
