package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;

/**
 * Created by twitchgg on 16-8-3.
 */
public interface BaseLinuxCommandExecuter <T>{
    T exec() throws ApiException;
}
