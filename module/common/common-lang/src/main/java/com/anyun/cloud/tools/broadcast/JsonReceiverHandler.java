/*
 *
 *      JsonReceiverHandler.java
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jgroups.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 9/30/15
 */
public class JsonReceiverHandler extends ReceiverHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonReceiverHandler.class);

    @Override
    public void receiveMessage(Address src, String msg) {
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        Gson gson = gb.create();
        CommandEntity entity = gson.fromJson(msg, CommandEntity.class);
        if (entity == null) {
            return;
        }
        String commandType = entity.getType();
        LOGGER.debug("Receiver command handler '{}'", commandType);
        CommandHandler handler = HandlerRegister.getRegister().getCommandHandler(entity.getType());
        if (handler == null) {
            LOGGER.warn("Handler '{}' is not registed", commandType);
            return;
        }
        LOGGER.debug("Command handler '{}' execute", handler.getClass().getName());
        Object commandEntity = gson.fromJson(entity.getContent(), handler.getContentType());
        handler.handler(src, commandEntity);
    }
}
