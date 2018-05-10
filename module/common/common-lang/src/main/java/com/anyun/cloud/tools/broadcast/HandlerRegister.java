/*
 *
 *      HandlerRegister.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 9/30/15
 */
public class HandlerRegister {

    private final static Logger LOGGER = LoggerFactory.getLogger(HandlerRegister.class);
    private static HandlerRegister register;
    private final Map<String, CommandHandler> handlers;

    private HandlerRegister() {
        handlers = new HashMap<>();
    }

    public static HandlerRegister getRegister() {
        synchronized (HandlerRegister.class) {
            if (register == null) {
                register = new HandlerRegister();
            }
        }
        return register;
    }

    public CommandHandler getCommandHandler(String type) {
        return handlers.get(type);
    }

    public void regist(String type, CommandHandler handler) {
        handlers.put(type, handler);
        LOGGER.debug("Message handler type '{}' is registed,handler impl '{}'", type,handler.getClass().getName());
    }

    public void unregist(String type) {
        handlers.remove(type);
    }

    public Map<String, CommandHandler> getHandlers() {
        return handlers;
    }
}