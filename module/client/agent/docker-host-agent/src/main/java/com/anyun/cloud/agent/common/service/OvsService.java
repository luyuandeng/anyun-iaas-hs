package com.anyun.cloud.agent.common.service;

import com.anyun.cloud.agent.param.AddOutBridgeParam;
import com.anyun.cloud.agent.param.NetParam;
import com.anyun.cloud.agent.param.OvsBridgeParam;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.api.Status;

import java.util.List;

/**
 * Created by sugs on 16-10-25.
 */
public interface OvsService {

    /**
     * 重置OVS；
     * @param netParam
     * @return
     */
    Response<Status<Boolean>> ResetNet(NetParam netParam);

    /**
     * 添加网桥,绑定网卡
     */
    Response<Status<Boolean>> addBridge(NetParam netParam);

    /**
     * 删除网桥
     */
    Response<Status<Boolean>> delBridge(NetParam netParam);

    /**
     * 添加全局控制
     */
    Response<Status<String>> addAllBridge(String bridge);

    /**
     * 添加访问出权限
     */
    Response<Status<String>> addOutBridge(AddOutBridgeParam addOutBridgeParam);

    /**
     * 添加访问限制
     */
    Response<Status<Boolean>> addVisitLimit(String bridge,String list);

    /**
     * 删除访问限制
     */
    Response<Status<Boolean>> delVisitLimit();
}
