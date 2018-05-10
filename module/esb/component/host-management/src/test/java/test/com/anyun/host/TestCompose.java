package test.com.anyun.host;

import org.junit.Test;

/**
 * Created by sxt on 16-12-7.
 */
public class TestCompose  extends BaseComponentTest {

    @Test
    public  void    template()
    {


         String  json="{\n" +
                 "        \"networks\": [\n" +
                 "            {\n" +
                 "                \"networkLabel\": \"test_network_1\", \n" +
                 "                \"driver\": \"ovn\", \n" +
                 "                \"ipRange\": \"172.20.0.0/16\", \n" +
                 "                \"gateway\": \"172.20.0.254\"\n" +
                 "            }, \n" +
                 "            {\n" +
                 "                \"networkLabel\": \"test_network_2\", \n" +
                 "                \"driver\": \"bridge\", \n" +
                 "                \"ipRange\": \"192.168.1.0/24\", \n" +
                 "                \"gateway\": \"192.168.1.1\"\n" +
                 "            }\n" +
                 "        ], \n" +
                 "        \"volumes\": [\n" +
                 "            {\n" +
                 "                \"volumeName\": \"test_volume_1\", \n" +
                 "                \"size\": \"500G\", \n" +
                 "                \"description\": \"data1\"\n" +
                 "            },\n" +
                 "\t\t\t{\n" +
                 "                \"volumeName\": \"test_volume_2\", \n" +
                 "                \"size\": \"200G\", \n" +
                 "                \"description\": \"data2\"\n" +
                 "            }\n" +
                 "        ], \n" +
                 "        \"containers\": [\n" +
                 "            {\n" +
                 "                \"name\": \"test_container_1\", \n" +
                 "                \"image_id\": \"94a7d85b02275de6\",\n" +
                 "\t\t\"image\": \"imagehub/ubuntu:16.04\",\n" +
                 "                \" cpu_core_limit\": \"4\", \n" +
                 "                \" memory_limit\": \"4\", \n" +
                 "                \" memory_swap_limit\": \"8\", \n" +
                 "                \" cpu_family\": \"E5645  @ 2.40GHz\", \n" +
                 "                \" replicas\": \"2\", \n" +
                 "                \" size\": \"8G\", \n" +
                 "                \" hostname\": \"test_hostname\", \n" +
                 "\t\t\" privileged\": \"true\",\n" +
                 "\t\t\" cap_add\": \"SYS_MODULE,\", \n" +
                 "                \" volume\": \"test_volume_1:/data\", \n" +
                 "                \" network\": \"test_network_1\"\n" +
                 "            },\n" +
                 "\t\t\t{\n" +
                 "                \"name\": \"test_container_2\", \n" +
                 "\t\t\"image_id\": \"94a7d85b02275de6\",\n" +
                 "                \"image\": \"imagehub/mysql:latest\", \n" +
                 "                \" cpu_core_limit\": \"4\", \n" +
                 "                \" memory_limit\": \"4\", \n" +
                 "                \" memory_swap_limit\": \"8\", \n" +
                 "                \" cpu_family\": \"E5645  @ 2.40GHz\", \n" +
                 "                \" replicas\": \"1\", \n" +
                 "                \" size\": \"8G\", \n" +
                 "                \" hostname\": \"test_hostname\", \n" +
                 "                \" volume\": \"test_volume_2:/data/mysql\", \n" +
                 "                \" network\": \"test_network_1\"\n" +
                 "            },\n" +
                 "\t\t\t{\n" +
                 "                \"name\": \"test_container_3\", \n" +
                 "\t\t\"image_id\": \"94a7d85b02275de6\",\n" +
                 "                \"image\": \"imagehub/nginx:latest\", \n" +
                 "                \" cpu_core_limit\": \"4\", \n" +
                 "                \" memory_limit\": \"4\", \n" +
                 "                \" memory_swap_limit\": \"8\", \n" +
                 "                \" cpu_family\": \"E5645  @ 2.40GHz\", \n" +
                 "                \" replicas\": \"1\", \n" +
                 "                \" size\": \"8G\", \n" +
                 "                \" hostname\": \"test_hostname\", \n" +
                 "                \" network\": \"test_network_1,test_network_2\"\n" +
                 "            }\n" +
                 "        ]\n" +
                 "    }, ";


        System.out.println(json);






    }




}
