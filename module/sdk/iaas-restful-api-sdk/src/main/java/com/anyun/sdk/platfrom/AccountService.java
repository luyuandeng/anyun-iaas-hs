/*
 *
 *      Account.java
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

package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.MetaDataKey;
import com.anyun.cloud.dto.UserDto;
import com.anyun.cloud.param.UserAddParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public interface AccountService {

    /**
     * 用户登录
     *
     * @param user
     * @param passwd
     * @return
     * @throws RestfulApiException
     */
    UserDto login(String user, byte[] passwd) throws RestfulApiException;

    /**
     * 添加用户
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    Id<String> createUser(UserAddParam param) throws RestfulApiException;
}
