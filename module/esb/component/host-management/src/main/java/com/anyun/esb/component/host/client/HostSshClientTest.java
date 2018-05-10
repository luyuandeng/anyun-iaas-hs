package com.anyun.esb.component.host.client;

import com.anyun.cloud.dto.HostNetworkCardDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.SSHFileCopy;
import com.jcraft.jsch.Session;
import javassist.scopedpool.SoftValueHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 17-3-28.
 */
public class HostSshClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostSshClientTest.class);
    private static final String USER = "root";
    private static final String PASSWORD = "1234qwer";
    private static final String HOST = "192.168.1.154";
    private static final int DEFAULT_SSH_PORT = 22;

    public static void main(String[] args) throws Exception {
//        copy  测试
        HostSshClient hostSshClient1 = new HostSshClient("root", "192.168.1.154", 22, "1234qwer");
        HostSshClient hostSshClient2 = new HostSshClient("root", "192.168.1.155", 22, "1234qwer");
        SSHFileCopy    sshFileCopy=new SSHFileCopy(hostSshClient1.getSession(),hostSshClient2.getSession());
        sshFileCopy.copy("/root/v","/root/v");

//        HostSshClient hostSshClient = new HostSshClient(USER, HOST, DEFAULT_SSH_PORT, PASSWORD);
//        String command;
//        command = "lshw   -C  network";
//        String str = hostSshClient.exec(command);
//        File configFile = File.createTempFile("network", ".conf");
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
//        bufferedWriter.write(str);
//        bufferedWriter.close();
//        LOGGER.debug(":临时文件路径[{}]", configFile.getPath());
//{
//                if (StringUtils.isNotEmpty(n.getProduct())){
////                    n.setId(StringUtils.uuidGen());
////                    l.add(n);
////                    n=null;
//                }
//            }
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
//        String tempString = null;
//        bufferedReader.readLine();
//
//        List<HostNetworkCardDto> l = new ArrayList<>();
//        HostNetworkCardDto n = new HostNetworkCardDto();;
//        int  i=0;
//        while ((tempString = bufferedReader.readLine()) != null) {
//
//            if (tempString.contains("*-network")){
//                i++;
//                continue;
//            }
//            if (tempString.contains("description:")) {
//                n.setDescription(tempString.split(":")[1].toString());
//                continue;
//            }
//
//            if (tempString.contains("product:")) {
//                n.setProduct(tempString.split(":")[1].trim());
//                continue;
//            }
//
//            if (tempString.contains("vendor:")) {
//                n.setVendor(tempString.split(":")[1].trim());
//                continue;
//            }
//
//            if (tempString.contains("logical name:")) {
//                n.setName(tempString.split(":")[1].trim());
//                continue;
//            }
//
//            if (tempString.contains("serial:")) {
//                n.setMac(tempString.split(":")[1].trim());
//                continue;
//            }
//
//            if(i==2){
//                if (StringUtils.isNotEmpty(n.getProduct())){
//                    n.setId(StringUtils.uuidGen());
//                    l.add(n);
//                    n=null;
//                }
//            }
//        }
//
//        LOGGER.debug("l:[{}]",JsonUtil.toJson(l));
//        try {//删除临时文件
//            if (configFile.exists())
//                configFile.delete();
//        } catch (Exception d) {
//            LOGGER.debug("configFile delete  fail:[{}]", d);
//        }
    }
}
