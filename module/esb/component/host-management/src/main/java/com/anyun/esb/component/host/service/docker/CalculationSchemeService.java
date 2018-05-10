package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.param.CommonQueryParam;

import java.util.List;

public interface CalculationSchemeService {
    /**
     * 1.查询计算方案详情
     * @param id
     * @return
     * @throws Exception
     */
    CalculationSchemeDto queryCalculationSchemeInfo(String id) throws Exception;

    /**
     * 2.查询计算方案列表
     */
    List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId) throws Exception;

    /**
     * 3.根据条件分页查询计算方案列表
     */
    PageDto<CalculationSchemeDto> pageQueryCalculationSchemeList(CommonQueryParam commonQueryParam) throws Exception;

    /**
     * 4.删除计算方案
     */
    void deleteCalculationScheme(String id) throws Exception;

    /**
     * 5.创建计算方案
     */
    CalculationSchemeDto createCalculationScheme(CalculationSchemeCreateParam param) throws Exception;
}
