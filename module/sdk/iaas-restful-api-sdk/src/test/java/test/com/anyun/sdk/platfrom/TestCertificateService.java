/*
 *
 *      TestCertificateService.java
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
import com.anyun.cloud.api.MetaDataKey;
import com.anyun.cloud.dto.UserCertificateRequestDto;
import com.anyun.cloud.param.CertificateRequestApprovalParam;
import com.anyun.cloud.param.CertificateRequestQueryParam;
import com.anyun.sdk.platfrom.CertificateService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/28/16
 */
public class TestCertificateService extends BaseAnyunTest {
    private static Logger LOGGER = LoggerFactory.getLogger(TestCertificateService.class);
    private CertificateService certificateService;

    @Before
    public void setUp() throws Exception {
        certificateService = AnyunSdkClientFactory.getFactory().getCertificateService();
    }

    @Test
    public void testQueryCertificateRequestByCondition() throws Exception {
        LOGGER.info("用户证书请求按条件检索测试");
        CertificateRequestQueryParam condition = new CertificateRequestQueryParam();
        condition.setPageNumber(1);
        condition.setPageSize(10);
        condition.setStatus(2);
        condition.setUserName("");
        List<UserCertificateRequestDto> requestDtos = certificateService.queryCertificateRequestByCondition(condition);
        Assert.assertNotNull(requestDtos);
        for (UserCertificateRequestDto requestDto : requestDtos) {
            LOGGER.debug("DTO [{}]", requestDto.asJson());
        }
    }

    @Test
    public void testUserCertificateRequestApproval() throws Exception {
        LOGGER.info("用户证书请求审批测试");
        String requestId = "c1cee42705794f4e86a0618af3360baa";
        CertificateRequestApprovalParam certificateRequestApprovalParam = new CertificateRequestApprovalParam();
        certificateRequestApprovalParam.setRequestType(
                CertificateRequestApprovalParam.CertificateRequestApprovalType.USER);
        certificateRequestApprovalParam.setStatus(CertificateRequestApprovalParam.STATUS_REFUSED);
        certificateRequestApprovalParam.setRequestId(requestId);
        certificateRequestApprovalParam.setResason("不给申请");
        MetaDataKey<String> id = certificateService.approvalCertificateRequest(certificateRequestApprovalParam);
        Assert.assertNotNull(id);
    }

    @Test
    public void testGenerateUserCertificateRequest() throws Exception {
        LOGGER.info("用户证书请求生成测试");
        String userId = "f4b7cf3bab3d44d080cc59e7a88450ab";
        Id<String> id = certificateService.generateUserCertificateRequest(userId);
        Assert.assertNotNull(id);
    }

    @Test
    public void testX509CertificateParser() throws Exception {
        LOGGER.info("证书通过流方式读取转换测试");
        String testCertPath = "/Users/twitchgg/Downloads/server.crt";
        FileInputStream fileInputStream = new FileInputStream(testCertPath);
        X509Certificate certificate = certificateService.parseCertificate(fileInputStream);
        Assert.assertNotNull(certificate);
        Assert.assertNotNull(certificate.getSerialNumber());
    }

    @Test
    public void testSingleCertOcspValidate() throws Exception {
        LOGGER.info("单个证书的OCSP验证测试");
        String testCertPath = "/Users/twitchgg/Desktop/公安CA.crt";
        FileInputStream fileInputStream = new FileInputStream(testCertPath);
        X509Certificate certificate = certificateService.parseCertificate(fileInputStream);
        LOGGER.debug("Serial number [{}]", certificate.getSerialNumber());
        List<SingleResp> singleResps = certificateService.validateCertificateStatus(Arrays.asList(certificate));
        Assert.assertNotNull(singleResps.get(0));
        for (SingleResp resp : singleResps) {
            String serialNumber = resp.getCertID().getSerialNumber().toString();
            LOGGER.debug("Certificate serial number [{}]", serialNumber);
            if (resp.getCertStatus() == null) {
                LOGGER.debug("Certificate [{}] is good", serialNumber);
            } else if (resp.getCertStatus() instanceof RevokedStatus) {
                RevokedStatus revokedStatus = (RevokedStatus) resp.getCertStatus();
                Date revocationTime = revokedStatus.getRevocationTime();
                int revocationReason = revokedStatus.getRevocationReason();
                LOGGER.debug("Certificate [" + serialNumber + "] is revoked," +
                        "revocation time [{}]\nrevocation reason[{}]", revocationTime, revocationReason);
            } else {
                LOGGER.debug("Certificate status type [{}]", resp.getCertStatus().getClass().getName());
            }
        }
    }

    @Test
    public void testMultiCertsOcspValidate() throws Exception {
        LOGGER.info("多个证书的OCSP验证测试");
        String cp1 = "/Users/twitchgg/Downloads/test.crt";
        String cp2 = "/Users/twitchgg/Downloads/test.crt";
        FileInputStream f1 = new FileInputStream(cp1);
        FileInputStream f2 = new FileInputStream(cp2);
        X509Certificate c1 = certificateService.parseCertificate(f1);
        X509Certificate c2 = certificateService.parseCertificate(f2);

        List<X509Certificate> certificates = Arrays.asList(c1, c2);
        List<SingleResp> singleResps =
                certificateService.validateCertificateStatus(certificates);
        Assert.assertEquals(certificates.size(), singleResps.size());
        for (SingleResp resp : singleResps) {
            String serialNumber = resp.getCertID().getSerialNumber().toString();
            LOGGER.debug("Certificate serial number [{}]", serialNumber);
            if (resp.getCertStatus() == null) {
                LOGGER.debug("Certificate [{}] is good", serialNumber);
            } else if (resp.getCertStatus() instanceof RevokedStatus) {
                RevokedStatus revokedStatus = (RevokedStatus) resp.getCertStatus();
                Date revocationTime = revokedStatus.getRevocationTime();
                int revocationReason = revokedStatus.getRevocationReason();
                LOGGER.debug("Certificate [" + serialNumber + "] is revoked," +
                        "revocation time [{}]\nrevocation reason[{}]", revocationTime, revocationReason);
            } else {
                LOGGER.debug("Certificate status type [{}]", resp.getCertStatus().getClass().getName());
            }
        }
    }

    @Test
    public void testCertificateRevocation() throws Exception {
        LOGGER.info("证书批量吊销测试");
        List<String> certificateSerialNumbers = new ArrayList<>();
        certificateSerialNumbers.add("1458978760395");
        CRLReason reason = CRLReason.lookup(CRLReason.removeFromCRL);
        MetaDataKey<String> status = certificateService.revocationCertificate(certificateSerialNumbers, reason);
        Assert.assertEquals("certificate revocation success", status.getKey());
    }
}
