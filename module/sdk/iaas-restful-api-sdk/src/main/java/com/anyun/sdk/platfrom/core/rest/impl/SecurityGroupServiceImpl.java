package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.SecurityGroupService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-19.
 */
public class SecurityGroupServiceImpl implements SecurityGroupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGroupServiceImpl.class);
    public static final String PATH_SECURITY_GROUP_CREATE = "/securityGroup/create";
    public static final String PATH_SECURITY_GROUP_UPDATE = "/securityGroup/update";
    public static final String PATH_SECURITY_GROUP_DELETE = "/securityGroup/delete";
    public static final String PATH_SECURITY_GROUP_DETAILS = "/securityGroup/details";
    public static final String PATH_SECURITY_GROUP_LIST = "/securityGroup/list";
    public static final String PATH_ADD_IP_TO_SECURITY_GROUP = "/securityGroup/addIpToSecurityGroup";
    public static final String PATH_REMOVE_IP_FROM_SECURITY_GROUP = "/securityGroup/removeIpFromSecurityGroup";
    public static final String PATH_SECURITY_GROUP_QUERY = "/securityGroup/list/query";
    public static final String PATH__SECURITY_GROUP_QUERY_BY_conditions = "/securityGroup/conditions";
    @Override
    public SecurityGroupDto securityGroupCreate(SecurityGroupCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_SECURITY_GROUP_CREATE, param.asJson());
        SecurityGroupDto requests = ResourceClient.convertToAnyunEntity(SecurityGroupDto.class, response);
        return requests;
    }

    @Override
    public SecurityGroupDto securityGroupUpdate(SecurityGroupUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_SECURITY_GROUP_UPDATE, param.asJson());
        SecurityGroupDto requests = ResourceClient.convertToAnyunEntity(SecurityGroupDto.class, response);
        return requests;
    }

    @Override
    public Status<String> securityGroupDeleteByLabel(String label, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_SECURITY_GROUP_DELETE + "/" + label, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public SecurityGroupDto securityGroupQueryByLabel(String label, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_SECURITY_GROUP_DETAILS + "/" + label, param);
        SecurityGroupDto requests = ResourceClient.convertToAnyunEntity(SecurityGroupDto.class, response);
        return requests;
    }

    @Override
    public List<SecurityGroupDto> securityGroupQueryByProject(String project, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_SECURITY_GROUP_LIST + "/" + project, param);
        List<SecurityGroupDto> requests = ResourceClient.convertToListObject(SecurityGroupDto.class, response);
        return requests;
    }

    @Override
    public Status<String> addIpToSecurityGroup(AddIpToSecurityGroupParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_ADD_IP_TO_SECURITY_GROUP, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> removeIpFromSecurityGroup(RemoveIpFromSecurityGroupParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_REMOVE_IP_FROM_SECURITY_GROUP, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<SecurityGroupDto> querySecurityGroup(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod", subMethod);
        param.put("subParameters", subParameters);
        String response = rsClient.query(PATH_SECURITY_GROUP_QUERY, param);
        List<SecurityGroupDto> requests = ResourceClient.convertToListObject(SecurityGroupDto.class, response);
        return requests;
    }

    @Override
    public PageDto<SecurityGroupDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH__SECURITY_GROUP_QUERY_BY_conditions+"/"+param.asJson(), new HashMap<>());
        PageDto<SecurityGroupDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
