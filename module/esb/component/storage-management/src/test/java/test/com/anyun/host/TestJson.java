package test.com.anyun.host;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.StorageInfo;
import com.anyun.cloud.tools.json.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TwitchGG <ray@proxzone.com>
 * @since 1.0.0 on 8/25/16
 */
public class TestJson extends BaseComponentTest{

    @Test
    public void test1() {
        Response<List<StorageInfo>> response = new Response<>();
        List<StorageInfo> infos = new ArrayList<>();
        StorageInfo info = new StorageInfo();
        info.setAvail(0.001F);
        info.setFilesystem("192.168.1.154://gluser");
        info.setMountedOn("/mnt");
        info.setSize(100000.00F);
        info.setType("gluster");
        info.setUsed(50.00F);
        info.setUsedPercentage(30.04F);
        infos.add(info);
        response.setType(ArrayList.class.getCanonicalName());
        response.setContent(infos);

        String json = JsonUtil.toJson(response);
        response = JsonUtil.fromJson(Response.class,json);
        System.out.println(json);
        System.out.println(response.asJson());
    }
}
