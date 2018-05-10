/*
 *
 *      Success.java
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

package com.anyun.cloud.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class Successed {
    @SerializedName("success")
    private Response success;
    @SerializedName("processTime")
    private ProcessTime processTime;
    private String operate;

    public Response getSuccess() {
        return success;
    }

    public void setSuccess(Response success) {
        this.success = success;
    }

    public ProcessTime getProcessTime() {
        return processTime;
    }

    public void setProcessTime(ProcessTime processTime) {
        this.processTime = processTime;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }
}
