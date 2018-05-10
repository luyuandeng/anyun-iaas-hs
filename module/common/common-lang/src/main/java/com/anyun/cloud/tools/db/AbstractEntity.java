/*
 *
 *      AbstractEntity.java
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.anyun.cloud.tools.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 *
 * @author TwitchGG
 * @date 2015-4-26
 * @version 1.0
 */
public class AbstractEntity implements BaseEntity<AbstractEntity>,Serializable{
    
    @Override
    public String asJson() {
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        gb.setPrettyPrinting();
        Gson gson = gb.create();
        return gson.toJson(this);
    }

    @Override
    public AbstractEntity build() {
        return this;
    }
}
