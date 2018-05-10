/*
 *
 *      ZookeeperFactory.java
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

package com.anyun.cloud.tools.scheduling;

import com.anyun.cloud.tools.StringUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Created by TwitchGG on 9/25/15.
 */
public class ZookeeperFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperFactory.class);
    public static final List<ACL> READ_PERMS= Collections.singletonList(new ACL(ZooDefs.Perms.READ, ZooDefs.Ids.ANYONE_ID_UNSAFE));
    public static final List<ACL> ALL_PERMS = Collections.singletonList(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.ANYONE_ID_UNSAFE));

    private static ZookeeperFactory factory;
    private ZookeeperConnector zookeeperConnector;

    private ZookeeperFactory() {
        zookeeperConnector = new ZookeeperConnector();
    }

    public static ZookeeperFactory getFactory() {
        synchronized (ZookeeperFactory.class) {
            if (factory == null)
                factory = new ZookeeperFactory();
        }
        return factory;
    }

    public void connect(String host, int timeout) throws Exception {
        zookeeperConnector.connect(host, 10);
    }

    public void addNodeStatusListener(final NodeListener nodeListener) throws Exception {
        String path = nodeListener.getListenerPath();
        ZooKeeper zooKeeper = zookeeperConnector.getZooKeeper();
        DefaultDataWatcher defaultDataWatcher = new DefaultDataWatcher(path, nodeListener);
        zooKeeper.getChildren(path, defaultDataWatcher);
        zooKeeper.getChildren(path, defaultDataWatcher, new AsyncCallback.Children2Callback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                nodeListener.processResult(rc,path, ctx,children,stat);
            }
        },path);
        LOGGER.info("Anyun cloud cluster scheduling listener [{}] added",nodeListener.getClass().getName());
    }

    public ZookeeperConnector getZookeeperConnector() {
        return zookeeperConnector;
    }

    public static void createPath(String path,String body) throws Exception {
        ZooKeeper zk = getFactory().getZookeeperConnector().getZooKeeper();
        byte[] data = null;
        if(StringUtils.isNotEmpty(body))
            data = body.getBytes();
        if(exist(path)) {
            zk.setData(path,data,-1);
        } else {
            zk.create(path,data,ZookeeperFactory.ALL_PERMS, CreateMode.PERSISTENT);
        }
    }

    public static void deletePath(String path) {
        ZooKeeper zk = getFactory().getZookeeperConnector().getZooKeeper();
        try {
            zk.delete(path, -1);
        } catch (Exception ex) {
            LOGGER.error("Zookeeper path [{}] delete error: {}", path,ex.getMessage());
        }
    }

    public static boolean exist(String path) throws Exception{
        ZooKeeper zk = getFactory().getZookeeperConnector().getZooKeeper();
        return zk.exists(path,null) != null;
    }

    public static String getStringData(String path) throws Exception{
        byte[] data = getData(path);
        if(data == null)
            return null;
        return new String(data);
    }

    public static byte[] getData(String path) throws Exception {
        ZooKeeper zk = getFactory().getZookeeperConnector().getZooKeeper();
        if(zk.exists(path,null) == null)
            return null;
        return zk.getData(path,null,null);
    }
}
