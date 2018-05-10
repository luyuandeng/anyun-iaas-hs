
import com.anyun.cloud.host.startup.DaemonStartup;
import com.anyun.cloud.host.startup.StartupClass;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.startup.StartupOption;

import java.util.HashMap;
import java.util.Map;

/*
 *
 *      Main.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * @author TwitchGG
 * @version 1.0
 * @date 2015-5-11
 */
public class Main {

    private static final Map<String, StartupClass> map = new HashMap<>();

    static {
        map.put("DAEMON", new DaemonStartup());
    }

    public static void main(String[] args) {
        try {
            StartupOption.getOption().init(args);  //初始化启动信息
            String startupType = StartupOption.getOption().getValue(StartupOption.STARTUP_TYPE);  //获取map中对应的alue
            if (StringUtils.isEmpty(startupType)) {
                System.exit(1);
            }
            /**
             * 获取对应的class类，并执行startup方法；
             */
            StartupClass startupClass = map.get(startupType);
            if (startupClass != null) {
                /**
                 * 开始执行 DaemonStartup 类中的startup 方法
                 */
                startupClass.startup();
            } else {
                throw new UnsupportedOperationException(startupType);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
