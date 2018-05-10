package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.param.TagUpdateParam;

/**
 * Created by admin on 2017/5/9.
 */
public interface TagService {
    /**
     * 查询Tag
     * @param param
     * @return PageDto<TagDto>
     * @throws Exception
     */
    PageDto<TagDto> queryByConditions(CommonQueryParam param) throws Exception;

    /**
     * 根据ID查询Tag详情
     * @param id
     * @return
     * @throws Exception
     */
    TagDto queryById(String id) throws Exception;

    /**
     * 创建TAG
     * @param param
     * @return
     * @throws Exception
     */
    TagDto createTag(TagCreateParam param) throws Exception;

    /**
     * 删除Tag
     * @param id
     * @throws Exception
     */
    void tagDeleteById(String id) throws Exception;

    /**
     * 修改Tag
     * @param param
     * @throws Exception
     */
    TagDto tagUpdate(TagUpdateParam param) throws Exception;

    /**
     * 根据ResourceId删除Tag
     */
    void tagDeleteByResourceId(String resourceId) throws Exception;
}
