/*
 *
 *      LshwCpuInfoParser.java
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

package com.anyun.cloud.tools.hardware.impl;

import com.anyun.cloud.tools.hardware.CpuInfoEntity;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/**
 * Created by TwitchGG on 9/24/15.
 */
public class LshwCpuInfoParser extends LshwXmlParser<CpuInfoEntity> {

    public LshwCpuInfoParser() {
        super("cpu");
    }

    @Override
    protected CpuInfoEntity nodeParser(Element node) {
        try {
            String product = ((Element) node.getElementsByTagName("product").item(0)).getTextContent();
            String vendor = ((Element) node.getElementsByTagName("vendor").item(0)).getTextContent();
            String businfo = ((Element) node.getElementsByTagName("businfo").item(0)).getTextContent();
            String width = ((Element) node.getElementsByTagName("width").item(0)).getTextContent();
            String capacity;
            try {
                capacity = ((Element) node.getElementsByTagName("capacity").item(0)).getTextContent();
            }catch (Exception ex) {
                capacity = "0";
            }
            CpuInfoEntity cpuInfoEntity = new CpuInfoEntity();
            cpuInfoEntity.setBusinfo(businfo);
            cpuInfoEntity.setCapacity(Long.valueOf(capacity));
            cpuInfoEntity.setProduct(product);
            cpuInfoEntity.setVendor(vendor);
            cpuInfoEntity.setWidth(Integer.valueOf(width));
            return cpuInfoEntity;
        } catch (Exception e) {
            return null;
        }
    }
}
