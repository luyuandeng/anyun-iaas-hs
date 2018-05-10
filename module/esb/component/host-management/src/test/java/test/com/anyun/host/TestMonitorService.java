package test.com.anyun.host;

import com.anyun.cloud.dto.MonitorContainerDto;
import com.anyun.cloud.dto.MonitorHostDto;
import com.anyun.esb.component.host.service.publish.MonitorQueryContainerByHostService;
import com.anyun.esb.component.host.service.publish.MonitorQueryService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by gp on 16-9-22.
 */
public class TestMonitorService extends BaseComponentTest {
    private MonitorQueryContainerByHostService monitorQueryContainerByHostService;
    private MonitorQueryService monitorQueryService;

    @Before
    public void setup() throws Exception {
        monitorQueryContainerByHostService = new MonitorQueryContainerByHostService();
        monitorQueryService = new MonitorQueryService();
    }


    @Test
    public void testMonitorQueryContainerByHostService() {
        exchange.getIn().setHeader("host", "FC0976C958695F8DE0C3CC5605EFA01A");
        MonitorContainerDto dto = (MonitorContainerDto) monitorQueryContainerByHostService.process(endpoint, exchange);
        System.out.print(dto);
    }

    @Test
    public void testMonitorService() {
        exchange.getIn().setHeader("subMethod", "queryHostMonitor");
        exchange.getIn().setHeader("subParameters", "3159a3f27c76440e0790147b6cf07730bb3e5e0c6e9d936742131007ff204b62");
        List<MonitorHostDto> l = (List<MonitorHostDto>) monitorQueryService.process(endpoint, exchange);
        System.out.print(l);
    }


}
