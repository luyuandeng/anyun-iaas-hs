/*
 *
 *      CertificateService.java
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

package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.MetaDataKey;
import com.anyun.cloud.dto.UserCertificateRequestDto;
import com.anyun.cloud.param.CertificateRequestApprovalParam;
import com.anyun.cloud.param.CertificateRequestQueryParam;
import com.anyun.cloud.param.UserCertificateRequestParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.cert.ocsp.SingleResp;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/22/16
 */
public interface CertificateService {

    /**
     * 根据查询条件查询证书请求列表（带分页查询条件）
     *
     * @param condition
     * @return
     * @throws RestfulApiException
     */
    List<UserCertificateRequestDto> queryCertificateRequestByCondition(
            CertificateRequestQueryParam condition) throws RestfulApiException;
    /**
     * 证书请求审批
     *
     * @param param
     * @return
     * @throws RestfulApiException
     */
    MetaDataKey<String> approvalCertificateRequest(CertificateRequestApprovalParam param) throws RestfulApiException;

    /**
     * 生成用户证书请求
     *
     * @param userId    用户ID
     * @return
     * @throws RestfulApiException
     */
    Id<String> generateUserCertificateRequest(String userId) throws RestfulApiException;

    /**
     * OCSP证书状态验证
     *
     * @param certificates
     * @return
     * @throws RestfulApiException
     */
    List<SingleResp> validateCertificateStatus(List<X509Certificate> certificates) throws RestfulApiException;

    /**
     * 通过输入流获取X.509证书
     *
     * @param inputStream
     * @return
     * @throws RestfulApiException
     */
    X509Certificate parseCertificate(InputStream inputStream) throws RestfulApiException;

    /**
     * 证书吊销
     *
     * @param serialNumbers
     * @param reason
     * @return
     * @throws RestfulApiException
     */
    MetaDataKey<String> revocationCertificate(List<String> serialNumbers, CRLReason reason) throws RestfulApiException;

}
