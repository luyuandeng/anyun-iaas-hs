package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.PictureUploadParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.service.core.PictureService;
import com.anyun.esb.component.registry.service.core.impl.PictureServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-17.
 */
public class PictureUploadService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureUploadService.class);
    private PictureService pictureService;

    public PictureUploadService(){
        pictureService = new PictureServiceImpl();
    }

    @Override
    public String getName(){
        return "upload_picture";
    }

    @Override
    public String getDescription(){
        return "PictureUploadService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        //获取参数进行校验
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]",postBody);
        if(StringUtils.isEmpty(postBody)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is Empty");
            exception.setUserMessage("postBody is Empty");
            exception.setUserTitle("postBody is Empty");
            throw exception;
        }
        //Json转换成对象
        PictureUploadParam param = JsonUtil.fromJson(PictureUploadParam.class,postBody);
        LOGGER.debug("param:[{}]",param.asJson());
        if(param == null){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
            throw exception;
        }
        try {
            PictureDto pictureDto = pictureService.pictureUpload(param);
            if(pictureDto == null)
                return new PictureDto();
            return pictureDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("PictureUpload fail [" + e.getMessage() + "]");
            exception.setUserMessage("[" + e.getMessage() + "]");
            exception.setUserTitle("PictureUpload fail");
            throw exception;
        }
    }
}
