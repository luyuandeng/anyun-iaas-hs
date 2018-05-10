package com.anyun.cloud.agent.common.service;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.api.Response;

import java.util.List;

/**
 * Created by twitchgg on 16-8-3.
 */
public interface StorageService {

    /**
     * @param type
     * @return
     * @throws ApiException
     */
    Response<List<StorageInfo>> listStorageByType(String type);

    /**
     * @param json
     * @return
     */
    Response<StorageInfo> addStorage(String type, String json);
}
