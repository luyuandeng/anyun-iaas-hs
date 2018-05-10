package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.param.TagUpdateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

/**
 * Created by admin on 2017/5/9.
 */
public interface TagService {
    /**
     * 1、查询Tag列根据条件
     * @retuen List<TagDto>
     *
     **/
    PageDto<TagDto> getListByconditions(CommonQueryParam param) throws RestfulApiException;

//    TagDto queryById(String id,String userUniqueId)throws RestfulApiException;
    /**
     * 2、创建Tag
     * @retuen TagDto
     *
     **/
    TagDto tagCreate(TagCreateParam param) throws RestfulApiException;

    /**
     * 3、删除Tag
     * @retuen Status<String>
     *
     **/
    Status<String> tagDelete(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 4.修改Tag
     */
    TagDto tagUpdate(TagUpdateParam param) throws RestfulApiException;

    /**
     * 5.根据resourceId删除Tag
     */
    Status<String> tagDeleteByResourceId(String resourceId, String userUniqueId) throws RestfulApiException;
}
