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
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/23/16
 */
public class DockerTest {
    public static void main(String[] args) throws Exception{
        DefaultDockerClientConfig.Builder builder = DefaultDockerClientConfig.createDefaultConfigBuilder();
        builder.withRegistryUrl("imagehub.anyuncloud.com");
        builder.withRegistryEmail("twitchgg@yahoo.com");
        builder.withDockerHost("tcp://192.168.1.4:2375");
        builder.withRegistryUsername("user");
        builder.withRegistryPassword("password");
        builder.withDockerTlsVerify(false);
        DockerClientConfig dockerClientConfig = builder.build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig)
                .withDockerCmdExecFactory(DockerClientBuilder.getDefaultDockerCmdExecFactory())
                .build();
//        Version version = dockerClient.versionCmd().exec();
//        Info info = dockerClient.infoCmd().exec();
//        System.out.println(version.getVersion());
//        System.out.println(JbiCommon.toJson(info));
        System.out.println(JbiCommon.toJson(dockerClient.authCmd().exec()));
        String image = "imagehub.anyuncloud.com/anyun/deve/ubuntu:14.04";
        if(dockerClient.listImagesCmd().withImageNameFilter(image).exec().size() == 0) {
            PullImageResultCallback resultCallback = new PullImageResultCallback();
            dockerClient.pullImageCmd(image).exec(resultCallback);
            resultCallback.awaitSuccess();
        }
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(image);
        createContainerCmd.withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withTty(true)
                .withNetworkMode("none")
                .withMemory(1024L * 1024 * 4);
        String containerId = createContainerCmd.exec().getId();
        System.out.println(containerId);
        dockerClient.startContainerCmd(containerId).exec();
    }
}
