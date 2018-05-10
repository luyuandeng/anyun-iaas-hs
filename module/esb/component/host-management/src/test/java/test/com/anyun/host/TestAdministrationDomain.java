package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.DomainCreateParam;
import com.anyun.esb.component.host.service.publish.AdministrationDomainCreateByConditionService;
import com.anyun.esb.component.host.service.publish.AdministrationDomainDeleteByIdService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twh-workspace on 17-2-22.
 */
public class TestAdministrationDomain extends BaseComponentTest {
    private AdministrationDomainCreateByConditionService administrationDomainCreateByConditionService;
    private AdministrationDomainDeleteByIdService administrationDomainDeleteByIdService;

    @Before
    public void setup() throws Exception {
        administrationDomainCreateByConditionService = new AdministrationDomainCreateByConditionService();
        administrationDomainDeleteByIdService = new AdministrationDomainDeleteByIdService();
    }

    @Test
    public void testAdministrationDomainCreateByConditionService() throws Exception {
        DomainCreateParam param = new DomainCreateParam();
        param.setId("222");
        param.setDomain("111111111111");
        param.setIp("192.168.1.111");
        param.setContainerId("31345354345");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status) administrationDomainCreateByConditionService.process(endpoint, exchange);
        System.out.println(status.getStatus());
    }

    @Test
    public void testAdministrationDomainDeleteById(){
        Map<String,Object> m =new HashMap<String,Object>();
        m.put("id","222");
        exchange.getIn().setHeaders(m);
        Status<String> status = (Status) administrationDomainDeleteByIdService.process(endpoint,exchange);
        System.out.println(status.getStatus());
    }
}
