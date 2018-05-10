package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ComposeDto;
import com.anyun.cloud.param.ComposeCreateParam;

import java.util.List;

/**
 * Created by sxt on 16-12-6.
 */
public interface ComposeService {
    void deleteComposeById(String userUniqueId, String id) throws Exception;

    List<ComposeDto> queryComposeByUserUniqueId(String userUniqueId) throws Exception;

    void createCompose(ComposeCreateParam param) throws Exception;

    ComposeDto queryComposeById(String userUniqueId, String id) throws Exception;
}
