/*
 *
 *      RuntimeUtil.java
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anyun.cloud.tools;

import com.anyun.cloud.tools.bash.BashCommand;
import com.anyun.cloud.tools.startup.StartupOption;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ray
 */
public class RuntimeUtil {

    public static int getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        int index = name.indexOf("@");
        int pid = 0;
        if (index != -1) {
            pid = Integer.parseInt(name.substring(0, index));
        }
        return pid;
    }

    public static boolean isExceedThread(int maxCount) {
        String threadCountPath = StartupOption.getOption().getValue(StartupOption.WORK_DIR) + "/etc/service/thread";
        int count = 0;
        if (FileUtil.exist(threadCountPath, FileUtil.FileType.FILE)) {
            try {
                count = Integer.valueOf(FileUtil.cat(threadCountPath, "utf-8").trim());
                count = count > maxCount ? maxCount : count;
            } catch (Exception e) {
                count = maxCount;
            }
        }
        if (count == 0) {
            count = maxCount;
        }
        return getRunCliPids().size() > count;
    }

    public static List<String> getRunCliPids() {
        String script = "PID=$(jps | grep 'HostManagement.jar$' | awk -F ' ' '{print $1}') && echo $PID";
        List<String> runners = new ArrayList<>();
        String result = new BashCommand(script).exec().trim();
        String servicePid = getServicePid();
        for (String runner : StringUtils.getSplitValues(result.trim(), " ")) {
            if (StringUtils.isEmpty(runner) || runner.equals(servicePid)) {
                continue;
            }
            runners.add(runner);
        }
        return runners;
    }

    public static String getServicePid() {
        String pidPath = StartupOption.getOption().getValue(StartupOption.WORK_DIR) + "/run/service";
        String pid = "0";
        if (FileUtil.exist(pidPath, FileUtil.FileType.FILE)) {
            try {
                pid = FileUtil.cat(pidPath, "utf-8").trim();
            } catch (Exception e) {

            }
        }
        return pid;
    }
}
