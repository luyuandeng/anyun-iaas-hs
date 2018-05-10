/*
 *
 *      BaseHardwareInfoEntity.java
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

package com.anyun.cloud.tools.hardware;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by TwitchGG on 9/23/15.
 */
public class BaseHardwareInfoEntity extends AbstractEntity {

    private String product;
    private String vendor;
    private String businfo;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getBusinfo() {
        return businfo;
    }

    public void setBusinfo(String businfo) {
        this.businfo = businfo;
    }
}