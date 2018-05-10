/*
 *
 *      PlatformResourceBuilder.java
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.anyun.cloud.tools.wsrs;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.message.Message;

/**
 *
 * @author TwitchGG
 * @date 2015-4-21
 * @version 1.0
 */
public class PlatformResourceBuilder {
    private static PlatformResourceBuilder builder;
    private final List<Class<?>> resources;
    private final List<ResourceProvider >providers;
    private final List<Interceptor<? extends Message>> interceptors;
    
    private PlatformResourceBuilder() {
        resources = new ArrayList();
        providers = new ArrayList();
        interceptors = new ArrayList<>();
    }
    
    public static PlatformResourceBuilder getBuilder() {
        synchronized (PlatformResourceBuilder.class) {
            if(builder == null)
                builder = new PlatformResourceBuilder();
        }
        return builder;
    }
    
    public List<Class<?>> getResources() {
        return resources;
    }

    public List<ResourceProvider> getProviders() {
        return providers;
    }

    public List<Interceptor<? extends Message>> getInterceptors() {
        return interceptors;
    }

    public void addResource(Class<?> resourceClass) {
        resources.add(resourceClass);
    }
     
     public void addResourceProvider(Object instance) {
         providers.add(new SingletonResourceProvider(instance));
     }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}
