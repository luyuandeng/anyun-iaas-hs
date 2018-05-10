/*
 *
 *      UserSessionDto.java
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

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by twitchgg on 10/27/15.
 */
public class UserSessionDto extends AbstractEntity {
    private String sessionTick;
    private String userId;
    private String userName;
    private String organization;
    private long lastModifyTimestamp;

    public String getSessionTick() {
        return sessionTick;
    }

    public void setSessionTick(String sessionTick) {
        this.sessionTick = sessionTick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLastModifyTimestamp() {
        return lastModifyTimestamp;
    }

    public void setLastModifyTimestamp(long lastModifyTimestamp) {
        this.lastModifyTimestamp = lastModifyTimestamp;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
