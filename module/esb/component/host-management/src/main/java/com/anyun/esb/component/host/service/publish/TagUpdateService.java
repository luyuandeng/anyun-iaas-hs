package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.TagUpdateParam;
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
public class TagUpdateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagUpdateService.class);
    private TagService tagService;
    public TagUpdateService(){
        tagService = new TagServiceImpl();
    }
    @Override
    public String getName() {
        return "tag_update";
    }

    @Override
    public String getDescription() {
        return "TagUpdateService";
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
        TagUpdateParam param = JsonUtil.fromJson(TagUpdateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try{
            //验证Tag是否存在
            TagDto dto = tagService.queryById(param.getId());
            if (dto == null){
                LOGGER.debug("TagDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("TagDto is null");
                exception.setUserMessage("TagDto is null");
                exception.setUserTitle("Tag不存在");
                throw exception;
            }
            return tagService.tagUpdate(param);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Tag  error [" + e + "]");
            exception.setUserMessage("Tag更新失败[" + e + "]");
            exception.setUserTitle("Tag更新失败");
            throw exception;
        }
    }
}
