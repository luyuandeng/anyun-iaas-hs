package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.TagService;
import com.anyun.esb.component.host.service.docker.impl.TagServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/5/10.
 */
public class TagCreateService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagCreateService.class);
    private TagService tagService;
    public TagCreateService() {
        tagService = new TagServiceImpl();
    }

    @Override
    public String getName() {
        return "tag_create";
    }

    @Override
    public String getDescription() {
        return "TagCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        //转化参数实体类，验证参数
        TagCreateParam param = JsonUtil.fromJson(TagCreateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.get__userTag__())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("__userTag__ is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getResourceId())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("Resource_id is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getResourceType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("Resource_type() is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            return   tagService.createTag(param);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area create error [" + e + "]");
            exception.setUserMessage("Tag创建失败[" + e + "]");
            exception.setUserTitle("Tag创建失败");
            throw exception;
        }
    }
}
