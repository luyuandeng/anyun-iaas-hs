package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 17-5-15.
 */
public class ContainerOpParam extends AbstractEntity {
    private String userUniqueId;//用户唯一标识
    private String id; //容器id
    /**
     * start：启动
     * restart：重启
     * stop：停止
     * pause：暂停
     * unpause：解除暂停
     * delete：删除
     * kill  : 结束进程
     */
    private String op; //操作


    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }
}
