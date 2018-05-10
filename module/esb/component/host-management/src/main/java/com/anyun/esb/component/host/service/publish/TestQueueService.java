package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.QpidQueueDto;
import com.anyun.cloud.param.QpidQueueParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.QpidService;
import com.anyun.esb.component.host.service.docker.impl.QpidServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zy on 17-5-3.
 */
public class TestQueueService extends AbstractBusinessService{
    private static final Logger LOGGER= LoggerFactory.getLogger(TestQueueService.class);
    private QpidService qpidService;

    public TestQueueService(){qpidService=new QpidServiceImpl();}

    @Override
    public String getName() {
        return "get_queue_from_qpid";
    }

    @Override
    public String getDescription() {
        return "get queue from qpid service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String body=exchange.getIn().getBody(String.class);
        LOGGER.debug("body:[{}]",body);
        if (StringUtils.isEmpty(body)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("body is empty");
            exception.setUserMessage("body is empty");
            exception.setUserTitle("body is empty");
            throw exception;
        }
        QpidQueueParam param;

        try {
            param=JsonUtil.fromJson(QpidQueueParam.class,body);
            LOGGER.debug("param:[{}]", param.asJson());
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("格式 转换 异常：[{" + e.getMessage() + "}]");
            exception.setUserMessage("格式 转换 异常");
            exception.setUserTitle("格式 转换 异常");
            throw exception;
        }
        try{
            List<QpidQueueDto> queueDtoList= qpidService.getQueuesFromQpid(param);
            LOGGER.debug("list:[{}]",queueDtoList);
            return queueDtoList;
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("host  error [" + e + "]");
            exception.setUserMessage("宿主机删除失败[" + e + "]");
            exception.setUserTitle("宿主机删除失败");
            throw exception;
        }
    }
}
