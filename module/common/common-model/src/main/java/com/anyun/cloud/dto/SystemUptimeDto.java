package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-12-1.
 */
public class SystemUptimeDto extends AbstractEntity {
    private String alreadyRunTime; //已经运行时间  单位(毫秒)
    private String formatTime;// 格式化运行时间
    private String startTime; //系统启动时间

    public String getAlreadyRunTime() {
        return alreadyRunTime;
    }

    public void setAlreadyRunTime(String alreadyRunTime) {
        this.alreadyRunTime = alreadyRunTime;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
