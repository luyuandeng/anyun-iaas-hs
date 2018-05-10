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

package com.anyun.esb.component.api.jaxrs.platform;

import javax.ws.rs.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/18/16
 */

@Path("/account/organization")
public class OrganizationApi {
    /**
     * 添加组织信息
     *
     * @param organization
     * @return
     * @see com.anyun.cloud.param.OrganizationAddParam
     */
    @PUT
    @Path("/add/")
    @Produces("application/json")
    public String organization_add(String organization) {
        return null;
    }

    @GET
    @Path("/info/{id}/")
    @Produces("application/json")
    public String organization_query(@PathParam("id") String id) {
        return null;
    }
}
