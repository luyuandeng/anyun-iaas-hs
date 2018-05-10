package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DatabaseCreateParam;

import java.util.List;

/**
 * Created by sxt on 7/4/17.
 */
public interface DatabaseService  {
    DatabaseDto getDatabaseDtoById(String id,String userUniqueId) throws Exception;

    PageDto<DatabaseDto> getPageDtoByCondition(CommonQueryParam param) throws  Exception;

    void deleteById(String id) throws Exception;

    DatabaseDto  createDatabase(DatabaseCreateParam param) throws Exception;

    void deleteDbByProjectId(String projectId)throws Exception;

    List<DatabaseDto> selectDatabaseDtoListByProjectId(String projectId)throws Exception;
}
