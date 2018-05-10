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

package test.com.anyun.host;

import com.anyun.esb.component.host.common.Env;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Assert;
import org.junit.Before;
import redis.clients.jedis.Jedis;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class BaseComponentTest extends Assert {
    protected TestEndpoint endpoint;
    protected Exchange exchange;

    @Before
    public void init() throws Exception {
        System.out.println("==================================开始测试=======================================");
        Jedis jedis = new Jedis("redis", 6379);
        Env.export(Jedis.class, jedis);
        endpoint = new TestEndpoint();
        exchange = new DefaultExchange(endpoint);
    }
}
