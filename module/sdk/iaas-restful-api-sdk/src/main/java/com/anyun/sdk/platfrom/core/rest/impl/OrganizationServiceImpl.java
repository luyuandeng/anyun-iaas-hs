/*
 *
 *      OrganizationServiceImpl.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Successed;
import com.anyun.cloud.api.account.Organization;
import com.anyun.cloud.param.OrganizationAddParam;
import com.anyun.sdk.platfrom.OrganizationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class OrganizationServiceImpl extends AbstractSdkService implements OrganizationService {
    public static String PATH_ADD_ORIGANIZATION = "/account/organization/add";
    public static String PATH_GET_ORIGANIZATION = "/account/organization/info/";

    public OrganizationServiceImpl() {
    }

    @Override
    public Id addOrganization(OrganizationAddParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_ADD_ORIGANIZATION,param.asJson());
        Id<String> id = ResourceClient.convertToAnyunEntity(Id.class,response);
        return id;
    }

    @Override
    public Organization getOrganization(String id) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.query(PATH_GET_ORIGANIZATION + "/" + id,null);
        Organization successed = ResourceClient.convertToAnyunEntity(Organization.class,response);
        return successed;
    }
}
