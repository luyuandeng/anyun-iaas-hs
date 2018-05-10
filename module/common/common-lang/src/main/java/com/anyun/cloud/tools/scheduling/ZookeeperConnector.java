/*
 *
 *      ZookeeperConnector.java
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

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by TwitchGG on 9/23/15.
 */
public class ZookeeperConnector {
    private ZooKeeper zookeeper;
    private final CountDownLatch connectedSignal = new CountDownLatch(1);

    public void close() throws InterruptedException {
        zookeeper.close();
    }

    public void connect(String host,int timeOut) throws Exception {
        zookeeper = new ZooKeeper(host, 5000,
                new Watcher() {
                    public void process(WatchedEvent event) {
                        if (event.getState() == Event.KeeperState.SyncConnected) {
                            connectedSignal.countDown();
                        }
                    }
                });
        connectedSignal.await(timeOut, TimeUnit.SECONDS);
        if (zookeeper == null || !zookeeper.getState().equals(ZooKeeper.States.CONNECTED)) {
            zookeeper = null;
            throw new IllegalStateException("ZooKeeper is not connected.");
        }
    }

    public ZooKeeper getZooKeeper() {
        return zookeeper;
    }

}
