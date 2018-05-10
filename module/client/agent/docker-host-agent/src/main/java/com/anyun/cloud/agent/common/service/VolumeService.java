package com.anyun.cloud.agent.common.service;

import com.anyun.cloud.agent.result.Status;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.param.MountVolumeParam;
import com.anyun.cloud.param.UmountVolumeParam;
import com.anyun.cloud.param.UpdateVolumeParam;
import com.anyun.cloud.param.VolumeCreateParamDocker;

/**
 * Created by twitchgg on 16-8-10.
 */
public interface VolumeService {

    /**
     * 创建并格式化卷
     * @param param
     * @return
     */
    Response<Status<String>> createVolume(VolumeCreateParamDocker param);

    /**
     * 挂载卷
     * @param param
     * @return
     */
    Response<Status<Boolean>> mountVolume(MountVolumeParam param);

    /**
     * 卸载卷
     */
    Response<Status<Boolean>> umountVolume(UmountVolumeParam param);

    /**
     * 修改卷大小
     */
    Response<Status<Boolean>> updateVolume(UpdateVolumeParam param);

    /**
     * 删除卷
     */
    Response<Status<Boolean>> deleteVolume(String id);

    /**
     * 判断卷是否存在
     */
    Response<Status<Boolean>> exitVolume(String id);
}
