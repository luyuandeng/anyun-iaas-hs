package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-28.
 */
public class OvsBridgeParam extends AbstractEntity{
    private String bridge;

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    private String priority;         //权重
    private String nw_src;           //源IP         //ip_or_segment
    private String nw_dst;           //目标IP       // ip
    private int tp_dst;              //端口              port
    private int nw_proto;            //TCP/UDP         6  或 17
    private String action;           //操作      normal：放行   drop ：阻塞  (normal)

    @Override
    public String toString() {
        return "OvsBridgeParam{" +
                "bridge='" + bridge + '\'' +
                ", priority='" + priority + '\'' +
                ", nw_src='" + nw_src + '\'' +
                ", nw_dst='" + nw_dst + '\'' +
                ", tp_dst=" + tp_dst +
                ", nw_proto=" + nw_proto +
                ", action='" + action + '\'' +
                '}';
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNw_src() {
        return nw_src;
    }

    public void setNw_src(String nw_src) {
        this.nw_src = nw_src;
    }

    public String getNw_dst() {
        return nw_dst;
    }

    public void setNw_dst(String nw_dst) {
        this.nw_dst = nw_dst;
    }

    public int getTp_dst() {
        return tp_dst;
    }

    public void setTp_dst(int tp_dst) {
        this.tp_dst = tp_dst;
    }

    public int getNw_proto() {
        return nw_proto;
    }

    public void setNw_proto(int nw_proto) {
        this.nw_proto = nw_proto;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
