/*
 *
 *      ReceiverHandler.java
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

package com.anyun.cloud.tools.broadcast;

import org.jgroups.Address;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 9/30/15
 */
public abstract class ReceiverHandler extends ReceiverAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiverHandler.class);

    @Override
    public void receive(Message msg) {
        Address address = msg.getSrc();
        JGroupReceiver jgr = (JGroupReceiver) ReceiverFactory.getfaFactory().getReceiver();
        if (jgr.getChannel().getAddress().equals(address)) {
            return;
        }
        Object reveivedObject = msg.getObject();
        if (!(reveivedObject instanceof String)) {
            return;
        }
        String json = (String) reveivedObject;
        LOGGER.debug("Reveived Message [{}] '{}'", msg.size(), json);
        receiveMessage(address, json);
    }

    public abstract void receiveMessage(Address src, String msg);
}
