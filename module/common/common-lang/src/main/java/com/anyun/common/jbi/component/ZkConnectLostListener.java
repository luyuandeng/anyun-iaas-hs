package com.anyun.common.jbi.component;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/16/16
 */
public interface ZkConnectLostListener {
    void afterConnectLost(ZKConnector connector);
}
