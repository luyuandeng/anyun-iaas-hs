/*
 *
 *      UserSessionManagement.java
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

/**
 * Created by twitchgg on 10/27/15.
 */
public class UserSessionManagement {
    private static UserSessionManagement sessionManagement = null;
    private static final ThreadLocal<String> USER_SESSION_TICK = new ThreadLocal<>();

    private UserSessionManagement() {
    }

    public static UserSessionManagement getSessionManagement() {
        synchronized (UserSessionManagement.class) {
            if(sessionManagement == null) {
                sessionManagement = new UserSessionManagement();
            }
        }
        return sessionManagement;
    }

    public void setUserSessionTick(String tick) {
        USER_SESSION_TICK.set(tick);
    }

    public String getUserSessionTick() {
        return USER_SESSION_TICK.get();
    }
}
