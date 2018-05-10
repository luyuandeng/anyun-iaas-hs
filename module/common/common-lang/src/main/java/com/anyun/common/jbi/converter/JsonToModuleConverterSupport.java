/*
 *
 *      JsonToModuleConverterSupport.java
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

package com.anyun.common.jbi.converter;

import com.anyun.cloud.tools.json.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/21/16
 */
public class JsonToModuleConverterSupport extends TypeConverterSupport {
    public static Logger LOGGER = LoggerFactory.getLogger(JsonToModuleConverterSupport.class);

    public <T> T convertTo(Class<T> type, Exchange exchange, Object value) throws TypeConversionException {
        LOGGER.info("Module Type: [{}]",type);
        String jsonValue = (String)value;
        LOGGER.info("Json Value: [{}]",jsonValue);
        return (T) JsonUtil.fromJson(type,jsonValue);
    }
}
