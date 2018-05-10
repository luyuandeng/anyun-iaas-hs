package test.com.anyun.host;

import com.anyun.cloud.dto.IpDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.HostCommon;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-11-10.
 */
public class TestOther  extends BaseComponentTest {

    @Test
    public void  calculateNetworkAddress() throws Exception{
        String  subnet="192.168.1.0/24";
        if(StringUtils.isEmpty(subnet))
            throw new Exception("subnet is empty");
        if(subnet.indexOf("/")==-1)
            throw new Exception("The format of the parameter is incorrect");
        String[]  array=subnet.split("/");
        if(array.length!=2)
            throw new Exception("Array exception");
        String   n=array[0];
        String   s=array[1];

           System.out.println( Integer.valueOf("11111111",2));

       /* String   startIp="192.168.1.1";
        String   endIp="192.168.1.254";
        List<IpDto>   l= new ArrayList<>();
        Inet4Address start = (Inet4Address) Inet4Address.getByName(startIp);
        Inet4Address end = (Inet4Address) Inet4Address.getByName(endIp);
        List<byte[]> list = HostCommon.getRangeIps(null, start.getAddress(), end.getAddress());
        int  i=0;
        for (byte[] bs : list) {
            String ip= InetAddress.getByAddress(bs).getHostAddress();
            IpDto  ipDto=new IpDto();
            ipDto.setIp(ip);
            l.add(ipDto);
            i++;
            System.out.println( "第 "+ i +" 个 ip:--->"+ipDto.getIp());
        }*/
    }

    @Test
    public   void    images(){
//        Jedis jedis = Env.env(Jedis.class);
//        String path = jedis.get("com.anyun.docker.registry.path.storage");
        String REGISTRY_PATH = "/data/docker/registry/v2/repositories";
        String path="";
        path +=  REGISTRY_PATH;
        StringBuilder sb = new StringBuilder();
        sb.append("find " + path);
        sb.append(" -type d");
        sb.append(" | grep '/tags/' ");
        sb.append(" | grep -v 'index' ");
        sb.append(" | grep -v '/current' ");
        sb.append(" | sed 's/\\.\\//''/g' ");
        sb.append(" | sed 's/\\/_manifests\\/tags\\//':'/g'");
        String command = sb.toString();
        System.out.println(command);

    }



}
