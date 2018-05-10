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

package com.anyun.cloud.tools.cert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mr-tan on 16-3-9.
 */
public class RandomGenerator {

    public static BigInteger generateCertSeed(){

        List<String> list = new ArrayList<String>();

        while (list.size()<=50){

            StringBuilder builder = new StringBuilder();

            list.add(getNumberString(builder));
        }

        int size = list.size();

        return  new BigInteger(list.get(new Random().nextInt(size)));
    }

    public static String getNumberString (StringBuilder builder){

        if(builder==null){
            return null;
        }
        if(builder.length() == 48){

            return builder.toString();

        }

        builder.append(new Random().nextInt(9));
        return getNumberString(builder);
    }


    public static void main(String[] args) {
        System.out.println(new BigInteger("f75e80839b9b9379f1cf1128f321639757dba514642c206bbbd99f9a4846208b3e93fbbe5e0527cc59b1d4b929d9555853004c7c8b30ee6a213c3d1bb7415d03",16));
    }
}
