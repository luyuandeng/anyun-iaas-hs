package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DatabaseCreateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;


/**
 * Created by sxt on 7/4/17.
 */
public interface DatabaseService {

    /**
     * 1、获取数据库详情
     *
     * @param id           主键
     * @param userUniqueId 用户唯一标识
     * @return DatabaseDto
     * @throws RestfulApiException
     */
    DatabaseDto getDetails(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 2、 分页查询数据库列表
     *
     * @param param
     * @throws RestfulApiException
     * @retuen PageDto <DatabaseDto>
     */
    PageDto<DatabaseDto> getPageListByCondition(CommonQueryParam param) throws RestfulApiException;


    /**
     * 3、 删除数据库
     *
     * @param id           主键
     * @param userUniqueId 用户唯一标识
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> delete(String id, String userUniqueId) throws RestfulApiException;


    /**
     * 4、创建数据库
     *
     * @param param
     * @return DatabaseDto
     * @throws RestfulApiException
     */
    DatabaseDto create(DatabaseCreateParam param) throws RestfulApiException;
}

