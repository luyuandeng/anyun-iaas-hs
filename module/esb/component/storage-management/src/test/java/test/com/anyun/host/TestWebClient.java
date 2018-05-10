package test.com.anyun.host;

import com.anyun.cloud.param.GlusterStorageAdd;
import com.anyun.esb.component.storage.common.HostWebClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by twitchgg on 16-8-9.
 */
public class TestWebClient extends BaseComponentTest{
    private HostWebClient hostWebClient;

    @Before
    public void initClient() {
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        configuration.setPlatformAddress("192.168.1.154");
        hostWebClient = HostWebClient.build(configuration);
    }

    //获取存储列表
    @Test
    public void testListStorage() throws Exception{
        String json = hostWebClient.get("/storage/list/gluster",null);
        System.out.println(json);
    }


    //创建存储
    @Test
    public void testAddStorage() throws Exception{
        GlusterStorageAdd  param=new GlusterStorageAdd();
        param.setGluserSrc("192.168.1.151:/test-volume");
        param.setUseType("docker.volume");
        String type="glusterfs";
        String json = hostWebClient.put("/storage/add/"+type,param.asJson());
        System.out.println(json);
    }
}
