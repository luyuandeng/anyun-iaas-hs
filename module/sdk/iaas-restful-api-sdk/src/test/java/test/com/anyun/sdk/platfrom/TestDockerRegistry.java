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

package test.com.anyun.sdk.platfrom;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/22/16
 */
public class TestDockerRegistry {
    public static void main(String[] args) throws Exception{
        String username = "user";
        String password = "password";

        String usernameAndPassword = username + ":" + password;
        String authorizationHeaderName = "Authorization";
        String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );

        // Build the form for a post request
//        MultivaluedMap<String,String> formParameters = new MultivaluedHashMap();
//        formParameters.add( "field1", "fieldValue1" );
//        formParameters.add( "field2", "fieldValue2" );

        // Perform a post request
        String restResource = "http://192.168.1.117/";
        Client client = ClientBuilder.newClient();
        Response res = client.target( restResource )
                .path( "/v2/anyun/tag/list" ) // API Module Path
                .request( "application/json" ) // Expected response mime type
                .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                .get();     // Perform a post with the form values

        System.out.println(res.getStatus());
        System.out.println(res.readEntity(String.class));
    }
}
