package test.com.anyun.cloud.agent;

import com.anyun.cloud.agent.common.service.StorageService;
import com.anyun.cloud.agent.core.DefaultStorageService;
import com.anyun.cloud.agent.param.GlusterStorageAdd;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.api.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by twitchgg on 16-8-3.
 */
public class StorageServiceTester extends BaseTest {
    private StorageService storageService;

    @Before
    public void setUpServices() {
        storageService = new DefaultStorageService();
    }

    @Test
    public void testStorage() {
        Response<List<StorageInfo>> response = storageService.listStorageByType("ext4");
        printResponse(response);
    }

    @Test
    public void testJson() {
        GlusterStorageAdd glusterStorageAdd = new GlusterStorageAdd();
        glusterStorageAdd.setGluserSrc("192.168.1.151:/test-volume1");
        glusterStorageAdd.setUseType("docker.volume");
        System.out.println(toJson(glusterStorageAdd));
        Response<StorageInfo> response = storageService.addStorage("ext4",toJson(glusterStorageAdd));
        printResponse(response);
    }
}
