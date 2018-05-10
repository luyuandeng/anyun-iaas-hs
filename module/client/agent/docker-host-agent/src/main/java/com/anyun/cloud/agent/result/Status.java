package com.anyun.cloud.agent.result;

/**
 * Created by twitchgg on 16-8-3.
 */
public class Status <T> {
    private T status;

    public Status() {
    }

    public Status(T status) {
        this.status = status;
    }

    public T getStatus() {
        return status;
    }

    public void setStatus(T status) {
        this.status = status;
    }
}
