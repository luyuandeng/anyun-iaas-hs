package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.param.ApplicationCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.HostCommon;
import com.anyun.esb.component.host.service.publish.ApplicationCreateService;
import com.anyun.esb.component.host.service.publish.ApplicationDeleteByIdService;
import com.anyun.esb.component.host.service.publish.ApplicationQueryByIdService;
import com.anyun.esb.component.host.service.publish.ApplicationQueryByProjectService;
import org.junit.Before;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-25.
 */
public class TestApplication extends BaseComponentTest {
    private ApplicationCreateService applicationCreateService;
    private ApplicationDeleteByIdService applicationDeleteByIdService;
    private ApplicationQueryByIdService applicationQueryByIdService;
    private ApplicationQueryByProjectService applicationQueryByProjectService;

    @Before
    public void setup() throws Exception {
        applicationCreateService = new ApplicationCreateService();
        applicationDeleteByIdService = new ApplicationDeleteByIdService();
        applicationQueryByIdService = new ApplicationQueryByIdService();
        applicationQueryByProjectService = new ApplicationQueryByProjectService();
    }

    @Test
    public void testcontainerConnectToNetworkIP() throws Exception {
        String ips = "25.253.22.255";

        if (StringUtils.isEmpty(ips)) {
            throw new Exception("Ip is empty");
        }
        //判斷IP 格式
        //TODO
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//IP正则表达式
        if (!ips.matches(regex)) {
            throw new Exception("Ip is ERROR IP");
        }
    }

    @Test
    public void testqueryAvailableIpList() throws Exception {
//        String subnet = "192.168.1.0/24";
        /*List<String>   list  =new ArrayList<>();
        int type = Integer.parseInt(subnet.replaceAll(".*//*", ""));
        System.out.println(type);

        int mask = 0xFFFFFFFF << (32 - type);
        System.out.println(mask);

        String cidrIp = subnet.replaceAll("/.*", "");
        System.out.println(cidrIp);

        String[] cidrIps = cidrIp.split("\\.");
        System.out.println(cidrIps[3]);

        int cidrIpAddr = (Integer.parseInt(cidrIps[0]) << 24)
                | (Integer.parseInt(cidrIps[1]) << 16)
                | (Integer.parseInt(cidrIps[2]) << 8)
                | Integer.parseInt(cidrIps[3]);

        System.out.println(cidrIpAddr);

        System.out.println(cidrIpAddr & mask);*/

        String subnet = "192.168.1.0/24";

        String startIp = "192.168.1.1";
        String endIp = "192.168.3.1";

        Inet4Address start = (Inet4Address) Inet4Address.getByName(startIp);
        Inet4Address end = (Inet4Address) Inet4Address.getByName(endIp);
        List<String> l = new ArrayList<>();

        List<byte[]> list = HostCommon.getRangeIps(null, start.getAddress(), end.getAddress());
        for (byte[] bs : list) {
            l.add(InetAddress.getByAddress(bs).getHostAddress());
        }

        System.out.println(JsonUtil.toJson(l));

    }

    //创建应用
    @Test
    public void testApplicationCreateService() {
//        ApplicationCreateParam param = new ApplicationCreateParam();
//        param.setDescription("测试");
//        param.setName("测试");
//        param.setContainer("b5eec737da092d027dea1f183756a34ee54bc34fa918bf54a902ec4e6b47c773");
//        param.setLabel("fd");
//        param.setType("WEB");
//        param.setIp("192.168.1.1");
//        param.setPort("80");
//        param.setLoadsTotal(2);
//        param.setWeightType("fdsf");
//        param.setLoadPort("80");
//        param.setUserUniqueId("");
//        exchange.getIn().setBody(param.asJson());
//        Status<String> status = (Status) applicationCreateService.process(endpoint, exchange);
//        System.out.println(status.getStatus());
    }

    //删除应用
    @Test
    public void testApplicationDeleteByIdService() {
        Map<String,Object> m =new HashMap<String,Object>();
        m.put("userUniqueId","");
        m.put("id","");
        exchange.getIn().setHeaders(m);
        Status<String> status = (Status) applicationDeleteByIdService.process(endpoint, exchange);
        System.out.println(status.getStatus());
    }

    //根据id查询应用
    @Test
    public void testApplicationQueryByIdService() {
        Map<String,Object> m =new HashMap<>();
        m.put("userUniqueId","");
        m.put("id","");
        exchange.getIn().setHeaders(m);
        ApplicationInfoDto dto = (ApplicationInfoDto) applicationQueryByIdService.process(endpoint, exchange);
        System.out.println(dto.asJson());
    }

    //根据项目查询应用
    @Test
    public void testApplicationQueryByProjectService() {
        Map<String,Object> m =new HashMap<>();
        m.put("userUniqueId","");
        m.put("project","");
        exchange.getIn().setHeaders(m);
        List<ApplicationInfoDto> list = (List) applicationQueryByProjectService.process(endpoint, exchange);
        System.out.println("list:" + JsonUtil.toJson(list));
    }
}
