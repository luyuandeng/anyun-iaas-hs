package com.anyun.sdk.platfrom;


import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.param.AssetsUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by  on 17-10-30.
 */
public interface AssetsService {

    /**
     * 查询资产详情
     * @param id
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    AssetsDto queryById(String id,String userUniqueId) throws RestfulApiException;

    /**
     * 根据设备分类查询资产详情
     * @param deviceCategory
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    List<AssetsDto> getList(String deviceCategory, String userUniqueId)throws RestfulApiException;

    /**
     * 分页查询资产信息
     * @param param
     * @return
     * @throws RestfulApiException
     */
    PageDto<AssetsDto> getPageListConditions(CommonQueryParam param)throws RestfulApiException;

    /**
     * 删除资产信息
     * @param id
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    Status<String> deleteAssets(String id, String userUniqueId)throws RestfulApiException ;

    /**
     *批量创建资产
     * @param param
     * @return
     * @throws RestfulApiException
     */
    List<AssetsDto> createAssets(List<AssetsCreateParam> param) throws RestfulApiException ;

    /**
     * 修改资产
     * @param param
     * @return
     * @throws RestfulApiException
     */
    AssetsDto  updateArea (AssetsUpdateParam param)throws RestfulApiException ;

    /**
     * 批量删除
     * @param list
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    Status<String> deleteListAssets(List<String> list , String userUniqueId)throws RestfulApiException ;


}
