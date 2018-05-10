package test.com.anyun.host;

import com.anyun.cloud.tools.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.hashids.Hashids;

/**
 * Created by twitchgg on 16-7-19.
 */
public class TestHashId {
    public static void main(String[] args) {
//        Hashids hashids = new Hashids(StringUtils.uuidGen());
//        String sid = "anyun-" + hashids.encode(System.nanoTime());
//        System.out.println(sid);
        //1.创建项目
        //2.创建项目虚拟交换机(调用VSwitchService.void createOVNNetwork(String short_id, String subnet, String gateway) throws Exception)
        //  2.1 创建成功的虚拟交换机和项目关联
        //  2.2 根据项目名称和nanotime创建虚拟交换机短ID,作为虚拟交换机name
        //      Hashids hashids = new Hashids(“项目名称”);
        //      String short_id = "anyun-" + hashids.encode(System.nanoTime());
        //3.在页面对项目虚拟机划分网络，将虚拟机划分到不同的虚拟交换机，一对多关系，除了项目虚拟交换机其他网络标签在平台配置初始化完成
    }
}
