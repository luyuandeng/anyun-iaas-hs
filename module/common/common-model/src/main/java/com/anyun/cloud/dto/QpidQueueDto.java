package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by zy on 17-7-31.
 */
public class QpidQueueDto extends AbstractEntity {
    private String id;
    private String name;
    private String type;
    private String queueDethMessage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQueueDethMessage() {
        return queueDethMessage;
    }

    public void setQueueDethMessage(String queueDethMessage) {
        this.queueDethMessage = queueDethMessage;
    }
}
