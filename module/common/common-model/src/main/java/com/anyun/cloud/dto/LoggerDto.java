package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by gp on 16-11-2.
 */
public class LoggerDto extends AbstractEntity {
    private List<LoggerDataDto> data;
    private String pageCount;
    private String pageNum;

    public List<LoggerDataDto> getData() {
        return data;
    }

    public void setData(List<LoggerDataDto> data) {
        this.data = data;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }
}
