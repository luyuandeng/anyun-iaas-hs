package com.anyun.cloud.agent.result;

/**
 * Created by twitchgg on 16-8-3.
 */
public class ExecuterStatus<T>{
    private int code = 0;
    private T status;

    public T getStatus() {
        return status;
    }

    public void setStatus(T status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
