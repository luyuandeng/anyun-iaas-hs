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

package com.anyun.exception.aaa;

import com.anyun.cloud.tools.execption.BaseRuntimeException;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/15/16
 */
public class BaseAAAException extends BaseRuntimeException {
    public BaseAAAException(int code, String message) {
        super(code, message);
    }

    public BaseAAAException(int code, Throwable throwable) {
        super(code, throwable);
    }

    public BaseAAAException(String message) {
        super(10000, message);
    }

    public BaseAAAException(Throwable throwable) {
        super(10000, throwable);
    }
}
