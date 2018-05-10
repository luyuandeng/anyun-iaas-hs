package com.anyun.esb.component.host.service;

import java.util.List;

/**
 * @author TwitchGG <ray@proxzone.com>
 * @since 1.0.0 on 31/10/2016
 */
public interface TemplateService {
    //生成nginx 配置文件
    String getNginxConfigutation(
            List<String> containersIp,
            int containerPort, int publishPort, String weightType) throws Exception;

    //生成  haproxy 配置文件
    String getHaproxyConfigutation(List<String> mysqldIps) throws Exception;


    //生成  mgm 配置文件
    String getMgmConfiguation(String mgmIp, List<String> mysqldIps) throws Exception;


    //生成  mysqld 配置文件
    String getMysqldConfiguation(String mgmIp) throws Exception;

}
