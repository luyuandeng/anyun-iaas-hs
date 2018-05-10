/*
 *
 *      MountCommand.java
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
package com.anyun.cloud.tools.bash;

import com.anyun.cloud.tools.StringUtils;

/**
 *
 * @author ray
 */
public class MountCommand implements LinuxCommand {

    private final String src;
    private final String dest;
    private final String type;

    public MountCommand() {
        this.src = null;
        this.dest = null;
        this.type = null;
    }

    public MountCommand(String type, String src, String dest) {
        this.type = type;
        this.src = src;
        this.dest = dest;
    }

    @Override
    public String exec() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("mount");
        if (StringUtils.isNotEmpty(type)) {
            sb.append(" -t ").append(type);
        }
        sb.append(" ").append(src);
        sb.append(" ").append(dest);
        BashCommand command = new BashCommand(sb.toString());
        String result = command.exec();
        if (command.getResult().getExitValue() != 0) {
            throw new Exception(result);
        }
        return result;
    }

    public void umountf(String umountPath) {
        new BashCommand("fuser -km " + umountPath).exec();
        new BashCommand("umount " + umountPath).exec();
    }

    public boolean isMounted(String srcPath) {
        String sc = "mount | grep '^" + srcPath + " on'";
        String result = new BashCommand(sc).exec();
        return !StringUtils.isEmpty(result);
    }

    public MountDiskInfo getLocalMountInfo(String srcPath) {
        String sc = "df -h " + srcPath.trim() + "| sed 1d | awk -F ' ' '{print $1,$2,$3,$4,$5,$6}'";
        BashCommand command = new BashCommand(sc);
        String result = command.exec();
        if (command.getException() != null) {
            return null;
        }
        if (result.trim().endsWith("No such file or directory")) {
            return null;
        }
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        String[] info = StringUtils.getSplitValues(result.trim(), " ");
        MountDiskInfo mdi = new MountDiskInfo();
        mdi.setSrc(info[0]);
        mdi.setSize(info[1]);
        mdi.setUsed(info[2]);
        mdi.setAvail(info[3]);
        mdi.setUse(info[4]);
        mdi.setMountedOn(info[5]);
        return mdi;
    }

    public boolean exist(String srcPath) {
        String sc = "mount | grep '^" + srcPath + " on'";
        BashCommand bc = new BashCommand(src);
        String rest = bc.exec();
        if (StringUtils.isEmpty(rest)) {
            return false;
        }
        if (rest.trim().startsWith(srcPath + "on")) {
            return true;
        }
        return false;
    }

    public static class MountDiskInfo {

        private String src;
        private String size;
        private String used;
        private String avail;
        private String use;
        private String mountedOn;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }

        public String getAvail() {
            return avail;
        }

        public void setAvail(String avail) {
            this.avail = avail;
        }

        public String getUse() {
            return use;
        }

        public void setUse(String use) {
            this.use = use;
        }

        public String getMountedOn() {
            return mountedOn;
        }

        public void setMountedOn(String mountedOn) {
            this.mountedOn = mountedOn;
        }

        @Override
        public String toString() {
            return "MountDiskInfo{" +
                    "src='" + src + '\'' +
                    ", size='" + size + '\'' +
                    ", used='" + used + '\'' +
                    ", avail='" + avail + '\'' +
                    ", use='" + use + '\'' +
                    ", mountedOn='" + mountedOn + '\'' +
                    '}';
        }
    }
}
