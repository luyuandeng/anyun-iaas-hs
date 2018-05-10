/*
 *
 *      Configuration.java
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

package com.anyun.sdk.platfrom.core.rest;

import com.anyun.cloud.tools.db.AbstractEntity;
import com.anyun.cloud.tools.json.JsonUtil;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class Configuration extends AbstractEntity {
    private String platformAddress;
    private int port = 8989;
    private String baseUrl;
    private boolean https = false;

    public static Configuration buildConfiguration(String jsonBody) {
        return JsonUtil.fromJson(Configuration.class,jsonBody);
    }

    public Configuration() {
    }

    public String getPlatformAddress() {
        return platformAddress;
    }

    public void setPlatformAddress(String platformAddress) {
        this.platformAddress = platformAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }
}
