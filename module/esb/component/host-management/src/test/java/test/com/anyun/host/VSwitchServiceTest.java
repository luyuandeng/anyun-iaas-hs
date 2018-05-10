package test.com.anyun.host;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.HostCommon;
import com.anyun.esb.component.host.service.docker.VSwitchService;
import com.anyun.esb.component.host.service.docker.impl.VSwitchServiceImpl;
import org.hashids.Hashids;
import org.junit.Before;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Date;
import java.util.List;

/**
 * Created by twitchgg on 16-7-19.
 */
public class VSwitchServiceTest extends BasePlatformEnvironmentTester {
    private VSwitchService vSwitchService;

    @Before
    public void initService() throws Exception {
        vSwitchService = new VSwitchServiceImpl();
        String hostCert = "/home/sxt/.ssh/office_rsa";
        HostSshClient hostSshClient = new HostSshClient(hostCert);
        hostSshClient.setHost("192.168.1.155");
        hostSshClient.connect();
        String serialNumber = "5A1035E96D7F35B945259852F9821A28";
        Env.export("host.ssh." + serialNumber, hostSshClient);
    }

    @Test
    public void testCreateNetworkLabel() throws Exception {
        Hashids hashids = new Hashids(StringUtils.uuidGen());
        System.out.print(StringUtils.uuidGen());
        String label = "anyun-" + hashids.encode(new Date().getTime());
        String subnet = "192.168.9.0/24";
        System.out.println("Label [" + label + "]");
        vSwitchService.createOVNNetwork(label, subnet, null);
        vSwitchService.deleteOVNNetwork(label);
    }

    @Test
    public void testQueryOVNLabels() throws Exception {
        String label = "foo1";
        String result = vSwitchService.getNetworkLabel(label, "openvswitch");
        System.out.println(result);
    }

    @Test
    public void testGetIpRange() throws Exception {
        Inet4Address start = (Inet4Address) Inet4Address.getByName("192.168.1.11");
        Inet4Address end = (Inet4Address) Inet4Address.getByName("192.168.2.5");
        List<byte[]> list = HostCommon.getRangeIps(null, start.getAddress(), end.getAddress());
        for (byte[] bs : list) {
            System.out.println(InetAddress.getByAddress(bs).getHostAddress());
        }
    }
}
