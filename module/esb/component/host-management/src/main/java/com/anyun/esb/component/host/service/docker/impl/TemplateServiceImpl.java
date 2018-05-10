package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.tools.FileUtil;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.common.Resources;
import com.anyun.esb.component.host.service.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.bouncycastle.asn1.x500.style.BCStyle.ST;

/**
 * @author TwitchGG <ray@proxzone.com>
 * @since 1.0.0 on 31/10/2016
 */
public class TemplateServiceImpl implements TemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateServiceImpl.class);
    private static final String TEMPLATE_NGINX = "/etc/anyuncloud/templates/user_app_template.conf";
    private static final String TEMPLATE_HAROXY="/etc/anyuncloud/templates/db_haproxy_template.conf";
    private static final String TEMPLATE_MGM="/etc/anyuncloud/templates/db_mgm_template.conf";
    private static final String TEMPLATE_MYSQLD="/etc/anyuncloud/templates/db_mysqld_template.conf";

    @Override
    public String getNginxConfigutation(List<String> containersIp, int containerPort, int publishPort, String weightType) throws Exception {
        if (containersIp == null || containersIp.isEmpty())
            throw new Exception("containers ip is not define");
        List<String> ips = containersIp.stream().map(ip -> ip
                + ":" + containerPort + ";\n").collect(Collectors.toList());
        File template = new File(TEMPLATE_NGINX);
        LOGGER.debug("Template path [{}]", template);
        String str = FileUtil.cat(template, "UTF-8");
        ST st = new ST(str);
        if (publishPort == 443) {
            st.add("http", "proxy_pass https://https_app;");
            st.add("protocol", true);
        } else {
            st.add("http", "proxy_pass http://https_app;");
            st.add("protocol", false);
        }
        st.add("weightType", weightType + " " + ";");
        st.add("servers", ips);
        st.add("port", containerPort);
        st.add("publish_port", publishPort);
        String output = st.render();
        LOGGER.debug("Configuration content [\n{}\n]", output);
        return output;
    }

    @Override
    public String getHaproxyConfigutation(List<String> mysqldIps) throws Exception {
        if (mysqldIps == null || mysqldIps.isEmpty() || mysqldIps.size() == 0)
            throw new Exception("mysqldIps is empty");
        StringBuffer sBuffer = null;
        List<String> mysqlds = new ArrayList<>();

        int i = 1;
        for (String str : mysqldIps) {
            if (StringUtils.isEmpty(str))
                continue;
            sBuffer = new StringBuffer();
            sBuffer.append("server mysql");
            sBuffer.append(i++);
            sBuffer.append(" ");
            sBuffer.append(str);
            sBuffer.append(":3306 weight 1 check inter 1s rise 2 fall 2");
            mysqlds.add(sBuffer + "\n");
        }

        File template = new File(TEMPLATE_HAROXY);
        LOGGER.debug("Template path [{}]", template);
        String str = FileUtil.cat(template, "UTF-8");
        ST st = new ST(str);
        st.add("mysqlds", mysqlds);
        String output = st.render();
        LOGGER.debug("Configuration content [\n{}\n]", output);
        return output;
    }

    @Override
    public String getMgmConfiguation(String mgmIp, List<String> mysqldIps) throws Exception {
        if (StringUtils.isEmpty(mgmIp))
            throw new Exception("mgmIp is empty");
        if (mysqldIps == null || mysqldIps.isEmpty() || mysqldIps.size() == 0)
            throw new Exception("mysqldIps is empty");
        List<String> ndbList = new ArrayList<>();
        List<String> mysqldList = new ArrayList<>();
        StringBuffer sBuffer = null;
        int i = 2;
        for (String str : mysqldIps) {
            if (StringUtils.isEmpty(str))
                continue;
            sBuffer = new StringBuffer();
            sBuffer.append("\n");
            sBuffer.append("[NDBD]\n");
            sBuffer.append("NodeId=" + i++ + "\n");
            sBuffer.append("HostName=" + str + "\n");
            sBuffer.append("DataDir=/usr/local/mysql/data");
            ndbList.add(sBuffer + "\n");
        }

        for (String str : mysqldIps) {
            sBuffer = new StringBuffer();
            if (StringUtils.isEmpty(str))
                continue;
            sBuffer.append("\n");
            sBuffer.append("[MYSQLD]\n");
            sBuffer.append("NodeId=" + i++ + "\n");
            sBuffer.append("HostName=" + str + "\n");
            mysqldList.add(sBuffer + "\n");
        }

        File template = new File(TEMPLATE_MGM);
        LOGGER.debug("Template path [{}]", template);
        String str = FileUtil.cat(template, "UTF-8");
        ST st = new ST(str);
        st.add("number",2);
        st.add("mgmIp", mgmIp);
        st.add("ndbList", ndbList);
        st.add("mysqldList", mysqldList);
        String output = st.render();
        LOGGER.debug("Configuration content [\n{}\n]", output);
        return output;
    }


    @Override
    public String getMysqldConfiguation(String mgmIp) throws Exception {
        if (StringUtils.isEmpty(mgmIp))
            throw new Exception("mgmIp is empty");
        File template = new File(TEMPLATE_MYSQLD);
        LOGGER.debug("Template path [{}]", template);
        String str = FileUtil.cat(template, "UTF-8");
        ST st = new ST(str);
        st.add("mgmIp", mgmIp);
        String output = st.render();
        LOGGER.debug("Configuration content [\n{}\n]", output);
        return output;
    }
}
