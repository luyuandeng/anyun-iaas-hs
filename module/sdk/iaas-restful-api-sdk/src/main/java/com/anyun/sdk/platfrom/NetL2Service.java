package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2CreateParam;
import com.anyun.cloud.param.NetL2UpdateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 17-4-10.
 */
public interface NetL2Service {
    /**
     * 1、查询L2网络详情
     *
     * @param userUniqueId String  false  用户标识
     * @param id        String  true     主键
     * @return NetL2InfoDto>   L2网络
     * @throws NetL2InfoDto
     */
    NetL2InfoDto getDetails(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 2、查询L2网络列表
     *
     * @param userUniqueId
     * @param type         String      true     L2网络类型： DOCKER  只有一种类型
     * @retuen List<NetL2InfoDto>
     */
    List<NetL2InfoDto> getNetL2InfoList(String type, String userUniqueId) throws RestfulApiException;

    /**
     * 3、删除L2网络
     *
     * @param id           主键  String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    Status<String> netL2DeleteById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 4、添加L2网络
     *
     * @param param
     * @retuen NetL2InfoDto
     */
    NetL2InfoDto createNetL2(NetL2CreateParam param,String userUniqueId) throws RestfulApiException;

    /**
     * 5、修改L2网络
     *
     * @param param
     * @retuen NetL2InfoDto
     */
    NetL2InfoDto updateNetL2 (NetL2UpdateParam param,String userUniqueId)throws RestfulApiException ;
}
