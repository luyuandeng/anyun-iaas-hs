package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.ApplicationCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ContainerOpParam;

import java.util.List;

/**
 * Created by sxt on 16-10-25.
 */
public interface ApplicationService {
    ApplicationInfoDto applicationCreate(ApplicationCreateParam param)  throws  Exception;

    void applicationDeleteById(String id) throws  Exception;

    ApplicationInfoDto applicationQueryById(String id) throws Exception;

    List<ApplicationInfoDto> applicationQueryByProject(String project) throws Exception;

    void applicationDeleteByContainer(String id) throws Exception;

    PageDto<ApplicationInfoDto> applicationQueryByConditions(CommonQueryParam param) throws Exception;

    List<ApplicationInfoLoadDto> queryLoadByApplication(String id ) throws Exception;

    PageDto<ApplicationInfoLoadDto> applicationLoadQueryByConditions(CommonQueryParam param) throws Exception;

    /**
     * @param  id  应用Id
     * @param  amount 负载数量
     */
    List<ApplicationInfoLoadDto>  addLoad(String id, int amount) throws Exception;


    /**
     * 操作负载
     * @param id 容器Id
     * @param op 操作
     *           start：启动
     *           restart：重启
     *           stop：停止
     *           pause：暂停
     *           unpause：解除暂停
     *           delete：删除
     *           kill  : 结束进程
     */
    void operationLoad(ContainerOpParam param)throws Exception;


    /**
     * 重新发布
     * @param id 应用Id
     */
    ApplicationInfoDto  republish(String appId) throws  Exception;
}
