package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.QpidQueueDto;
import com.anyun.cloud.param.QpidQueueParam;

import java.util.List;

/**
 * Created by zy on 17-7-17.
 */
public interface QpidService {
    List<QpidQueueDto> getQueuesFromQpid(QpidQueueParam param);
}
