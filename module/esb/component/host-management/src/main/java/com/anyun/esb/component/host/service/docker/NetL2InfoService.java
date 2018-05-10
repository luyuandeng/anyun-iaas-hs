package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2CreateParam;
import com.anyun.cloud.param.NetL2UpdateParam;

import java.util.List;

/**
 * Created by sxt on 17-4-10.
 */
public interface NetL2InfoService  {
    NetL2InfoDto queryNetL2InfoById(String id) throws Exception;

    List<NetL2InfoDto> getNetL2InfoListByType(String type) throws Exception;

    void netL2DeleteById(String id) throws Exception;

    NetL2InfoDto createNetL2(NetL2CreateParam param) throws Exception;

    NetL2InfoDto updateNetL2(NetL2UpdateParam param);
}
