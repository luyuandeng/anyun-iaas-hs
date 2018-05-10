/*
 *
 *      DefaultDataWatcher.java
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

/**
 * Created by TwitchGG on 9/25/15.
 */
public class DefaultDataWatcher implements Watcher{
    private NodeListener nodeListener;
    private String path;

    public DefaultDataWatcher(String path,NodeListener nodeListener) {
        this.nodeListener = nodeListener;
        this.path = path;
    }

    @Override
    public void process(final WatchedEvent event) {
        try {
            ZookeeperFactory.getFactory().addNodeStatusListener(nodeListener);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nodeListener.process(event);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public NodeListener getNodeListener() {
        return nodeListener;
    }

    public String getPath() {
        return path;
    }
}
