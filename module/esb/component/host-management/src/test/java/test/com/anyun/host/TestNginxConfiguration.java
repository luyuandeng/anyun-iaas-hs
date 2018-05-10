package test.com.anyun.host;

import com.anyun.cloud.tools.FileUtil;
import com.anyun.esb.component.host.common.Resources;
import com.anyun.esb.component.host.service.TemplateService;
import com.anyun.esb.component.host.service.docker.impl.TemplateServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.stringtemplate.v4.ST;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author TwitchGG <ray@proxzone.com>
 * @since 1.0.0 on 31/10/2016
 */
public class TestNginxConfiguration extends Assert {

    @Test
    public void test1() throws Exception{
        ST hello = new ST("Hello, <name>!");
        hello.add("name", "World");
        String output = hello.render();
        System.out.println(output);
    }

    @Test
    public void test2() throws Exception {
        StringBuilder sb = new StringBuilder();
        ST st = new ST("<servers>!");
        st.add("servers", Arrays.asList(
                "server1",
                "server2",
                "server3"
        ));
        String output = st.render();
        System.out.println(output);
    }

    @Test
    public void test3() throws Exception{
        ClassLoader classLoader = TestNginxConfiguration.class.getClassLoader();


        File template = Resources.getResourceAsFile(classLoader,"templates/user_app_template.conf");
        System.out.println(template.getAbsolutePath());
        String str = FileUtil.cat(template,"UTF-8");
        ST st = new ST(str);
        st.add("protocol",false);
        st.add("servers", Arrays.asList(
                "s1.app1.domain.com;\n",
                "s2.app1.domain.com;\n",
                "s3.app1.domain.com;\n"
        ));
        String output = st.render();
        System.out.println("----------content----------");
        System.out.println(output);
    }

    @Test
    public void test4() throws Exception {
        TemplateService templateService = new TemplateServiceImpl();
        List<String> containersIp = new ArrayList<>();
        containersIp.add("s1.app1.domain.com");
        containersIp.add("s2.app1.domain.com");
        containersIp.add("s3.app1.domain.com");
//        templateService.getNginxConfigutation(containersIp,8443,443);
    }

    @Test
    public void test5() throws Exception {
        TemplateService templateService = new TemplateServiceImpl();
        List<String> containersIp = new ArrayList<>();
        containersIp.add("172.20.0.17");
        containersIp.add("172.20.0.18");

        //      String  s=templateService.getNginxConfigutation(containersIp,80,443);
    //    System.out.println("s:"+s);
    }


    @Test
    public  void dbTest() throws Exception{
        TemplateService templateService=new TemplateServiceImpl();
        String mgmIp="192.168.1.129";
        List<String>  mysqldIps=new ArrayList<>();
        mysqldIps.add("192.168.1.130");
        mysqldIps.add("192.168.1.131");
        mysqldIps.add("192.168.1.132");
//        String haproxyConf=templateService.getHaproxyConfigutation(mysqldIps);
//        String  mysqldConf=templateService.getMysqldConfiguation(mgmIp);
        String mgmConf=templateService.getMgmConfiguation(mgmIp,mysqldIps);
    }




}
