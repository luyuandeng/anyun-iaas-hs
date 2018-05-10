package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostEntityNetworkCardDto;
import com.anyun.cloud.dto.HostTailDto;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.SSHFileCopy;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.HostService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 17-3-28.
 */
public class HostServiceImpl implements HostService {
    private static final Logger LOGGER= LoggerFactory.getLogger(HostServiceImpl.class);
    private static DockerHostService dockerHostService;
    private static HostBaseInfoDao hostBaseInfoDao;
    private static final String BRANYUN="br-anyun";
    private static final String BRGA="br-bond";
    private String managementNetworkCard = null;

    public HostServiceImpl(){
        dockerHostService = new DockerHostServiceImpl();
        hostBaseInfoDao=new HostBaseInfoDaoImpl();
    }

    @Override
    public HostTailDto createHost(HostCreateParam param) throws Exception {
//        List<String> activeHosts = dockerHostService.findAllActiveHosts();
//        if(activeHosts == null || activeHosts.isEmpty()){
//            throw new Exception("Could not find active hosts");
//        }
//        String serialNumber = activeHosts.get(0).split(":")[0].trim();
//        LOGGER.debug("Find host serial number [{}]", serialNumber);
//        HostSshClient hostSshClient = Env.getSshClient(activeHosts.get(0));
//        if (hostSshClient == null) {
//            throw new Exception("Could not find active host [" + serialNumber + "] ssh client");
//        }
//        HostSshClient newHostSshClient= new HostSshClient(param.getUsername(),param.getHostip(),param.getPort(),param.getPassword());
        /*SSHFileCopy sshFileCopy= new SSHFileCopy(hostSshClient.getSession(),newHostSshClient.getSession());

        hostSshClient.exec("zip -r /var/lib/anyuncloud-agent.zip /var/lib/anyuncloud-agent/");
        sshFileCopy.copy("/var/lib/anyuncloud-agent.zip","/var/lib/");
        hostSshClient.exec("rm -f /var/lib/anyuncloud-agent.zip");

        hostSshClient.exec("zip -r /root/.ssh/ssh.zip /root/.ssh/");
        sshFileCopy.copy("/root/.ssh/ssh.zip","/root/.ssh/");
        hostSshClient.exec("rm -f /root/.ssh/ssh.zip");

//        hostSshClient.exec("zip -r /etc/anyuncloud.zip /etc/anyuncloud/");
//        sshFileCopy.copy("/etc/anyuncloud.zip","/etc/");
//        hostSshClient.exec("rm -f /etc/anyuncloud.zip");

//        hostSshClient.exec("zip -r /opt/jvm.zip /opt/jvm/");
//        sshFileCopy.copy("/opt/jvm.zip","/opt/");
//        hostSshClient.exec("rm -f /opt/jvm.zip");


        newHostSshClient.exec("rm -rf /var/lib/anyuncloud-agent/");
        newHostSshClient.exec("unzip /var/lib/anyuncloud-agent.zip -d /var/lib/");
        newHostSshClient.exec("rm -f /var/lib/anyuncloud-agent.zip");
        newHostSshClient.exec("mv /var/lib/var/lib*//* /var/lib/");
        LOGGER.debug("unzip anyuncloud-agent success");
        newHostSshClient.exec("rm -rf /var/lib/var");

        newHostSshClient.exec("unzip -jo /root/.ssh/ssh.zip -d /root/.ssh/");
        LOGGER.debug("unzip ssh success");
        newHostSshClient.exec("rm -f /root/.ssh/ssh.zip");

//        newHostSshClient.exec("mkdir -p /etc/anyuncloud");
//        newHostSshClient.exec("unzip -j /etc/anyuncloud.zip -d /etc/anyuncloud/");
//        LOGGER.debug("unzip anyuncloud success");
//        newHostSshClient.exec("rm -f /etc/anyuncloud.zip");

//        newHostSshClient.exec("unzip /opt/jvm.zip -d /opt/");
//        newHostSshClient.exec("rm -f /opt/jvm.zip");
//        newHostSshClient.exec("mv /opt/opt*//* /opt");
//        newHostSshClient.exec("rm -rf /opt/opt");
        //初始化ovs集群
        hostInitializeOvsCluster(newHostSshClient,param.getHostip());

        //创建ovs网桥绑定公安网卡
        hostBuildInternetBridge(hostSshClient,newHostSshClient,param.getHostip());

        //初始化存储
//        hostInitializeStorage(newHostSshClient);

        //docker从zk集群中获取配置文件
        hostDockerAddZookeeper(newHostSshClient,sshFileCopy);

        String nameServer = hostSshClient.exec("cat /etc/resolv.conf");
        //添加DNS域名服务器
        hostAddDns(newHostSshClient,nameServer);

        //初始化zookeeper
        newHostSshClient.exec("rm -f /etc/anyuncloud/system.properties");
        newHostSshClient.exec("touch /etc/anyuncloud/system.properties");
        newHostSshClient.exec("echo path.net.interface.management="+managementNetworkCard+" "+">> /etc/anyuncloud/system.properties");
        newHostSshClient.exec("echo zookeeper.connection.string=zk1.anyuncloud.com:2181,zk2.anyuncloud.com:2181,zk3.anyuncloud.com:2181 >> /etc/anyuncloud/system.properties");
        newHostSshClient.exec("echo zookeeper.connection.timeout=3000 >> /etc/anyuncloud/system.properties");
        */
        HostSshClient newHostSshClient= new HostSshClient(param.getUsername(),param.getHostip(),param.getPort(),param.getPassword());
        try{
            String result=newHostSshClient.exec("pgrep java");
            LOGGER.debug("-------agent had  started");
        }catch(Exception e){
            LOGGER.debug(e.getMessage());
            newHostSshClient.exec("service hostinit restart");
            LOGGER.debug("-------agent start success!");
        }finally {
            Thread.currentThread().sleep(5000);//毫秒
            LOGGER.debug("1-------------------------------------Delayed 5s");
            LOGGER.debug("");
            HostTailDto hostTailDto= hostBaseInfoDao.selectHostInfoByDescription(param.getHostip());
            if(hostTailDto==null){
                Thread.currentThread().sleep(4000);//毫秒
                LOGGER.debug("2-------------------------------------Delayed 4s");
                hostTailDto=hostBaseInfoDao.selectHostInfoByDescription(param.getHostip());
                if(hostTailDto==null){
                    Thread.currentThread().sleep(3000);//毫秒
                    LOGGER.debug("3-------------------------------------Delayed 3s");
                    hostTailDto=hostBaseInfoDao.selectHostInfoByDescription(param.getHostip());
                    if(hostTailDto==null){
                        Thread.currentThread().sleep(2000);//毫秒
                        LOGGER.debug("4-------------------------------------Delayed 2s");
                        hostTailDto=hostBaseInfoDao.selectHostInfoByDescription(param.getHostip());
                        if(hostTailDto==null){
                            Thread.currentThread().sleep(3000);//毫秒
                            LOGGER.debug("5-------------------------------------Delayed 1s");
                            if(hostTailDto==null){
                                throw new Exception("----------time out!");
                            }
                        }
                    }
                    hostBaseInfoDao.updateHostTail(param);
                }
                hostBaseInfoDao.updateHostTail(param);
            }
            hostBaseInfoDao.updateHostTail(param);
            return  hostTailDto;
        }
    }








    @Override
    public void hostAddDns(HostSshClient hostSshClient,String nameServer) throws Exception {
        hostSshClient.exec("echo \""+nameServer+"\" > /etc/resolv.conf");
    }

    @Override
    public void hostBuildInternetBridge(HostSshClient hostSshClient,HostSshClient newHostSshClient,String hostIp) throws Exception {
        String label=hostBaseInfoDao.selectLabelByType();
//        List<HostEntityNetworkCard> list = getHostNetworkCardDtoList(newHostSshClient);
//        LOGGER.debug("list:[{}]",list);
//        for (HostEntityNetworkCard henc : list){
//            newHostSshClient.exec("ifconfig "+henc.getName()+" up");
//        }
        List<String> cardList=new ArrayList<>();
        List<HostEntityNetworkCardDto> l=getHostNetworkCardDtoList(newHostSshClient);
        for (int i=0;i<l.size();i++){
            if (l.get(i).getConfiguration().contains(hostIp)){
                managementNetworkCard=l.get(i).getName();
                l.remove(i);
            }
        }
        LOGGER.debug("l:[{}]",JsonUtil.toJson(l));
        for (int i=0;i<l.size();i++){
            if (l.get(i).getSpeed()==null){
                l.remove(i);
                i--;
            }
        }
        for (HostEntityNetworkCardDto h : l){
            LOGGER.debug("speed:[{}]",h.getSpeed());
            if (h.getSpeed().equals("10000Mbit/s")||h.getSpeed().equals("10Gbit/s")){
                cardList.add(h.getName());
            }
        }
        LOGGER.debug("l:[{}]",JsonUtil.toJson(l));
        if (cardList!=null&&!cardList.isEmpty()){
            if(networkLabelExist(label,"bridge",newHostSshClient)==false){
                String subnet_gateway = hostSshClient.exec("docker network inspect" + " " + label);
                String string = subnet_gateway.replace("[", "");
                String s = string.replace("]", "");
                JSONObject jsonObject = JSONObject.fromObject(s);
                String t = jsonObject.getString("IPAM");
                jsonObject = JSONObject.fromObject(t);
                String ss = jsonObject.getString("Config");
                jsonObject = JSONObject.fromObject(ss);
                newHostSshClient.exec("docker network create --internal --subnet="+jsonObject.getString("Subnet")+" --gateway="+jsonObject.getString("Gateway")+" -d bridge"+" "+label);
            }
            String result=newHostSshClient.exec("ifconfig | grep \"br-\"");
            String[] strs=result.split(" ");
            String br_id=strs[0];
            newHostSshClient.exec("ifconfig "+br_id+" 0.0.0.0");

            String br_bond=newHostSshClient.exec("lshw   -C  network |grep  \"logical name\" |awk -F ' ' '{printf \"%s \\n\", $3}'");
            if(!br_bond.contains(BRGA)){
                newHostSshClient.exec("ovs-vsctl add-br "+BRGA);
            }
            String ovsBridgePort=newHostSshClient.exec("ovs-vsctl list-ports "+BRGA);
            if (!ovsBridgePort.contains("bond0")){
                newHostSshClient.exec("ovs-vsctl add-bond "+BRGA+" "+"bond0"+" "+cardList.get(0)+" "+cardList.get(1)+" "+"bond_mode=balance-slb");
//                newHostSshClient.exec("ifconfig "+BRGA+" "+"15.16.9.20/24;route add default gw 15.16.9.254;");
                newHostSshClient.exec("ovs-vsctl set port bond0 lacp=active");
                newHostSshClient.exec("ovs-vsctl set port bond0 other_config:lacp-time=fast");
            }
            if (!ovsBridgePort.contains(br_id)){
                newHostSshClient.exec("ovs-vsctl add-port "+BRGA+" "+br_id);
            }
        }else{
            if(networkLabelExist(label,"bridge",newHostSshClient)==false){
                String subnet_gateway = hostSshClient.exec("docker network inspect" + " " + label);
                String string = subnet_gateway.replace("[", "");
                String s = string.replace("]", "");
                JSONObject jsonObject = JSONObject.fromObject(s);
                String t = jsonObject.getString("IPAM");
                jsonObject = JSONObject.fromObject(t);
                String ss = jsonObject.getString("Config");
                jsonObject = JSONObject.fromObject(ss);
                newHostSshClient.exec("docker network create --internal --subnet="+jsonObject.getString("Subnet")+" --gateway="+jsonObject.getString("Gateway")+" -d bridge"+" "+label);
            }
            String result=newHostSshClient.exec("ifconfig | grep \"br-\"");
            String[] strs=result.split(" ");
            String br_id=strs[0];
            newHostSshClient.exec("ifconfig "+br_id+" 0.0.0.0");

            String br_anyun=newHostSshClient.exec("lshw   -C  network |grep  \"logical name\" |awk -F ' ' '{printf \"%s \\n\", $3}'");
            if(!br_anyun.contains(BRANYUN)){
                newHostSshClient.exec("ovs-vsctl add-br "+BRANYUN);
            }
            String ovsBridgePort=newHostSshClient.exec("ovs-vsctl list-ports "+BRANYUN);
            if (!ovsBridgePort.contains(l.get(0).getName())){
                newHostSshClient.exec("ovs-vsctl add-port "+BRANYUN+" "+l.get(0).getName());
            }
            if (!ovsBridgePort.contains(br_id)){
                newHostSshClient.exec("ovs-vsctl add-port "+BRANYUN+" "+br_id);
            }
        }

    }

    @Override
    public void hostDockerAddZookeeper(HostSshClient hostSshClient,SSHFileCopy copy) throws Exception {
        hostSshClient.exec("systemctl stop docker");
        hostSshClient.exec("rm -f /lib/systemd/system/docker.service");
        copy.copy("/lib/systemd/system/docker.service","/lib/systemd/system/");
        hostSshClient.exec("systemctl daemon-reload");
        hostSshClient.exec("systemctl start docker");


    }

    @Override
    public void hostInitializeOvsCluster(HostSshClient hostSshClient,String hostip) throws Exception {
        String command = "ovs-vsctl set Open_vSwitch . external_ids:ovn-remote=\"tcp:192.168.1.117:6640\" \\"
                        + "external_ids:ovn-encap-ip="+hostip+" external_ids:ovn-encap-type=\"geneve\"";
        hostSshClient.exec(command);
        hostSshClient.exec("/usr/share/openvswitch/scripts/ovn-ctl start_controller");
        hostSshClient.exec("ovn-docker-overlay-driver --detach");
    }

    @Override
    public void hostInitializeStorage(HostSshClient hostSshClient) throws Exception {
        hostSshClient.exec("modprobe fuse");
        String string = hostSshClient.exec("dmesg | grep -i fuse");
        System.out.println(string);
        if(string.contains("fuse init (API version")){
            hostSshClient.exec("apt install glusterfs-common glusterfs-client");
            hostSshClient.exec("mkdir -p /storage/docker/volumes");
//            hostSshClient.exec("apt-get update");
//            hostSshClient.exec("apt-get install attr");
//            String result=hostSshClient.exec("mount -t glusterfs 192.168.1.151:/test-volume /storage/docker/volumes");
//            LOGGER.debug("[{}]",result);
//            hostSshClient.exec("umount /storage/docker/volumes");
        }else{
            throw new Exception("fuse init failed");
        }
    }

    @Override
    public void updateHostStatus(HostStatusUpdateParam param) throws Exception {
        hostBaseInfoDao.updateHostByIp(param);
    }

    @Override
    public HostTailDto selectHostInfoByDescription(String ip) throws Exception {
        HostTailDto dto =hostBaseInfoDao.selectHostInfoByDescription(ip);
        return dto;
    }

    @Override
    public void deleteHost(String id,String ip) throws Exception {
        HostSshClient hostSshClient=new HostSshClient("root",ip,22,"1234qwer");
        try{
            String result=hostSshClient.exec("pgrep java");
            List<String> str = StringUtils.readStringLines(result);
            for(String s:str){
                hostSshClient.exec("kill -9"+" "+s);
            }
        }catch (Exception e){
            LOGGER.debug("------"+e.getMessage());
            LOGGER.debug("------agent had stop");
        }
        LOGGER.debug("agent  stop success");
        hostBaseInfoDao.deleteFromHostInfoManagementIp(id);
        hostBaseInfoDao.deleteFromHostNetworkCard(id);
        hostBaseInfoDao.deleteFromHostInfoExt(id);
        hostBaseInfoDao.deleteFromDockerHostInfo(id);
        hostBaseInfoDao.deleteFromHostInfoBase(id);
    }

    @Override
    public List<HostBaseInfoDto> queryHostByCluster(String id) throws Exception {
        return hostBaseInfoDao.selectHostByCluster(id);
    }

    private String getNetworkLabel(String id, String type,HostSshClient hostSshClient) throws Exception {
        String command = "docker network ls | grep " + type + " | awk -F ' ' '{printf \"%s:%s\\n\", $3,$2}'";
        LOGGER.debug("find docker network label cmd [{}]", command);
        String result = hostSshClient.exec(command);
        LOGGER.debug("Network list result [{}]", result);
        List<String> labels = StringUtils.readStringLines(result);
        for (String label : labels) {
            LOGGER.debug("OVN docker network label [{}]", label);
            String _label = type + ":" + id;
            LOGGER.debug("label [{}] | [{}]", _label, label);
            if (label.equals(_label)) {
                return _label;
            }
        }
        return null;
    }

    private boolean networkLabelExist(String label, String type,HostSshClient hostSshClient) throws Exception {
        Boolean flag = true;
        try {
            String _label = getNetworkLabel(label, type,hostSshClient);
            if (StringUtils.isNotEmpty(_label))
                flag = true;
            else
                flag = false;
        } catch (Exception e) {
            LOGGER.warn("find network label error", e);
            flag = false;
        } finally {
            LOGGER.debug("flag:[{}]", flag);
            return flag;
        }
    }

    private List<HostEntityNetworkCardDto> getHostNetworkCardDtoList(HostSshClient hostSshClient) throws Exception {
        String command = "lshw   -C  network   | awk  -F ''  'BEGIN {print \"begin\"}  {print $0 } END {print \"*-network\"}'\n ";
        String str = hostSshClient.exec(command).trim();
        File configFile = File.createTempFile("network", ".conf");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write(str);
        bufferedWriter.close();
        LOGGER.debug(":临时文件路径[{}]", configFile.getPath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
        String tempString = null;
        List<HostEntityNetworkCardDto> l = new ArrayList<>();
        HostEntityNetworkCardDto n = new HostEntityNetworkCardDto();
        while ((tempString = bufferedReader.readLine()) != null) {
            if (tempString.contains("*-network")) {
                if (StringUtils.isNotEmpty(n.getProduct())) {
                    l.add(n);
                }
                n = new HostEntityNetworkCardDto();
                continue;
            }

            if (tempString.contains("product:")) {
                n.setProduct(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("logical name:")) {
                n.setName(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("configuration:")) {
                n.setConfiguration(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("size:")) {
                n.setSpeed(tempString.split(":")[1].trim());
                continue;
            }

        }
        LOGGER.debug("l:[{}]", JsonUtil.toJson(l));
        if (configFile.exists()) {
            configFile.delete();
        }
        return l;
    }
}
