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

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/20/16
 */
@Path("/cert")
public class CertificateApi {
    @POST
    @Path("/ocsp/validation")
    @Consumes("application/ocsp-request")
    @Produces("application/ocsp-response")
    @RestMethodDefine(needAuthentication = false,
            component = "anyun-aaa",
            operate = "certificate.ocsp.validation",
            service = "certificate_ocsp_validation")
    public byte[] cert_ocsp_validation(byte[] cert) {
        return null;
    }

    @POST
    @Path("/revocation")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = false,
            component = "anyun-aaa",
            operate = "cert.ocsp.revocation",
            service = "cert_ocsp_revocation")
    public String cert_ocsp_revocation(String body) {
        return null;
    }

    @POST
    @Path("/csr/user/request")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = false,
            component = "anyun-aaa",
            operate = "cert.csr.request",
            service = "cert_csr_request")
    public String cert_csr_request(String body) {
        return null;
    }

    @POST
    @Path("/csr/approval")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = false,
            component = "anyun-aaa",
            operate = "cert.csr.approval",
            service = "cert_csr_approval")
    public String cert_csr_approval(String body) {
        return null;
    }

    @GET
    @Path("/request")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = false,
            component = "anyun-aaa",
            operate = "cert.request.query",
            service = "cert_request_query")
    public String cert_request_query(
            @QueryParam("pageSize") int pageSize,
            @QueryParam("pageNumber") int pageNumber,
            @QueryParam("status") int status,
            @QueryParam("userName") String userName) {
        return null;
    }
}
