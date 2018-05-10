/*
 *
 *      AccountServiceImpl.java
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
import com.anyun.cloud.dto.UserDto;
import com.anyun.cloud.param.UserAddParam;
import com.anyun.cloud.param.UserLoginByPaswordParam;
import com.anyun.cloud.tools.EncryptUtils;
import com.anyun.sdk.platfrom.AccountService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class AccountServiceImpl extends AbstractSdkService implements AccountService {
    public static final String PATH_LOGIN_BY_PASSWD = "/account/user/login/bypasswd";
    public static final String PATH_USER_ADD = "/account/user/add";

    @Override
    public UserDto login(String user, byte[] passwd) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        UserLoginByPaswordParam param = new UserLoginByPaswordParam();
        param.setUserName(user);
        param.setPassword(EncryptUtils.getMD5ofStr(new String(passwd)));
        String response = rsClient.post(PATH_LOGIN_BY_PASSWD, param.asJson());
        UserDto userDto = ResourceClient.convertToAnyunEntity(UserDto.class, response);
        return userDto;
    }

    @Override
    public Id<String> createUser(UserAddParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_USER_ADD, param.asJson());
        Id<String> userId = ResourceClient.convertToAnyunEntity(Id.class, response);
        return userId;
    }
}
