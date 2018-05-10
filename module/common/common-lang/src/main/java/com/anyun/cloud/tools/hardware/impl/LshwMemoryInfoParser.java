/*
 *
 *      LshwMemoryInfoParser.java
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
import com.anyun.cloud.tools.hardware.MemoryInfoEntity;
import org.w3c.dom.Element;
/**
 * Created by TwitchGG on 9/24/15.
 */
public class LshwMemoryInfoParser extends LshwXmlParser<MemoryInfoEntity> {

    public LshwMemoryInfoParser() {
        super("memory");
    }

    @Override
    protected MemoryInfoEntity nodeParser(Element node) {
        String physid = ((Element) node.getElementsByTagName("physid").item(0)).getTextContent();
        String size = ((Element) node.getElementsByTagName("size").item(0)).getTextContent();
        MemoryInfoEntity entity = new MemoryInfoEntity();
        entity.setPhysid(physid);
        entity.setSize(Long.valueOf(size));
        return entity;
    }

}
