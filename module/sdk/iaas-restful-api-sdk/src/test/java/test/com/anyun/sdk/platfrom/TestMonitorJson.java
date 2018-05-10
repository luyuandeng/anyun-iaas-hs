package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.Configuration;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by twitchgg on 16-7-28.
 */
public class TestMonitorJson {
    public static void main(String[] args) throws Exception{
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        //初始化客户端,只要初始化一次
        Configuration configuration = new Configuration();
        configuration.setPlatformAddress("192.168.1.117");
        configuration.setPort(8888);
        configuration.setBaseUrl("/api/v1.3");
        factory.config(configuration);


        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String json = rsClient.query("/containers/docker/175488189ac9a812cdfcf3fb2904635e376f97a1ef8f234cb0a262f517921d5e",null);
        List<Map<String,Object>> stats = (List<Map<String,Object>>)JsonUtil.fromJson(Map.class,json).get("stats");
        for (Map<String,Object> map : stats) {
            System.out.println("======================================================================");
//            for (Map.Entry<String,Object> entry : map.entrySet()) {
//                System.out.println(entry.getKey() + " : " + entry.getValue());
//            }
            String timestamp = (String)map.get("timestamp");
            System.out.println(timestamp);
        }
        System.out.println(stats.size());
    }
}
