package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-9-26.
 */
public class MonitorContainerDto extends AbstractEntity {
    private int runningContainer;
    private int stopContainer;

    @Override
    public String toString() {
        return "MonitorContainerDto{" +
                "runningContainer=" + runningContainer +
                ", stopContainer=" + stopContainer +
                '}';
    }

    public int getRunningContainer() {
        return runningContainer;
    }

    public void setRunningContainer(int runningContainer) {
        this.runningContainer = runningContainer;
    }

    public int getStopContainer() {
        return stopContainer;
    }

    public void setStopContainer(int stopContainer) {
        this.stopContainer = stopContainer;
    }
}