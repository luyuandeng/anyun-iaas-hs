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

package com.anyun.cloud.agent.result;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/17/16
 */
public class Version {
    private String api;
    private OperateSystemVersion os;
    private AgentVersion agent;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public OperateSystemVersion getOs() {
        return os;
    }

    public void setOs(OperateSystemVersion os) {
        this.os = os;
    }

    public AgentVersion getAgent() {
        return agent;
    }

    public void setAgent(AgentVersion agent) {
        this.agent = agent;
    }
}
