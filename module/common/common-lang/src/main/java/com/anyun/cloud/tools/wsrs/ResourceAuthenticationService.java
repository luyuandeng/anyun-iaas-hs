/*
 *
 *      ResourceAuthenticationManagement.java
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

package com.anyun.cloud.tools.wsrs;

import com.anyun.cloud.tools.execption.ResourceAuthenticationException;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 11/17/15
 */
public interface ResourceAuthenticationService {

    /**
     *
     * @param remoteIp
     * @param path
     * @param sessionTick
     * @throws ResourceAuthenticationException
     */
    void checkResource(String remoteIp, String path, String sessionTick) throws ResourceAuthenticationException;
}
