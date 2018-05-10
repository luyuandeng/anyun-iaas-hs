package com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public interface  PictureService  {
    /**
     * 查询所有图片
     *
     * @return ProjectDto
     * @throws RestfulApiException
     */
    List<PictureDto>   pictureQueryAll(String  userUniqueId)  throws  RestfulApiException;

    PageDto<PictureDto> getPageListConditions(CommonQueryParam param)throws RestfulApiException;

}
