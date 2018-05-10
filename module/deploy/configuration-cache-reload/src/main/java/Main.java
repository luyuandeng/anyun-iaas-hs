
import com.anyun.cloud.cfg.BeanManagement;
import com.anyun.cloud.cfg.SystemPropertiesConfiguration;
import com.anyun.cloud.cfg.db.DatabaseFactory;

/**
 * @author TwitchGG
 * @version 1.0
 * @date 2015-5-11
 */
public class Main {
    public static void main(String[] args) {
        try {
            DatabaseFactory.getFactory().test();
            SystemPropertiesConfiguration systemPropertiesConfiguration
                    = BeanManagement.getManagement().getBean(SystemPropertiesConfiguration.class);
            systemPropertiesConfiguration.reload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
