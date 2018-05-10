/*
 *
 *      AnyunSdkClientFactory.java
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

import com.anyun.sdk.platfrom.*;
import com.anyun.sdk.platfrom.core.rest.impl.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class AnyunSdkClientFactory {
    private static AnyunSdkClientFactory factory;
    private Configuration configuration;

    public static AnyunSdkClientFactory getFactory() {
        synchronized (AnyunSdkClientFactory.class) {
            if (factory == null) {
                factory = new AnyunSdkClientFactory();
            }
            return factory;
        }
    }

    private AnyunSdkClientFactory() {

    }

    public ResourceClient getResourceClient() {
        StringBuilder rootUrl = new StringBuilder();
        rootUrl.append(configuration.isHttps() ? "https://" : "http://");
        rootUrl.append(configuration.getPlatformAddress());
        rootUrl.append(":").append(configuration.getPort());
        rootUrl.append("/").append(configuration.getBaseUrl());
        return ResourceClient.getClient(rootUrl.toString());
    }

    public void config(Configuration configuration) {
        this.configuration = configuration;
    }

    public HostService getHostService() {
        return new HostServiceImpl();
    }

    public ContainerService getContainerService() {
        return new ContainerServiceImpl();
    }

    public ProjectService getProjectService() {
        return new ProjectServiceImpl();
    }

    public StorageService getStorageService() {
        return new StorageServiceImpl();
    }

    public ImageService getImageService() {
        return new ImageServiceImpl();
    }

    public NetService getNetService() {
        return new NetServiceImpl();
    }

    public MonitorService getMoniotrService() {
        return new MonitorServiceImpl();
    }

    public PictureService getPictureService() {
        return new PictureServiceImpl();
    }

    public SecurityGroupService getSecurityGroupService() {
        return new SecurityGroupServiceImpl();
    }

    public PlatformService getPlatformService() {
        return new PlatformServiceImpl();
    }

    public ApplicationService getApplicationService() {
        return new ApplicationServiceImpl();
    }

    public LoggerService getLoggerService() {
        return new LoggerServiceImpl();
    }

    public ServiceOperationService getServiceOperationService() {
        return new ServiceOperationServiceImpl();
    }

    public NetL2Service getNetL2Service() {
        return new NetL2ServiceImpl();
    }

    public ClusterService getClusterService(){
        return new ClusterServiceImpl();
    }

    public AssetsService getAssetsService(){
        return new AssetsServiceImpl();
    }
    
    public TagService getTagService() {
        return new TagServiceImpl();
    }

    public DatabaseService getDatabaseService() {
        return new DatabaseServiceImpl();
    }


    public DiskSchemeService getDiskSchemeService(){return new DiskSchemeServiceImpl();}

    public CalculationSchemeService getCalculationSchemeService(){
        return new CalculationSchemeServiceImpl();
    }
}
