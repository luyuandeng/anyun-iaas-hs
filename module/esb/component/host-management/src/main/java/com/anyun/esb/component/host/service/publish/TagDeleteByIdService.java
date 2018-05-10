package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
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
public class TagDeleteByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDeleteByIdService.class);
    private TagService tagService;
    public TagDeleteByIdService(){
        tagService = new TagServiceImpl();
    }
    @Override
    public String getName() {
        return "tag_delete";
    }

    @Override
    public String getDescription() {
        return "TagDeleteByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("id is empty");
            exception.setMessage("id is empty");
            exception.setUserTitle("id is empty");
            throw exception;
        }

        try {
            tagService.tagDeleteById(id);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("delete Tag fail");
            exception.setMessage("delete Tag  error" + e.getMessage());
            exception.setUserTitle("Tag删除失败");
            throw exception;
        }
    }
}
