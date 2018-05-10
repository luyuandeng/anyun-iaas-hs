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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.junit.Assert;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/6/16
 */
public class BaseHostTest extends Assert {

    public DockerClient getClient(String address) throws Exception {
        String email = "twitchgg@yahoo.com";
        String passwd = "N@yveCY5Y38=!==uuW";
        String user = "TwitchGG";
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder();
        builder.withDockerHost("tcp://" + address + ":2375");
        builder.withRegistryEmail(email);
        builder.withRegistryUsername(user);
        builder.withRegistryPassword(passwd);
        builder.withDockerTlsVerify(false);
        DockerClientConfig dockerClientConfig = builder.build();
        return DockerClientBuilder.getInstance(dockerClientConfig)
                .withDockerCmdExecFactory(DockerClientBuilder.getDefaultDockerCmdExecFactory())
                .build();
    }
}
