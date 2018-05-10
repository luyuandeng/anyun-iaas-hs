package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.CalculationSchemeDto;

import java.util.List;

public interface CalculationSchemeDao {
    CalculationSchemeDto queryCalculationSchemeInfo(String id);

    List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId);

    void deleteCalculationScheme(String id);

    CalculationSchemeDto createCalculationScheme(CalculationSchemeDto calculationSchemeDto);

}
