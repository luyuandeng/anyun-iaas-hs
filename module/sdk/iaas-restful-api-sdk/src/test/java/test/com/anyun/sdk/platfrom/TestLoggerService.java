package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.LoggerService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by gp on 16-11-22.
 */
public class TestLoggerService extends BaseAnyunTest {
    private LoggerService loggerService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        loggerService = factory.getLoggerService();
        ResourceClient.setUserToken("debug_token");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //1、查询日志列表
    @Test
    public void getList() {
        LoggerQueryParam param = new LoggerQueryParam();
        param.setIp("192.168.1.155");
        param.setEndTime("2017-3-1 19:50:00.000");
        param.setStartTime("2017-3-17 19:10:00.000");
        param.setKeyWord("");
        param.setFileName("");
        param.setPageNum(1);
        param.setPageCount(20);
        param.setUserUniqueId("huangwentao");
        List<LoggerDto> l = loggerService.queryLoggerByCondition(param);
        System.out.print(JsonUtil.toJson(l));
        for (LoggerDto loggerDto : l) {
            System.out.println(JsonUtil.toJson(loggerDto));
        }
    }
}
