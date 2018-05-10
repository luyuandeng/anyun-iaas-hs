package test.com.anyun.host;

import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.service.docker.DockerHostRegistService;
import com.anyun.esb.component.host.service.docker.impl.DockerHostRegistServiceImpl;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/6/16
 */
public class TestHostRegist extends BaseComponentTest {
    private DockerHostRegistService dockerHostRegistService;

    @Before
    public void init() throws Exception {
        dockerHostRegistService = new DockerHostRegistServiceImpl();
    }

    @Test
    public void testRegist() throws Exception {
        String id = "2C6AEFA5794DBB9BE070F37FF7D192D59";
        String mgrIp = "192.168.1.156";
        String host = id + ":" + mgrIp;
        dockerHostRegistService.connectHost(host, id, mgrIp);
    }
}
