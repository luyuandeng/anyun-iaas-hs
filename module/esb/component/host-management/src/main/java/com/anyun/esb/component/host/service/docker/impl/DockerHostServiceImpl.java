package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.service.docker.DockerHostService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twitchgg on 16-7-19.
 */
public class DockerHostServiceImpl implements DockerHostService {
    private static final String ZK_PATH_HOST = "/anyuncloud/host";

    @Override
    public List<String> findAllActiveHosts() throws Exception {
        ZKConnector zookeeperConnector = Env.env(ZKConnector.class);
        List<String> children = zookeeperConnector.getChildren(ZK_PATH_HOST);
        if (children == null || children.isEmpty())
            return new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (String name : children) {
            result.add(zookeeperConnector.getStringData(ZK_PATH_HOST + "/" + name));
        }
        return result;
    }
}
