package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public interface AreaService {


    /**
     * 1、查询区域详情
     *
     * @param id           String   true
     * @param userUniqueId
     * @retuen  AreaDto
     */
      AreaDto queryById(String id,String userUniqueId)throws RestfulApiException;


    /**
     * 2、查询区域列表
     * @param      type    类型： 两种 (KVM ,Docker)   String  false
     * @param     status       状态 ： Enable(可用),Disable(禁用)   String   false
     * @retuen List<AreaDto>
     *
    **/
    List<AreaDto> getList(String type, String status,String userUniqueId)throws RestfulApiException;


    /**
     * 3、删除区域
     * @param id           String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    Status<String> deleteArea(String id, String userUniqueId)throws RestfulApiException ;



    /**
     * 4、创建区域
     *
     * @param  param
     * @retuen  Status<String>
     */
    Status<String> createArea(AreaCreateParam param) throws RestfulApiException ;

    /**
     * 5、修改区域
     *
     * @param  param
     * @retuen Status<String>
     */
    Status<String>  updateArea (AreaUpdateParam param)throws RestfulApiException ;


    /**
     * 6、修改状态
     *
     * @param  param
     * @retuen Status<String>
     */
    Status<String> changeAreaStatus(AreaUpdateParam param)throws RestfulApiException ;


    /**
     * 7、查询区域列根据条件
     * @retuen List<AreaDto>
     *
     **/
    PageDto<AreaDto>  getPageListConditions(CommonQueryParam param)throws RestfulApiException;
}
