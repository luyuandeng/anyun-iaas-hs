package com.anyun.cloud.agent.result;

/**
 * Created by twitchgg on 16-8-3.
 */
public class HostResponse <T> {
    private String responseType;
    private T response;
    private long systemTime;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }
}
