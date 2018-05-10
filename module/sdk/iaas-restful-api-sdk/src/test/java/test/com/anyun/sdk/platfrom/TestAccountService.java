/*
 *
 *      TestAccountService.java
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

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.account.Organization;
import com.anyun.cloud.dto.UserDto;
import com.anyun.cloud.param.OrganizationAddParam;
import com.anyun.cloud.param.UserAddParam;
import com.anyun.sdk.platfrom.AccountService;
import com.anyun.sdk.platfrom.OrganizationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/23/16
 */
public class TestAccountService extends BaseAnyunTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TestAccountService.class);
    public static final String TEST_USER_MAIL = "tester@anyuncloud.com";
    public static final String TEST_USER_LOGIN_NAME = "TestUserLoginName";
    public static final String TEST_USER_REAL_NAME = "TestUser";
    public static final String TEST_USER_LOGIN_PASSWD = "passwd";
    public static final String TEST_USER_MOBILE = "13235186672";

    private OrganizationService organizationService;
    private AccountService accountService;

    @Before
    public void setUp() throws Exception {
        AnyunSdkClientFactory factory = AnyunSdkClientFactory.getFactory();
        organizationService = factory.getOrganizationService();
        accountService = factory.getAccountService();
        ResourceClient.setUserToken("debug_token");
    }

    ////////////////////////////////用户测试//////////////////////////////////////////////////
    @Test
    public void testUserLoginByPassword() throws Exception {
        String userName = "wangjian";
        byte[] passwd = "passwd".getBytes();
        UserDto userDto = accountService.login(userName, passwd);
        Assert.assertNotNull(userDto);
    }

    @Test
    public void testAddUser() throws Exception {
        UserAddParam user = buildTestAddUserParam();
        Id<String> userId = accountService.createUser(user);
        Assert.assertNotNull(userId);
    }

    ////////////////////////////////组织测试//////////////////////////////////////////////////
    @Test
    public void testAddOrganization() throws Exception {
        OrganizationAddParam organizationAddParam = buildTestAddParam();
        Id<String> id = organizationService.addOrganization(organizationAddParam);
        Assert.assertNotNull(id);
        LOGGER.debug("添加的组织ID [{}]", id.getId());
    }

    @Test
    public void testGetOrganizationInfo() throws Exception {
        String organizationId = "testId";
        Organization organization = organizationService.getOrganization(organizationId);
        Assert.assertNotNull(organization);
    }

    private UserAddParam buildTestAddUserParam() {
        UserAddParam user = new UserAddParam();
        user.setMail("twitchgg@yahoo.com");
        user.setUserName("TwitchGG1");
        user.setOrganization("c6f3c0b0b3d840ab8d075f9ff6ee92a7");
        user.setRealName("汤磊");
        return user;
    }

    private OrganizationAddParam buildTestAddParam() {
        OrganizationAddParam organizationAddParam = new OrganizationAddParam();
        organizationAddParam.setDescript("SDK测试组织");
        organizationAddParam.setName("测试的组织");
        organizationAddParam.setContactName(TEST_USER_LOGIN_NAME);
        organizationAddParam.setMail(TEST_USER_MAIL);
        organizationAddParam.setMobile(TEST_USER_MOBILE);
        return organizationAddParam;
    }
}
