/*
 *
 *      Id.java
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

import com.anyun.cloud.tools.db.AbstractEntity;
import com.google.gson.annotations.SerializedName;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class Id <T>  extends AbstractEntity{
    @SerializedName("id")
    private T id;

    public Id(T id) {
        this.id = id;
    }

    public Id() {
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
