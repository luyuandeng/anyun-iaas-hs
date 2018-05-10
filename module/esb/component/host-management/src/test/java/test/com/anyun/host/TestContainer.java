package test.com.anyun.host;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.ContainerNetIpMacDto;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* *
 *Created by gaopeng on 16-6-8.
 */
public class TestContainer extends BaseComponentTest {
    private ContainerStartService containerStartService;
    private ContainerStopService containerStopService;
    private ContainerDeleteService containerDeleteService;
    private ContainerQueryByIdService containerQueryByIdService;
    private ContainerQueryByProjectService containerQueryByProjectService;
    private ContainerQueryByNetLabelService containerQueryByNetLabelService;
    private ContainerQueryByImageService containerQueryByImageService;
    private ContainerCreateService containerCreateService;
    private ContainerQueryByProjectAndType containerQueryByProjectAndType;
    private ContainerChangeCalculationSchemeService containerChangeCalculationSchemeService;
    private ContainerChangeDiskSchemeService containerChangeDiskSchemeService;

    @Before
    public void setup() throws Exception {
        containerStartService = new ContainerStartService();
        containerStopService = new ContainerStopService();
        containerDeleteService = new ContainerDeleteService();
        containerQueryByIdService = new ContainerQueryByIdService();
        containerQueryByProjectService = new ContainerQueryByProjectService();
        containerQueryByNetLabelService = new ContainerQueryByNetLabelService();
        containerQueryByImageService = new ContainerQueryByImageService();
        containerCreateService = new ContainerCreateService();
        containerQueryByProjectAndType = new ContainerQueryByProjectAndType();
        containerChangeCalculationSchemeService = new ContainerChangeCalculationSchemeService();
        containerChangeDiskSchemeService = new ContainerChangeDiskSchemeService();
    }

    //创建容器有条件
    @Test
    public void testContainerCreate() {
        ContainerCreateByConditionParam param = new ContainerCreateByConditionParam();
        param.setHost("fdfdf");
        param.setImageId("4BF4AF4E85A54D732B50347812F152F0");
        param.setName("sxt_test");
        param.setUser("root12");
        param.setHostName("She-1986");
        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
        param.setProjectId("3a3cbe33c3624344a90526b2436a989f");
        Map<String, String> m = new HashMap<String, String>();
        m.put("setpcap", "weqe");
        param.setCapAdd(m);
        param.setSecurityOpt(new HashMap<String, String>() {{
            put("user", "");
        }});
        param.setUserUniqueId("huangwentao");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status<String>) containerCreateService.process(endpoint, exchange);
        System.out.print(status.getStatus());
    }

    //批量创建
    @Test
    public void batchCreate() {
        List<ContainerCreateByConditionParam> params = new ArrayList<>();
        ContainerCreateByConditionParam param;
//        param= new ContainerCreateByConditionParam();
//        param.setImageId("4BF4AF4E85A54D732B50347812F152F0");
//        param.setName("sxt_test");
//        param.setUser("root12");
//        param.setHostName("She-1986");
//        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
//        param.setProjectId("3a3cbe33c3624344a90526b2436a989f");
//        Map<String,String>  m = new HashMap<String,String>();
//        m.put("setpcap","weqe");
//        param.setCapAdd(m);
//        param.setSecurityOpt(new HashMap<String,String>(){{
//            put("user","");
//        }});
//        param.setUserUniqueId("huangwentao");
//        params.add(param);
//
//        param = new ContainerCreateByConditionParam();
//        param.setImageId("4BF4AF4E85A54D732B50347812F152F0");
//        param.setName("sxt_test");
//        param.setUser("root12");
//        param.setHostName("She-1986");
//        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
//        param.setProjectId("3a3cbe33c3624344a90526b2436a989f");
//        Map<String,String>  m1 = new HashMap<String,String>();
//        m1.put("setpcap","weqe");
//        param.setCapAdd(m);
//        param.setSecurityOpt(new HashMap<String,String>(){{
//            put("user","");
//        }});
//        param.setUserUniqueId("huangwentao");
//        params.add(param);
//
//        param = new ContainerCreateByConditionParam();
//        param.setImageId("4BF4AF4E85A54D732B50347812F152F0");
//        param.setName("sxt_test");
//        param.setUser("root12");
//        param.setHostName("She-1986");
//        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
//        param.setProjectId("3a3cbe33c3624344a90526b2436a989f");
//        Map<String,String>  m2 = new HashMap<String,String>();
//        m2.put("setpcap","weqe");
//        param.setCapAdd(m);
//        param.setSecurityOpt(new HashMap<String,String>(){{
//            put("user","");
//        }});
//        param.setUserUniqueId("huangwentao");
//        params.add(param);
        System.out.println("-------params:," + JsonUtil.toJson(params));
        exchange.getIn().setBody(JsonUtil.toJson(params));
        List<ContainerDto> list = (List<ContainerDto>) containerCreateService.process(endpoint, exchange);
        System.out.println("list:" + JsonUtil.toJson(list));
    }


    //启动容器
    @Test
    public void testStartContainer() throws Exception {
        ContainerStartParam param = new ContainerStartParam();
        param.setUserUniqueId("");
        param.setId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status<String>) containerStartService.process(endpoint, exchange);
        System.out.print(status.getStatus());
    }

    //停止容器
    @Test
    public void testStopContainer() throws Exception {
        ContainerStopParam param = new ContainerStopParam();
        param.setUserUniqueId("");
        param.setId("");
        exchange.getIn().setBody(param.asJson());
        Status<String> status = (Status<String>) containerStopService.process(endpoint, exchange);
        System.out.print(status.getStatus());
    }

    //删除容器
    @Test
    public void testDeleteContainer() throws Exception {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "");
        m.put("id", "");
        exchange.getIn().setHeaders(m);
        Status<String> status = (Status<String>) containerDeleteService.process(endpoint, exchange);
        System.out.println(status.getStatus());
    }

    //根据id查询容器信息
    @Test
    public void testContainerQueryById() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "");
        m.put("id", "");
        exchange.getIn().setHeaders(m);
        ContainerDto containerDto = (ContainerDto) containerQueryByIdService.process(endpoint, exchange);
        System.out.println(containerDto.asJson());
    }

    //根据项目查询容器信息
    @Test
    public void testContainerQueryByProject() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "1");
        m.put("id", "1");
        exchange.getIn().setHeaders(m);
        List<ContainerDto> dtos = (List<ContainerDto>) containerQueryByProjectService.process(endpoint, exchange);
    }

    //根据网络标签查询容器信息
    @Test
    public void testContainerQueryByNetLabel() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "1");
        m.put("id", "1");
        exchange.getIn().setHeaders(m);
        List<ContainerDto> dtos = (List<ContainerDto>) containerQueryByNetLabelService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dtos));
    }

    //根据镜像获取容器信息
    @Test
    public void testContainerQueryByImage() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "1");
        m.put("id", "1");
        exchange.getIn().setHeaders(m);
        List<ContainerDto> dtos = (List<ContainerDto>) containerQueryByImageService.process(endpoint, exchange);
        System.out.println(JsonUtil.toJson(dtos));
    }

    @Test
    public void testContainerQueryByProjectAndType() {
        Map<String, Object> m = new HashMap();
        m.put("userUniqueId", "1");
        m.put("project", "1");
        m.put("type", 0);
        exchange.getIn().setHeaders(m);
        List<ContainerDto> l = (List<ContainerDto>) containerQueryByProjectAndType.process(endpoint, exchange);
        System.out.println("l" + JsonUtil.toJson(l));
    }

    @Test
    public void testRunTime() {
        String formatTimes = "3 4 1 32";
        String formatTime = formatTimes.split(" ")[0] + "天" +
                formatTimes.split(" ")[1] + "时" +
                formatTimes.split(" ")[2] + "分" +
                formatTimes.split(" ")[3] + "秒";

        System.out.print(formatTime);
    }


    @Test
    public void containerChangeCalculationSchemeService() {
        ContainerChangeCalculationSchemeParam p = new ContainerChangeCalculationSchemeParam();
        p.setId("s");
        p.setCalculationSchemeId("s");
        exchange.getIn().setBody(p.asJson());
        Status<String> status = (Status<String>) containerChangeDiskSchemeService.process(endpoint, exchange);
        System.out.println(status);
    }

    @Test
    public void containerChangeDiskSchemeService() {
        ContainerChangeDiskSchemeParam p = new ContainerChangeDiskSchemeParam();
        p.setId("s");
        p.setDiskSchemeId("s");
        exchange.getIn().setBody(p.asJson());
        Status<String> status = (Status) containerChangeDiskSchemeService.process(endpoint, exchange);
        System.out.println(status);
    }
}