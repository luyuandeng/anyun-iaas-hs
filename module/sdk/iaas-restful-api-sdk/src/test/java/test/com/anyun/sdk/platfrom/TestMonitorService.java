package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.MonitorHostDto;
import com.anyun.cloud.dto.MonitorOverviewDto;
import com.anyun.sdk.platfrom.MonitorService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by gp on 16-9-21.
 */
public class TestMonitorService extends BaseAnyunTest {
    private MonitorService monitorService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        monitorService = factory.getMoniotrService();
        ResourceClient.setUserToken("debug_token");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //1、总览监控详情
    @Test
    public void getOverviewMonitorDetails() {
        MonitorOverviewDto dto = monitorService.queryOverviewMonitor("fd");
        System.out.print(dto);
    }


    //2、查询监控列表
    @Test
    public void teatqueryMonitor() throws Exception {

        String subMethod="QUERY_BY_HOST";
        String subParameters ="192.168.1.155";

//        String subMethod = "QUERY_BY_CONTAINER";
//        String subParameters = "a0ff3738e2849232e97ab7a5f6a8294c6b5c7268f07f6f3344faa318e4e60a03";

        String userUniqueId = "df";
        List<MonitorHostDto> list = monitorService.queryMonitor(userUniqueId, subMethod, subParameters);
        for (MonitorHostDto monitorHostDto : list) {
            System.out.println(monitorHostDto.asJson());
        }
    }


    //3、查询监控历史
    @Test
    public void getMonitorHistoryData() throws Exception {
        Date beginTime = new Date();
        Date endTime = new Date();

//        String subMethod="GET_HOST_MONITOR_HISTORY_DATA";
//        String subParameters ="192.168.1.155|"+beginTime+"|"+endTime;

        String subMethod = "GET_CONTAINER_MONITOR_HISTORY_DATA";
        String subParameters = "573776b184236511c050d3d433da02483c0a54853949cb3e2661a4fff5255631|" + beginTime + "|" + endTime;

        List<MonitorHostDto> list = monitorService.getMonitorHistoryData("", subMethod, subParameters);
        for (MonitorHostDto monitorHostDto : list) {
            System.out.println(monitorHostDto.asJson());
        }

    }


    //4、删除监控数据
    @Test
    public void deleteMonitorData() throws Exception {
        Date endTime = new Date();
        Status<Boolean> status = monitorService.deleteMonitorData("", endTime);
        System.out.print(status.getStatus());
    }

}
