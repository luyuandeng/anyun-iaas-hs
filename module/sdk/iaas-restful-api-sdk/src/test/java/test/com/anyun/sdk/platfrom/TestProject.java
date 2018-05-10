package test.com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ProjectService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaopen g on 16-7-20.
 */
public class TestProject extends BaseAnyunTest {
    private ProjectService projectService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        projectService = factory.getProjectService();
        ResourceClient.setUserToken("debug_token");
    }

    //1、查询项目详情
    @Test
    public void getDetails() {
        String id = "c4a0b79c85244152b92436e9f01ce063";
        String userUniqueId = "2";
        ProjectDto p = projectService.queryProjectById(id, userUniqueId);
        System.out.print(p.asJson());
    }

    //2、查询项目列表
    @Test
    public void getList() {
        String userUniqueId = "sxt";
        List<ProjectDto> list = projectService.queryProjectByCondition(userUniqueId);
        for(ProjectDto p:list){
            System.out.println(p.asJson());
        }
    }

    //3、删除项目
    @Test
    public void delete() {
        String id = "4df6c2f3ae964db88ada5567e805fa3d";
        String userUniqueId = "2";
        projectService.deleteProjectByProjectId(id, userUniqueId);
    }

    //4、创建项目 ：OK
    @Test
    public void create() {
        ProjectCreateParam params = new ProjectCreateParam();
        params.setName("test");
        params.setDescript("2017/08/14");
        params.setSpace(100);
        params.setUserUniqueId("sxt");
        ProjectDto dto=projectService.createProject(params);
        System.out.print(dto.asJson());
    }

    //5、修改项目
    @Test
    public void update() {
        ProjectUpdateParam param = new ProjectUpdateParam();
        param.setId("4531144940f24ebeb13ec65bc7342e13");
        param.setName("");
        param.setSpace(0);
        param.setDescript("123");
        param.setUserUniqueId("sxt");
        projectService.updateProject(param);
    }


    //6、根据条件分页查询项目
    @Test
    public void   getPageListConditions()throws Exception{
//        {name:“__userTag__",op:"in",value:"tag1,tag2"}
        CommonQueryParam commonQueryParam = new CommonQueryParam();
        commonQueryParam.setCount(false);
        commonQueryParam.setStart(1);
        commonQueryParam.setLimit(2);
        commonQueryParam.setSortBy("createDate");
        commonQueryParam.setReplyWithCount(false);
        commonQueryParam.setSortDirection("asc");

        List<Conditions> l = new ArrayList<>();

//        Conditions conditions = new Conditions();
//        conditions.setName("__userTag__");
//        conditions.setOp("is null");
//        conditions.setValue("%D%");
//        l.add(conditions);

        Conditions conditions1 = new Conditions();
        conditions1.setName("name");
        conditions1.setOp("like");
        conditions1.setValue("%2%");
        l.add(conditions1);

//        Conditions conditions2 = new Conditions();
//        conditions2.setName("name");
//        conditions2.setOp("is not null");
//        conditions2.setValue("11");
//        l.add(conditions2);


//        Conditions conditions3 = new Conditions();
//        conditions3.setName("status");
//        conditions3.setOp("not in");
//        String test1 = "1";
//        String test2 = "2";
//        conditions3.setValue("(" + test1 + "," + test2 + ")");
//        l.add(conditions3);

        commonQueryParam.setConditions(l);
        PageDto<ProjectDto> pageDto= projectService.getPageListConditions(commonQueryParam);
        System.out.println(JsonUtil.toJson(pageDto));
    }
}
