package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;


/**
 * Created by gp on 16-9-14.
 */
public class MonitorHostDto extends AbstractEntity {
    private String cpu;        //完成
    private MemoryDto memory;  //内存
    private String harddisk;   // 无硬盘内容
    private List<NetMonitorDto> network;  //网络
    private List<FileSystemDto> filesystem; // 文件使用
    private String date;//查询时间
    private SystemUptimeDto systemUptimeDto; //系统运行时间


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public MemoryDto getMemory() {
        return memory;
    }

    public void setMemory(MemoryDto memory) {
        this.memory = memory;
    }

    public String getHarddisk() {
        return harddisk;
    }

    public void setHarddisk(String harddisk) {
        this.harddisk = harddisk;
    }

    public List<NetMonitorDto> getNetwork() {
        return network;
    }

    public void setNetwork(List<NetMonitorDto> network) {
        this.network = network;
    }

    public List<FileSystemDto> getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(List<FileSystemDto> filesystem) {
        this.filesystem = filesystem;
    }

    public SystemUptimeDto getSystemUptimeDto() {
        return systemUptimeDto;
    }

    public void setSystemUptimeDto(SystemUptimeDto systemUptimeDto) {
        this.systemUptimeDto = systemUptimeDto;
    }
}
