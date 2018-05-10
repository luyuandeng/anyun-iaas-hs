package test.com.anyun.host;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.ProjectInfoQueryDto;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ZKConnector;
import com.anyun.common.jbi.component.ZkConnectLostListener;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.publish.*;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-13.
 */
public class TestProject extends BaseComponentTest {
    private ProjectDao projectDao;

    @Before
    public void setup() throws Exception {
        projectDao = new ProjectDaoImpl();
        String hostCert = "/home/sxt/.ssh/office_rsa";
        HostSshClient hostSshClient = new HostSshClient(hostCert);
        hostSshClient.setHost("192.168.1.155");
        hostSshClient.connect();
        String serialNumber = "B48925F9F8970E48432206056A3808A6";
        Env.export("host.ssh." + serialNumber, hostSshClient);
        ZKConnector zookeeperConnector = new ZKConnector();
        zookeeperConnector.setConnectLostListener(new ZkConnectLostListener() {
            @Override
            public void afterConnectLost(ZKConnector connector) {
            }
        });
        String zkString = "192.168.1.117:2181";
        zookeeperConnector.connect(zkString, 3000);
        Env.set(ZKConnector.class,zookeeperConnector);
    }

   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //创建项目
    @Test
    public  void testProjectCreateService(){
        ProjectCreateService   service=new ProjectCreateService();
        ProjectCreateParam  param = new ProjectCreateParam();
        param.setDescript("");
        param.setName("123");
        param.setSpace(200);
        exchange.getIn().setBody(param.asJson());
        service.process(endpoint,exchange);
    }

    //修改项目
    @Test
    public  void testProjectUpdateByIdService(){
        ProjectUpdateByIdService  service  = new ProjectUpdateByIdService();
        ProjectUpdateParam  param = new ProjectUpdateParam();
        param.setId("be5705b117a8418bb4ab0d9bba76dcdb");
        param.setDescript("");
        param.setName("");
        param.setSpace(10);
        exchange.getIn().setBody(param.asJson());
        service.process(endpoint,exchange);
    }

    //删除项目
    @Test
    public void  testProjectDeleteByIdService(){
        ProjectDeleteByIdService service = new ProjectDeleteByIdService();
        exchange.getIn().setHeader("id","01e200982dbe4934958dc40e16ca0a8f");
        service.process(endpoint,exchange);
    }

    //查询项目
    @Test
    public void testProjectQuery(){
        ProjectQueryByUserUniqueIdService  service = new ProjectQueryByUserUniqueIdService();
        Map<String,Object>   m  = new HashMap<>();
        List<ProjectDto> dto = ( List<ProjectDto>) service.process(endpoint,exchange);
        System.out.println(JsonUtil.toJson(dto));
    }


    //查询网络容器关系信息
    @Test
    public  void  testProjectSurveyQueryByIdService(){
        ProjectSurveyQueryByIdService projectSurveyQueryByIdService =new ProjectSurveyQueryByIdService();
        String  id="0486a8ae-0563-45dc-a432-ef2d8e1ee9f1";
        exchange.getIn().setHeader("id",id);
        ProjectInfoQueryDto  projectInfoQueryDto =(ProjectInfoQueryDto)projectSurveyQueryByIdService.process(endpoint,exchange);
        System.out.print(JsonUtil.toJson(projectInfoQueryDto));
    }

    @Test
    public void test(){
        SimpleDateFormat date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = date.format(new Date());
        System.out.print(createTime);
    }
}
