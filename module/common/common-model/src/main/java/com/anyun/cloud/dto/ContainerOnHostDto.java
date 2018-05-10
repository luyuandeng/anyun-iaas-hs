package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 17-3-28.
 */
public class ContainerOnHostDto extends AbstractEntity {
    private int total;//总数量
    private int run;//运行的
    private int stop;//停止的
    private List<ContainerDto> list;//容器列表

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public List<ContainerDto> getList() {
        return list;
    }

    public void setList(List<ContainerDto> list) {
        this.list = list;
    }
}
