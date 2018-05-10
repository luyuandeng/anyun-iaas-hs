package com.anyun.esb.component.host.service.docker;


import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.param.AssetsUpdateParam;

import java.util.List;

public interface AssetsService {
    /**
     * 1、查询资产详情
     *
     * @param id
     * @retuen
     */
    AssetsDto QueryAssetsInfo(String id) throws Exception;


    /**
     * 2、根据设备分类查询资产列表
     *
     * @param deviceCategory String  true   分类：  SERVER服务器  ROUTER路由器  SWITCH交换机  OTHER其他设备   ALL:所有
     * @retuen
     */
    List<AssetsDto> QueryAssetsBydeviceCategory(String deviceCategory) throws Exception;


    /**
     * 3,根据条件分页查询资产列表

    @GET
    @Path("/base/conditions/{s}")
    @Produces("application/json")
     */
    PageDto<AssetsDto> QueryAssetsByCondition (CommonQueryParam commonQueryParam) throws Exception;


    /**
     * 4、删除资产
     *
     * @param id           String   true
     * @retuen Status<String>
     */
    void deleteAsserts(String id) throws Exception;

    List<AssetsDto> createAssets(List<AssetsCreateParam> param) throws Exception;

    AssetsDto updateAsserts(AssetsUpdateParam param) throws Exception;
}
