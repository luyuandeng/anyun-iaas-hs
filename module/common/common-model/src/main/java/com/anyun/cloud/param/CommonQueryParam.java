package com.anyun.cloud.param;


import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 17-4-25.
 */
public class CommonQueryParam extends AbstractEntity {
    private String userUniqueId;//用户唯一标识
    private boolean count = false;  //是否count查询   true 只返回总条数  不返回具体信息   false: 分页查询（默认）
    private int start = 1; //分页offset start
    private int limit = 3;//分页 每页记录数目
    private boolean replyWithCount = true;//返回结果是否包含总条数
    private List<Conditions> conditions;////查询条件列表
    private String sortBy = "";//排序字段
    private String sortDirection = "asc"; //排序方向  asc:正序（默认）   desc ：倒序

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isReplyWithCount() {
        return replyWithCount;
    }

    public void setReplyWithCount(boolean replyWithCount) {
        this.replyWithCount = replyWithCount;
    }

    public List<Conditions> getConditions() {
        return conditions;
    }

    public void setConditions(List<Conditions> conditions) {
        this.conditions = conditions;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
