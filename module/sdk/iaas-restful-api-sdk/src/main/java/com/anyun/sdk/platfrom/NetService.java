package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerIpInfoDto;
import com.anyun.cloud.dto.NetLabelInfoDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ConnectToNetParam;
import com.anyun.cloud.param.DisconnectFromNetParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by sxt on 16-7-22.
 */
public interface NetService {
    /**
     * 1、查询  网络标签详情
     *
     * @param userUniqueId String  false  用户标识
     * @param label        String  true     主键
     * @return NetLabelInfoDto>   网络标签信息
     * @throws RestfulApiException
     */
    NetLabelInfoDto getNetLabelInfoDetails(String label,String userUniqueId) throws RestfulApiException;


    /**
     * 2、查询  网络标签列表
     *
     * @param userUniqueId String  false  用户标识
     * @param type         String    true    有两种类型 openvswitch和bridge
     * @return List<NetLabelInfoDto>   网络标签信息列表 格式如   label
     * @throws RestfulApiException
     */
    List<NetLabelInfoDto> getNetLabelInfoList(String type,String userUniqueId) throws RestfulApiException;


    /**
     * 3、查询 容器ip 列表
     *
     * @param userUniqueId  String   false   用户标识
     * @param subMethod     String   true    子方法
     *                      QUERY_BY_LABEL_CONTAINER：根据容器ID和网络标签 查询 容器IP信息
     *                      container     String    true     容器id
     *                      label         String    true     网络标签
     *                      QUERY_ALREADY_ADDED_BY_LABEL： 根据安全组标签 查询安全组中  已添加的
     *                      label   String   true    安全组标签
     *                      QUERY_NOT_ADD_BY_LABEL： 查询 未添加到该安全组的  容器ip 列表
     *                      label   String   true    安全组标签
     * @param subParameters 子参数  格式如  id|label
     * @return List<ContainerIpInfoDto>
     * @throws RestfulApiException
     */
    List<ContainerIpInfoDto> getContainerIpInfoList(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;


    /**
     * 4、容器连上网络
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> containerConnectToNetwork(ConnectToNetParam param) throws RestfulApiException;


    /**
     * 5、容器断掉网络
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> containerDisconnectFromNetwork(DisconnectFromNetParam param) throws RestfulApiException;


    /**
     * 7、查询容器ip列表
     * @retuen List<ContainerIpInfoDto>
     *
     **/
    PageDto<ContainerIpInfoDto> getContainerIpList(CommonQueryParam param)throws RestfulApiException;

    /**
     * 7、查询网络标签列表
     * @retuen List<NetLabelInfoDto>
     *
     **/
    PageDto<NetLabelInfoDto> QueryNetLabelInfoList(CommonQueryParam param)throws RestfulApiException;
}
