package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ContainerService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.core.rest.impl.ContainerServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-8-3.
 */
public class TestContainer extends BaseAnyunTest {
    private ContainerService service;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        service = factory.getContainerService();
        ResourceClient.setUserToken("debug_token");
    }

    ///////////////////////////////////////////////////容器测试/////////////////////////////////////////////////////////////////////////////////
    //1、查询容器详情
    @Test
    public void getDetails() throws Exception {
        String id = "949d29546a54ce6441a8aa9d34dd16162d0822e10e61465bd21bb4116140562c";
        String userUniqueId = "";
        ContainerDto c = service.queryContainerById(id, userUniqueId);
        System.out.println(c.asJson());
    }

    //2、查询容器列表
    @Test
    public void getList() throws Exception {
        //QUERY_BY_IMAGE
        //QUERY_BY_LABEL
        //QUERY_NOT_CONNECTED_BY_LABEL
        //QUERY_BY_PROJECT
        //QUERY_UNPUBLISHED_BY_PROJECT
        String subMethod = "QUERY_UNPUBLISHED_BY_PROJECT";
        String subParameters = "c4a0b79c85244152b92436e9f01ce063";
        List<ContainerDto> l = service.queryContainer("", subMethod, subParameters);
        for (ContainerDto c : l) {
            System.out.println(c.asJson());
        }
    }

    //3、创建容器
    @Test
    public void createContainer() throws Exception {
        ContainerCreateByConditionParam param = new ContainerCreateByConditionParam();
        param.setProjectId("c4a0b79c85244152b92436e9f01ce063");
        param.setImageId("45028571E6EE583AFB47BF62020C92CA");
        param.setName("aqzddd");
        param.setHostName("She-1113");
        param.setUser("root13");
        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
        param.setCpuFamily("Intel(R) Core(TM) i5-4460 CPU @ 3.20GHz");
        param.setPurpose("修改测试");
        param.setCpuCoreLimit(0);
        param.setMemoryTotal(15);
        param.setMemoryUnused(7);
        param.setMemoryLimit(0);
        param.setMemorySwapLimit(0);
        param.setIp("172.18.3.11");
        param.setHost("028E6CDD1B70EDA57F1BCBCEFAF125E9");
        param.setCapAdd(new HashMap<>());
        param.setUlimit(new HashMap<>());
        param.setSecurityOpt(new HashMap<>());
        param.setPrivileged(0);
        param.setUserUniqueId("2");
        param.setPurpose("WEB");
        param.setCalculationSchemeId("3fdd5d96e58442d38b3d786882d24241");
        param.setDiskSchemeId("39d8066c0cc940ecae3535808d9e938c");
        ContainerDto dto = service.createContainerByCondition(param);
        System.out.println(dto.asJson());
    }


    /**
     * 4、 操作容器
     * <p>
     * start：启动
     * restart：重启
     * stop：停止
     * pause：暂停
     * unpause：解除暂停
     * delete：删除
     * kill  : 结束进程
     */
    @Test
    public void operationContainer() {
        ContainerService s = new ContainerServiceImpl();
        ContainerOpParam p = new ContainerOpParam();
        p.setId("8053cfcab76458068c42e031172bbf17248614127041f631cf57dfafc44ff264");
        p.setOp("delete");
        Status<String> status = s.operationContainer(p);
        System.out.println(status.asJson());
    }

    /**
     * 5、查询容器列表
     */
    @Test
    public void getPageListConditions() throws Exception {
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(10);
        commonQueryParam.setSortBy("name");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();

        Conditions conditions = new Conditions();
        conditions.setName("__userTag__");
        conditions.setOp("is null");
        conditions.setValue("%D%");
        l.add(conditions);

        Conditions conditions1 = new Conditions();
        conditions1.setName("status");
        conditions1.setOp("like");
        conditions1.setValue("%2%");
        l.add(conditions1);

        Conditions conditions2 = new Conditions();
        conditions2.setName("name");
        conditions2.setOp("is not null");
        conditions2.setValue("11");
        l.add(conditions2);


        Conditions conditions3 = new Conditions();
        conditions3.setName("status");
        conditions3.setOp("not in");
        String test1 = "1";
        String test2 = "5";
        conditions3.setValue("(" + test1 + "," + test2 + ")");
        l.add(conditions3);

        commonQueryParam.setConditions(l);
        PageDto<ContainerDto> pageDto = service.getContainerList(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }

    @Test
    public void createByList() throws Exception {
        List<ContainerCreateByConditionParam> params = new ArrayList<>();
        ContainerCreateByConditionParam param;

        //容器1
        param = new ContainerCreateByConditionParam();
        param.setProjectId("c4a0b79c85244152b92436e9f01ce063");
        param.setImageId("45028571E6EE583AFB47BF62020C92CA");
        param.setName("1");
        param.setHostName("She-1112");
        param.setUser("root13");
        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
        param.setCpuFamily("Intel(R) Core(TM) i5-4460 CPU @ 3.20GHz");
        param.setPurpose("修改测试");
        param.setCpuCoreLimit(0);
        param.setMemoryTotal(15);
        param.setMemoryUnused(7);
        param.setMemoryLimit(0);
        param.setMemorySwapLimit(0);
        param.setIp("172.18.3.11");
        param.setHost("028E6CDD1B70EDA57F1BCBCEFAF125E9");
        param.setCapAdd(new HashMap<>());
        param.setUlimit(new HashMap<>());
        param.setSecurityOpt(new HashMap<>());
        param.setPrivileged(0);
        param.setUserUniqueId("2");
        param.setPurpose("WEB");
        param.setCalculationSchemeId("1");
        param.setDiskSchemeId("");
        params.add(param);

        //容器2
        param = new ContainerCreateByConditionParam();
        param.setProjectId("c4a0b79c85244152b92436e9f01ce063");
        param.setImageId("45028571E6EE583AFB47BF62020C92CA");
        param.setName("2");
        param.setHostName("She-1112");
        param.setUser("root13");
        param.setKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8nnMlna53qpCThGsp2wazb3p98bdnseis00WXy30Ned0H9H2OmWoIHiHYVbUtQhWjXTOHNAWB34mb4+uQ++9Z1IWOOZT3BjTF0ENMIrVhqRAVNqwq4IT3Z/o632vE3IStZKJMg55CN17mr6WRZodTplOt4G54bzN1WBnz6b00CSWGUMoBZJBwFjyUGgZ9KIAF7bQRKRxT3HU0PWyfKRwbZJSceinQyLPgtv0VJ66Y5dXDia3hknOx7iiIjGiSSIjtNN/7TBwL6ydM2eHyxaszrNZ9X9knjy7t9EWDkhZoMMSxDf29jrPFlylYkzrabUsrsQqyEtaJrs1vmxliqeVr");
        param.setCpuFamily("Intel(R) Core(TM) i5-4460 CPU @ 3.20GHz");
        param.setPurpose("修改测试");
        param.setCpuCoreLimit(0);
        param.setMemoryTotal(15);
        param.setMemoryUnused(7);
        param.setMemoryLimit(0);
        param.setMemorySwapLimit(0);
        param.setIp("172.18.3.11");
        param.setHost("028E6CDD1B70EDA57F1BCBCEFAF125E9");
        param.setCapAdd(new HashMap<>());
        param.setUlimit(new HashMap<>());
        param.setSecurityOpt(new HashMap<>());
        param.setPrivileged(0);
        param.setUserUniqueId("2");
        param.setPurpose("WEB");
        param.setCalculationSchemeId("1");
        param.setDiskSchemeId("");
        params.add(param);

        List<ContainerDto> list = service.createContainer(params);
        System.out.println(JsonUtil.toJson(list));

    }

    @Test
    public void changeCalculationScheme() {
        ContainerChangeCalculationSchemeParam param = new ContainerChangeCalculationSchemeParam();
        param.setId("8053cfcab76458068c42e031172bbf17248614127041f631cf57dfafc44ff264");
        param.setCalculationSchemeId("3fdd5d96e58442d38b3d786882d24241");
        Status<String> status = service.changeCalculationScheme(param);
        System.out.println(status);
    }


    @Test
    public void changeDiskScheme() {
        ContainerChangeDiskSchemeParam param = new ContainerChangeDiskSchemeParam();
        param.setId("8053cfcab76458068c42e031172bbf17248614127041f631cf57dfafc44ff264");
        param.setDiskSchemeId("39d8066c0cc940ecae3535808d9e938c");
        Status<String> status = service.changeDiskScheme(param);
        System.out.println(status);
    }


}
