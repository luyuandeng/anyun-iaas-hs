package test.com.anyun.host;

import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.esb.component.host.common.Env;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by twitchgg on 16-7-19.
 */
public class BasePlatformEnvironmentTester extends BaseComponentTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePlatformEnvironmentTester.class);
    @Before
    public void initEnvironment() throws Exception {
        Jedis jedis = new Jedis("redis", 6379);
        String zkString = jedis.get("com.anyun.zookeeper.address");
        String timeout = jedis.get("com.anyun.zookeeper.timeout");
        ZKConnector zookeeperConnector = new ZKConnector();
        zookeeperConnector.connect(zkString, Long.valueOf(timeout).intValue());
        LOGGER.info("Anyun cloud scheduling server [{}] connected", zkString);
        Env.set(ZKConnector.class,zookeeperConnector);
        Env.set(Jedis.class,jedis);
    }
}
