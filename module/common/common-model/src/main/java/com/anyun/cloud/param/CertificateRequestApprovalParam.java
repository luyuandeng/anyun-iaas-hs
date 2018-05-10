/*
 *
 *      CertificateRequestApprovalParam.java
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

package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/28/16
 */
public class CertificateRequestApprovalParam extends AbstractEntity{
    public static int STATUS_APPROVED = 1;
    public static int STATUS_REFUSED = 2;

    private CertificateRequestApprovalType requestType = CertificateRequestApprovalType.USER;
    private String requestId;
    private int status;
    private String resason;

    public CertificateRequestApprovalType getRequestType() {
        return requestType;
    }

    public void setRequestType(CertificateRequestApprovalType requestType) {
        this.requestType = requestType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResason() {
        return resason;
    }

    public void setResason(String resason) {
        this.resason = resason;
    }

    public enum CertificateRequestApprovalType {
        USER,SERVER
    }
}
