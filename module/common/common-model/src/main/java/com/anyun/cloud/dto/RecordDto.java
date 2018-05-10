package com.anyun.cloud.dto;

/**
 * Created by sxt on 16-6-21.
 */
public class RecordDto {
    private String  id;
    private String  operationLimit;
    private String  operationTargetInfo;
    private String  sdkName;
    private String  serviceClassPath;
    private String  requestParam;
    private String  responseParam;
    private Boolean operationResult;
    private String  errorInfo;
    private String  recordCreateDate;
    private String  remarks;
    private String  operator;

    public String getSdkName() {
        return sdkName;
    }

    public void setSdkName(String sdkName) {
        this.sdkName = sdkName;
    }

    public String getRecordCreateDate() {
        return recordCreateDate;
    }

    public void setRecordCreateDate(String recordCreateDate) {
        this.recordCreateDate = recordCreateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationLimit() {
        return operationLimit;
    }

    public void setOperationLimit(String operationLimit) {
        this.operationLimit = operationLimit;
    }

    public String getOperationTargetInfo() {
        return operationTargetInfo;
    }

    public void setOperationTargetInfo(String operationTargetInfo) {
        this.operationTargetInfo = operationTargetInfo;
    }


    public String getServiceClassPath() {
        return serviceClassPath;
    }

    public void setServiceClassPath(String serviceClassPath) {
        this.serviceClassPath = serviceClassPath;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getResponseParam() {
        return responseParam;
    }

    public void setResponseParam(String responseParam) {
        this.responseParam = responseParam;
    }

    public Boolean getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(Boolean operationResult) {
        this.operationResult = operationResult;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
