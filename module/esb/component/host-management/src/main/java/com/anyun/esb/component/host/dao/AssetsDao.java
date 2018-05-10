package com.anyun.esb.component.host.dao;


import com.anyun.cloud.dto.AssetsDto;

import java.util.List;

public interface AssetsDao {
    /**
     * 1、查询资产详情
     *
     * @param id
     * @retuen
     */
    AssetsDto QueryAssetsInfo(String id);


    /**
     * 2、根据设备分类查询资产列表
     * @param deviceCategory String  true   分类：  SERVER服务器  ROUTER路由器  SWITCH交换机  OTHER其他设备   ALL:所有
     * @retuen
     */
    List<AssetsDto> QueryAssetsBydeviceCategory(String deviceCategory);
    List<AssetsDto> QueryAssetsAll(String deviceCategory);


    /**
     * 4、删除资产
     *
     * @param id String   true
     * @retuen Status<String>
     */
     void deleteAsserts(String id) ;

    List<AssetsDto> createAssets(AssetsDto assetsDto) throws Exception;

    AssetsDto updateAssets(AssetsDto assetsDto) throws Exception;

}
