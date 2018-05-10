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

import com.anyun.common.jbi.JbiCommon;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.SSHFileCopy;
import com.anyun.esb.component.host.service.publish.HostApprovalService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Version;
import org.junit.Before;
import org.junit.Test;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/6/16
 */
public class HostClientTest  extends BaseHostTest{
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testVersion() throws Exception {
       DockerClient dockerClient = getClient("192.168.1.14");
        Version version = dockerClient.versionCmd().exec();
        System.out.println("Docker version [" + JbiCommon.toJson(version) +"]");
    }

    @Test
    public void testCreate()throws Exception{
//        HostSshClient hostSshClient=new HostSshClient("root","192.168.1.154",22,"1234qwer");
//        HostSshClient hostSshClient1=new HostSshClient("root","192.168.1.155",22,"1234qwer");
//        SSHFileCopy sshFileCopy=new SSHFileCopy(hostSshClient.getSession(),hostSshClient1.getSession());
//        sshFileCopy.copy("/root/.ssh/ssh.zip","/root/.ssh/");
    }
}
