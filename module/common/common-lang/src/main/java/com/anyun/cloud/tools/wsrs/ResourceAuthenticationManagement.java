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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 11/17/15
 */
public class ResourceAuthenticationManagement {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceAuthenticationManagement.class);
    private static ResourceAuthenticationManagement MANAGEMENT = null;
    private ResourceAuthenticationService resourceAuthenticationService;

    private ResourceAuthenticationManagement() {
    }

    public static ResourceAuthenticationManagement getManagement() {
        synchronized (ResourceAuthenticationManagement.class) {
            if(MANAGEMENT == null)
                MANAGEMENT = new ResourceAuthenticationManagement();
        }
        return MANAGEMENT;
    }

    public ResourceAuthenticationService getResourceAuthenticationService() {
        return resourceAuthenticationService;
    }

    public void setResourceAuthenticationService(ResourceAuthenticationService resourceAuthenticationService) {
        this.resourceAuthenticationService = resourceAuthenticationService;
        LOGGER.debug("ResourceAuthenticationService set to [{}]",resourceAuthenticationService.getClass().getName());
    }

    public void checkApiResource(String remoteIp,String path,String sessionTick) throws ResourceAuthenticationException {
        if(resourceAuthenticationService == null) {
            LOGGER.warn("ResourceAuthenticationService is not set,ignore");
            return;
        }
        resourceAuthenticationService.checkResource(remoteIp,path,sessionTick);
    }
}
