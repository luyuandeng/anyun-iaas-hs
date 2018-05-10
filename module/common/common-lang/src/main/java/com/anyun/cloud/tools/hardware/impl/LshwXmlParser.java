/*
 *
 *      LshwXmlParser.java
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

import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;
import com.anyun.cloud.tools.db.AbstractEntity;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Created by TwitchGG on 9/24/15.
 */
public abstract class LshwXmlParser<T extends AbstractEntity> {

    private String content;
    private DocumentBuilderFactory builderFactory;
    private Document document = null;
    private DocumentBuilder builder;
    private Element rootElement;

    public LshwXmlParser(String type) {
        if(StringUtils.isEmpty(content))
            content = getContent(type);
        if (content == null) {
            return;
        }
        builderFactory = DocumentBuilderFactory.newInstance();
        try {
            builder = builderFactory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(content.getBytes()));
            rootElement = document.getDocumentElement();
        } catch (ParserConfigurationException | SAXException | IOException ex) {
        }
    }

    public List<T> getHardwareInfoEntity() {
        List<T> entitys = new ArrayList<>();
        if (getContent() == null) {
            return entitys;
        }
        NodeList nodeList = getRootElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (!(node instanceof Element)) {
                continue;
            }
            T entity = nodeParser((Element) node);
            if (entity != null) {
                entitys.add(entity);
            }
        }
        return entitys;
    }

    protected abstract T nodeParser(Element node);

    private String getContent(String type) {
        BashCommand bashCommand = new BashCommand("lshw -xml -class " + type.toLowerCase());
        String xml = bashCommand.exec();
        if (bashCommand.getException() != null) {
            return null;
        }
        return xml;
    }

    public String getContent() {
        return content;
    }

    public DocumentBuilderFactory getBuilderFactory() {
        return builderFactory;
    }

    public Document getDocument() {
        return document;
    }

    public DocumentBuilder getBuilder() {
        return builder;
    }

    public Element getRootElement() {
        return rootElement;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
