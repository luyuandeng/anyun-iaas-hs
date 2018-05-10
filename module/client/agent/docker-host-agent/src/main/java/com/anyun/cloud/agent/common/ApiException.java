package com.anyun.cloud.agent.common;

import com.anyun.cloud.api.Response;

/**
 * Created by twitchgg on 16-8-3.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public Response<ApiException> buildJson(String type) {
        Response<ApiException> response = new Response<>();
        response.setException(this);
        response.setType(type);
        return response;
    }
}
