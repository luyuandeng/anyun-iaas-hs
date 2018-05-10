/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.api.jaxrs.request;

import org.apache.camel.Exchange;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/12/16
 */
public class SimpleMultiPartFileHandler implements MultiPartFileHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMultiPartFileHandler.class);

    @Override
    public Object process(List<MultiPartInputStreamParser.MultiPart> files, HttpServletRequest servletRequest, Exchange exchange) throws Exception{
        LOGGER.debug("***********************************************************************************************");
        for (MultiPartInputStreamParser.MultiPart multiPart : files) {
            String uploadName = multiPart.getName();
            String fileName = multiPart.getFile().getName();
            String filePath = multiPart.getFile().getAbsolutePath();
            String contentType = multiPart.getContentType();
            LOGGER.debug("File [" + uploadName + "] [" + fileName + "] [" + contentType +"]: " + filePath);
            BufferedReader read = new BufferedReader(new InputStreamReader(multiPart.getInputStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line = read.readLine()) != null) {
                sb.append(line).append("\n");
            }
            LOGGER.debug("Content [\n{}\n]",sb.toString());
    }
        return "success";
    }

    @Override
    public boolean isChain() {
        return false;
    }
}
