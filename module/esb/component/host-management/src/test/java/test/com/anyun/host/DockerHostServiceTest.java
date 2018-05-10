package test.com.anyun.host;

import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.impl.DockerHostServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by twitchgg on 16-7-19.
 */
public class DockerHostServiceTest extends BasePlatformEnvironmentTester {
    private DockerHostService dockerHostService;

    @Before
    public void initService() throws Exception{
        dockerHostService = new DockerHostServiceImpl();
    }

    @Test
    public void testQueryActiveZookeeperHostNode() throws Exception{
        List<String> nodes = dockerHostService.findAllActiveHosts();
        for (String node : nodes) {
            System.out.println("Node [" + node + "]");
        }
    }
}
