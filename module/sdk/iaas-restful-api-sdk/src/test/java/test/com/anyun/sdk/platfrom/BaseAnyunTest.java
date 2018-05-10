/*
 *
 *      BaseAnyunTest.java
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

package test.com.anyun.sdk.platfrom;

import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.Configuration;
import org.junit.Assert;
import org.junit.Before;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/28/16
 */
public class BaseAnyunTest extends Assert {

    @Before
    public void setup() throws Exception{
        System.out.println("==================================开始测试=======================================");
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        //初始化客户端,只要初始化一次
        Configuration configuration = new Configuration();
        configuration.setPlatformAddress("172.18.3.5");
//       configuration.setPlatformAddress("api1.esb.iaas.anyuncloud.com");
        configuration.setPort(8989);
        configuration.setBaseUrl("/rest/v1");
        factory.config(configuration);
    }
}
