/*
 *
 *      ResourceClient.java
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

package com.anyun.sdk.platfrom.core.rest;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.exception.ServerErrorEntity;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/20/16
 */
public class ResourceClient {
    private static Logger LOGGER = LoggerFactory.getLogger(ResourceClient.class);

    private String url;
    private Client rsClient;
    private WebTarget webTarget;
    private static final ThreadLocal<String> USER_TOKEN = new ThreadLocal<>();

    private ResourceClient(String url) {
        this.url = url;
        rsClient = ClientBuilder.newClient();
        webTarget = rsClient.target(url);
    }

    public static ResourceClient getClient(String url) {
        return new ResourceClient(url);
    }

    public static void setUserToken(String token) {
        USER_TOKEN.set(token);
    }

    public String put(String path, Object content) throws RestfulApiException {
        webTarget = webTarget.path(path);
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Entity entity = Entity.entity(content, MediaType.APPLICATION_JSON);
        Response response = builder.put(entity);
        return readResponse(response);
    }

    public String delete(String path) throws RestfulApiException {
        webTarget = webTarget.path(path);
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Response response = builder.delete();
        return readResponse(response);
    }

    public OCSPResp ocspPost(String path, OCSPReq ocspReq) throws Exception {
        webTarget = webTarget.path(path);
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        builder.accept("application/ocsp-response");
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Entity<byte[]> entity = Entity.entity(ocspReq.getEncoded(), "application/ocsp-request");
        LOGGER.debug("OCSP request size [{}]", entity.getEntity().length);
        Response response = builder.post(entity);
        byte[] oscpResp = response.readEntity(byte[].class);
        OCSPResp ocspResp = new OCSPResp(oscpResp);
        return ocspResp;
    }

    public String post(String path, Object content) throws RestfulApiException {
        webTarget = webTarget.path(path);
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Entity entity = Entity.entity(content, MediaType.APPLICATION_JSON);
        Response response = builder.post(entity);
        return readResponse(response);
    }

    public String query(String path, Map<String, Object> params) throws RestfulApiException {
        webTarget = webTarget.path(path);
        LOGGER.debug("webTarget.getUri():[{}]",webTarget.getUri());
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOGGER.debug("webTarget.getUri():[{}]",webTarget.getUri());
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Response response = builder.get();
        return readResponse(response);
    }

    public String delete(String path, Map<String, Object> params) throws RestfulApiException {
        webTarget = webTarget.path(path);
        LOGGER.debug("webTarget.getUri():[{}]",webTarget.getUri());
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
            }
        }
        LOGGER.debug("webTarget.getUri():[{}]",webTarget.getUri());
        Invocation.Builder builder = webTarget.request();
        if (StringUtils.isNotEmpty(USER_TOKEN.get()))
            builder = builder.header("anyun_cloud_user_token", USER_TOKEN.get());
        Response response = builder.delete();
        return readResponse(response);
    }

    private String readResponse(Response response) throws RestfulApiException {
        if (response.getStatus() != 200) {
            LOGGER.error("Http Response Code [{}]", response.getStatus());
            RestfulApiException restfulApiException =
                    new RestfulApiException(response.getStatus(), 500, new Exception("Server error"));
            throw restfulApiException;
        }
        try {
            String responseEntity = response.readEntity(String.class);
            LOGGER.debug("Http Response [{}]", responseEntity);
            if (StringUtils.isEmpty(responseEntity))
                throw new RestfulApiException(response.getStatus(), 500, new Exception("Server not data"));
            ServerErrorEntity errorEntity = JsonUtil.fromJson(ServerErrorEntity.class, responseEntity);
            if (errorEntity == null) {
                return responseEntity;
            } else {
                ServerErrorEntity.ServerError error = errorEntity.getError();
                if (error == null) {
                    return responseEntity;
                }
                RestfulApiException exception = new RestfulApiException(error.getCode(), error.getError_subcode());
                exception.setUserMessage(error.getError_user_msg());
                exception.setUserTitle(error.getError_user_title());
                exception.setMessage(error.getMessage());
                exception.setType(error.getType());
                throw exception;
            }
        } catch (RestfulApiException ex) {
            throw ex;
        }
    }

    public static <T> T convertToAnyunEntity(Class<T> type, String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            String contentJson = root.findValue("success").findValue("content").toString();
            return mapper.readValue(contentJson, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> convertToListObject(Class<T> type, String response) {
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            String contentJson = root.findValue("success").findValue("content").toString();
            List<Map<String, Object>> entities = mapper.readValue(contentJson, List.class);
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
}
