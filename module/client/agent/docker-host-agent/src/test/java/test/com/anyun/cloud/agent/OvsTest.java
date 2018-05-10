package test.com.anyun.cloud.agent;

import com.anyun.cloud.agent.common.service.OvsService;
import com.anyun.cloud.agent.core.DefaultOvsService;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.api.Status;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by sugs on 16-10-26.
 */
public class OvsTest extends BaseTest{

    private OvsService ovsService;

    @Before
    public void setOvsService(){
        ovsService = new DefaultOvsService();
    }

    @Test
    public void test(){
        Response<Status<String>> response = ovsService.addAllBridge("br0");
        System.out.println(response);
    }

}
