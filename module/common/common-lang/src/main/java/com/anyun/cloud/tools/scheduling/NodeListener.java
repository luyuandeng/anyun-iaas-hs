/*
 *
 *      NodeListener.java
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
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by TwitchGG on 9/25/15.
 */
public interface NodeListener {

    /**
     *
     * @param event
     */
    void process(WatchedEvent event);

    /**
     *
     * @param rc
     * @param path
     * @param ctx
     * @param children
     * @param stat
     */
    void processResult(int rc, String path, Object ctx, List<String> children, Stat stat);

    /**
     *
     * @return
     */
    String getListenerPath();

    /**
     *
     */
    void init();
}
