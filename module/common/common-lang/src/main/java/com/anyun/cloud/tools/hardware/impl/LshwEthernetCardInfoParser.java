/*
 *
 *      LshwEthernetCardInfoParser.java
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
import com.anyun.cloud.tools.hardware.EthernetCardInfoEntity;
import org.w3c.dom.Element;

/**
 * Created by TwitchGG on 9/24/15.
 */
public class LshwEthernetCardInfoParser extends LshwXmlParser<EthernetCardInfoEntity> {

    public LshwEthernetCardInfoParser() {
        super("network");
    }

    @Override
    protected EthernetCardInfoEntity nodeParser(Element node) {
        try {
            String product = ((Element) node.getElementsByTagName("product").item(0)).getTextContent();
            String vendor = ((Element) node.getElementsByTagName("vendor").item(0)).getTextContent();
            String businfo = ((Element) node.getElementsByTagName("businfo").item(0)).getTextContent();
            String version = ((Element) node.getElementsByTagName("version").item(0)).getTextContent();
            String width = ((Element) node.getElementsByTagName("width").item(0)).getTextContent();
            String clock = ((Element) node.getElementsByTagName("clock").item(0)).getTextContent();
            EthernetCardInfoEntity ethernetCardInfoEntity = new EthernetCardInfoEntity();
            ethernetCardInfoEntity.setBusinfo(businfo);
            ethernetCardInfoEntity.setClock(Long.valueOf(clock));
            ethernetCardInfoEntity.setProduct(product);
            ethernetCardInfoEntity.setVendor(vendor);
            ethernetCardInfoEntity.setVersion(version);
            ethernetCardInfoEntity.setWidth(Long.valueOf(width));
            return ethernetCardInfoEntity;
        } catch (Exception e) {
            return null;
        }
    }
}
