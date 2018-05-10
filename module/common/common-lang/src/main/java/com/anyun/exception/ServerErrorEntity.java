/*
 *
 *      ServerErrorEntity.java
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

package com.anyun.exception;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/21/16
 */
public class ServerErrorEntity extends AbstractEntity {
    private ServerError error;

    public ServerError getError() {
        return error;
    }

    public void setError(ServerError error) {
        this.error = error;
    }

    public static class ServerError {
        private int code;
        private int error_subcode;
        private String error_user_msg;
        private String error_user_title;
        private String message;
        private String type;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getError_subcode() {
            return error_subcode;
        }

        public void setError_subcode(int error_subcode) {
            this.error_subcode = error_subcode;
        }

        public String getError_user_msg() {
            return error_user_msg;
        }

        public void setError_user_msg(String error_user_msg) {
            this.error_user_msg = error_user_msg;
        }

        public String getError_user_title() {
            return error_user_title;
        }

        public void setError_user_title(String error_user_title) {
            this.error_user_title = error_user_title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}


