/*
 *
 *      CertificateServiceImpl.java
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

package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.MetaDataKey;
import com.anyun.cloud.dto.UserCertificateRequestDto;
import com.anyun.cloud.param.CertificateRequestApprovalParam;
import com.anyun.cloud.param.CertificateRequestQueryParam;
import com.anyun.cloud.param.CertificateRevocationParam;
import com.anyun.cloud.param.UserCertificateRequestParam;
import com.anyun.sdk.platfrom.CertificateService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public class CertificateServiceImpl implements CertificateService {
    public static final String PATH_OCSP = "/cert/ocsp/validation";
    public static final String PATH_REVOCATION = "/cert/revocation";
    public static final String PATH_USER_REQ = "/cert/csr/user/request";
    public static final String PATH_USER_REQ_APPROVAL = "/cert/csr/approval";
    public static final String PATH_USER_REQ_QUERY_CONDITION = "/cert/request";

    @Override
    public List<UserCertificateRequestDto> queryCertificateRequestByCondition(
            CertificateRequestQueryParam condition) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object> params = new HashMap<>();
        params.put("status",condition.getStatus());
        params.put("userName",condition.getUserName());
        params.put("pageNumber",condition.getPageNumber());
        params.put("pageSize",condition.getPageSize());
        String response = rsClient.query(PATH_USER_REQ_QUERY_CONDITION, params);
        List<UserCertificateRequestDto> requests = ResourceClient.convertToListObject(UserCertificateRequestDto.class, response);
        return requests;
    }

    @Override
    public MetaDataKey<String> approvalCertificateRequest(CertificateRequestApprovalParam param)
            throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_USER_REQ_APPROVAL, param.asJson());
        MetaDataKey<String> approvalStatus = ResourceClient.convertToAnyunEntity(MetaDataKey.class, response);
        return approvalStatus;
    }

    @Override
    public Id<String> generateUserCertificateRequest(String userId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        UserCertificateRequestParam param = new UserCertificateRequestParam();
        param.setUserId(userId);
        String response = rsClient.post(PATH_USER_REQ, param.asJson());
        Id<String> csrId = ResourceClient.convertToAnyunEntity(Id.class, response);
        return csrId;
    }

    @Override
    public List<SingleResp> validateCertificateStatus(List<X509Certificate> certificates) throws RestfulApiException {
        List<SingleResp> singleResps = new ArrayList<>();
        OCSPReq ocspReq = null;
        try {
            ocspReq = generateOCSPRequest(certificates);
        } catch (Exception ex) {
            RestfulApiException se = new RestfulApiException(3000, 3000);
            se.setType(ex.getClass().getSimpleName());
            se.setUserMessage("X.509证书生解析器实例化错误");
            se.setMessage("X.509 certificate parser instance error [" + ex.getMessage() + "]");
            se.setUserTitle("X.509 certificate parser instance error");
            throw se;
        }
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        try {
            OCSPResp ocspResp = rsClient.ocspPost(PATH_OCSP, ocspReq);
            if (ocspResp.getStatus() != 0)
                throw new Exception("OCSP Response status[" + ocspResp.getStatus() + "]");
            BasicOCSPResp basicOCSPResp = (BasicOCSPResp) ocspResp.getResponseObject();
            SingleResp[] resps = basicOCSPResp.getResponses();
            for (SingleResp resp : resps) {
                singleResps.add(resp);
            }
        } catch (Exception ex) {
            RestfulApiException se = new RestfulApiException(3000, 3001);
            se.setType(ex.getClass().getSimpleName());
            se.setUserMessage("OCSP服务器验证错误 [" + ex.getMessage() + "]");
            se.setMessage("OCSP server error [" + ex.getMessage() + "]");
            se.setUserTitle("OCSP server error");
            throw se;
        }
        return singleResps;
    }

    @Override
    public X509Certificate parseCertificate(InputStream inputStream) throws RestfulApiException {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            Certificate certificate = certificateFactory.generateCertificate(inputStream);
            X509Certificate x509Certificate = (X509Certificate) certificate;
            inputStream.close();
            return x509Certificate;
        } catch (Exception ex) {
            RestfulApiException se = new RestfulApiException(3000, 3002);
            se.setType(ex.getClass().getSimpleName());
            se.setUserMessage("X.509证书生成错误 [" + ex.getMessage() + "]");
            se.setMessage("X.509 certificate generate error [" + ex.getMessage() + "]");
            se.setUserTitle("X.509 certificate generate error");
            throw se;
        }
    }

    @Override
    public MetaDataKey<String> revocationCertificate(List<String> serialNumbers, CRLReason reason)
            throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        CertificateRevocationParam param = new CertificateRevocationParam();
        param.setRevocationResason(reason.getValue().intValue());
        param.setSerialNumbers(serialNumbers);
        String response = rsClient.post(PATH_REVOCATION, param.asJson());
        MetaDataKey<String> revocationStatus = ResourceClient.convertToAnyunEntity(MetaDataKey.class, response);
        return revocationStatus;
    }

    public static OCSPReq generateOCSPRequest(List<X509Certificate> issuerCerts) throws Exception {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        OCSPReqBuilder ocspReqBuilder = new OCSPReqBuilder();

        DigestCalculator digestCalculator =
                new JcaDigestCalculatorProviderBuilder().setProvider("BC").build().get(CertificateID.HASH_SHA1);
        for (X509Certificate issuerCert : issuerCerts) {
            JcaX509CertificateHolder certificateHolder = new JcaX509CertificateHolder(issuerCert);
            BigInteger serialNumber = issuerCert.getSerialNumber();
            CertificateID certificateID = new CertificateID(digestCalculator, certificateHolder, serialNumber);
            ocspReqBuilder.addRequest(certificateID);
        }
        BigInteger bigInteger = BigInteger.valueOf(System.nanoTime());
        DEROctetString derOctetString = new DEROctetString(bigInteger.toByteArray());
        ASN1ObjectIdentifier extnId = OCSPObjectIdentifiers.id_pkix_ocsp_nonce;
        Extension extension = new Extension(extnId, true, derOctetString);
        ocspReqBuilder.setRequestExtensions(new Extensions(new Extension[]{extension}));
        return ocspReqBuilder.build();
    }
}
