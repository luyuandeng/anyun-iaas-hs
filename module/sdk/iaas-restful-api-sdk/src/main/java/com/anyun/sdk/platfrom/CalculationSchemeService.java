package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by twh-workspace on 17-11-23.
 */
public interface CalculationSchemeService {

    /**
     * 1、查询磁盘方案详情
     *
     * @param id
     * @retuen DiskSchemeDto
     */

    CalculationSchemeDto queryCalculationSchemeByid(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 2、查询计算方案列表
     *
     * @retuen List<DiskSchemeDto>
     */

    List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId) throws RestfulApiException;


    /**
     * 3,根据条件分页查询磁盘方案列表
     */

    PageDto<CalculationSchemeDto> queryCalculationSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws RestfulApiException;


    /**
     * 4、删除磁盘方案
     *
     * @param id
     * @retuen Status<String>
     */

    Status<String> deleteCalculationSchemeById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 5、创建磁盘方案
     *
     * @param
     * @retuen DiskSchemeDto
     */
    CalculationSchemeDto createCalculationScheme(CalculationSchemeCreateParam param) throws RestfulApiException;
}
