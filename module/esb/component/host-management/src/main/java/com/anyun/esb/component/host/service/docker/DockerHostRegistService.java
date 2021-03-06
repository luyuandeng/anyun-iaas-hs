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

package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ContainerDto;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;

import java.util.List;


/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public interface DockerHostRegistService {

    /**
     *
     * @param host
     * @param serialNumber
     * @param managementIp
     * @throws Exception
     */
    void connectHost(String host, String serialNumber, String managementIp) throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    List<Image> getAllDockerImages() throws Exception;

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    DockerClient getDockerClient(String id) throws Exception;

    /**
     *
     * @param ip
     */
    void disconnectDockerHost(String ip);
}
