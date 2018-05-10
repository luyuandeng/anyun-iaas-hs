/*
 *
 *      Common.java
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

package com.anyun.common.jbi;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.exception.JbiComponentException;
import com.anyun.exception.ServerErrorEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.cxf.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/24/16
 */
public class JbiCommon {
    public static final Logger LOGGER = LoggerFactory.getLogger(JbiCommon.class);
    public static final String JBI_MQ_URI_DATA_ACCESS = "rabbitmq://remote/com.anyun.data.access" +
            "?connectionFactory=#rabbitmqConnectionFactory";
    public static final String COMPNOENT_ERROR = "ANYUN.CLOUD.COMPONENT.RETURN.ERROR";
    public static final String COMPNOENT_ERROR_TYPE = "ANYUN.CLOUD.COMPONENT.RETURN.ERROR.TYPE";
    public static String FLOW_1_PARAM_VALIDATE_ERROR = "FLOW_1_PARAM_VALIDATE_ERROR";
    public static String FLOW_CAN_NEXT = "FLOW_CAN_NEXT";

    public static String buildValidateErrorEntity(JbiComponentException ex) {
        ServerErrorEntity.ServerError error = new ServerErrorEntity.ServerError();
        error.setCode(ex.getCode());
        error.setError_subcode(ex.getSubCode());
        error.setError_user_msg(ex.getUserMessage());
        error.setError_user_title(ex.getUserTitle());
        error.setMessage(ex.getMessage());
        error.setType(ex.getType());
        ServerErrorEntity errorEntity = new ServerErrorEntity();
        errorEntity.setError(error);
        return toJson(errorEntity);
    }

    public static <T> T getComponentBodyObject(Exchange exchange, Class<T> type) throws JbiComponentException {
        String body = getComponentBody(exchange);
        if (StringUtils.isEmpty(body))
            return null;
        if (type.equals(String.class))
            return (T) body;
        return convertToAnyunEntity(type, body);
    }

    public static String getComponentBody(Exchange exchange) throws JbiComponentException {
        String body = new String((byte[]) exchange.getIn().getBody());
        boolean isComponentError = (boolean) exchange.getIn().getHeader(COMPNOENT_ERROR);
        if (isComponentError) {
            String errorTypeName = (String) exchange.getIn().getHeader(COMPNOENT_ERROR_TYPE);
            LOGGER.debug("Component error body [{}]", body);
            ServerErrorEntity serverErrorEntity = null;
            JbiComponentException exception = null;
            try {
                serverErrorEntity = JsonUtil.fromJson(ServerErrorEntity.class, body);
                if (serverErrorEntity == null)
                    throw new Exception();
                ServerErrorEntity.ServerError error = serverErrorEntity.getError();
                exception = new JbiComponentException(error.getCode(), error.getError_subcode());
                exception.setMessage(error.getMessage());
                exception.setUserTitle(error.getError_user_title());
                exception.setUserMessage(error.getError_user_msg());
            } catch (Exception ex) {
                exception = new JbiComponentException(5000, 5001);
                exception.setUserTitle("Bad component error type");
                exception.setUserMessage("非法的组件调用错误类型");
                exception.setMessage("Bad component error type [" + errorTypeName + "]");
            }
            exchange.setProperty(FLOW_CAN_NEXT, false);
            exchange.setProperty(FLOW_1_PARAM_VALIDATE_ERROR, exception);
            throw exception;
        }
        return body;
    }


    public static <T> T convertToAnyunEntity(Class<T> type, String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
//            JsonNode root = mapper.readTree(response);
//            String contentJson = root.findValue("success").findValue("content").toString();
            return mapper.readValue(response, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> convertToListObject(Class<T> type, String response) {
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, Object>> entities = mapper.readValue(response, List.class);
            for (Map<String, Object> entry : entities) {
                T _entry = new ObjectMapper().readValue(toJson(entry), type);
                list.add(_entry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return list;
        }
        return list;
    }

    public static String toJson(Object obj) {
        try {
            GsonBuilder ex = new GsonBuilder();
            ex.disableHtmlEscaping();
            ex.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            ex.setPrettyPrinting();
            Gson gson = ex.create();
            return gson.toJson(obj);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static <T> T getBodyParam(String body, Class<T> type) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);
        String contentJson = root.findValue("params").findValue("content").toString();
        LOGGER.debug("Params string [{}]", contentJson);
        return mapper.readValue(contentJson, type);
    }

    public static Map<String, String> convertHttpQueryParams(Exchange exchange) {
        Message message = (Message) exchange.getIn().getHeader("CamelCxfMessage");
        String queryString = (String) message.get(Message.QUERY_STRING);
        LOGGER.debug("Parse http query string [{}]", queryString);
        if (StringUtils.isEmpty(queryString))
            return new HashMap<>();
        Map<String, String> map = new HashMap<>();
        String[] str1 = StringUtils.getSplitValues(queryString, "&");
        LOGGER.debug("Parsed http query string size [{}]", str1.length);
        for (String str : str1) {
            if (!str.contains("=")) {
                map.put(str, "");
                continue;
            }
            String[] entry = StringUtils.getSplitValues(str, "=");
            if (entry.length == 1)
                map.put(entry[0], "");
            else
                map.put(entry[0], entry[1]);
            LOGGER.debug("Put http query string [{}][{}]", entry[0], entry[1]);
        }
        return map;
    }

    public static Map<String, List<String>> splitQuery(String queryParam) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = queryParam.split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }
}
