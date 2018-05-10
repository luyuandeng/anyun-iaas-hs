package com.anyun.esb.component.host.service.docker;

import java.util.List;

/**
 * Created by twitchgg on 16-7-19.
 */
public interface DockerHostService {
    List<String> findAllActiveHosts() throws Exception;
}
