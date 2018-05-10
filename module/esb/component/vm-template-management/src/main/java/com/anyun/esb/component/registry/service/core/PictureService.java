package com.anyun.esb.component.registry.service.core;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.PictureUploadParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 16-10-17.
 */
public interface PictureService {
    /**
     * 图片上传
     * @param param
     * @return
     * @throws Exception
     */
    PictureDto pictureUpload(PictureUploadParam param)  throws  Exception;

    List<PictureDto> pictureQueryAll()  throws  Exception;

    PageDto<PictureDto> getPageListConditions(CommonQueryParam param)throws RestfulApiException;
}
