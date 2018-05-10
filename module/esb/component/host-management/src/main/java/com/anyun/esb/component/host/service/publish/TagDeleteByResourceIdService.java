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
 * Created by admin on 2017/5/15.
 */
public class TagDeleteByResourceIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagDeleteByResourceIdService.class);
    private TagService tagService;
    public TagDeleteByResourceIdService(){
        tagService = new TagServiceImpl();
    }
    @Override
    public String getName() {
        return "tag_delete_resourceId";
    }

    @Override
    public String getDescription() {
        return "TagDeleteByResourceIdService";
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

        String resourceId = exchange.getIn().getHeader("resourceId", String.class);
        LOGGER.debug("resourceId:[{}]", resourceId);
        if (StringUtils.isEmpty(resourceId)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("resourceId is empty");
            exception.setMessage("resourceId is empty");
            exception.setUserTitle("resourceId is empty");
            throw exception;
        }

        try {
            tagService.tagDeleteByResourceId(resourceId);
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
