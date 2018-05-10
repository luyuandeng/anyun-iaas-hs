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

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.*;
import java.math.BigInteger;
import java.security.spec.RSAPublicKeySpec;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Splitter.fixedLength;
import static com.google.common.base.Throwables.propagate;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static com.google.common.io.BaseEncoding.base16;
import static com.google.common.io.BaseEncoding.base64;

/**
 * Created by mr-tan on 16-6-16.
 */
public class PublicKeyUtil {

    public static RSAPublicKeySpec publicKeySpecFromOpenSSH(String idRsaPub) {
        try {
            return publicKeySpecFromOpenSSH(ByteSource.wrap(idRsaPub.getBytes()));
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    public static String toStringAndClose(InputStream input) throws IOException {
        checkNotNull(input, "input");
        try {
            return CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8));
        } finally {
            Closeables.closeQuietly(input);
        }
    }

    private static byte[] readLengthFirst(InputStream in) throws IOException {
        int byte1 = in.read();
        int byte2 = in.read();
        int byte3 = in.read();
        int byte4 = in.read();
        int length = (byte1 << 24) + (byte2 << 16) + (byte3 << 8) + (byte4 << 0);
        byte[] val = new byte[length];
        ByteStreams.readFully(in, val);
        return val;
    }

    public static RSAPublicKeySpec publicKeySpecFromOpenSSH(ByteSource supplier)
            throws IOException {
        InputStream stream = supplier.openStream();
        Iterable<String> parts = Splitter.on(' ').split(toStringAndClose(stream).trim());
        checkArgument(size(parts) >= 2 && "ssh-rsa".equals(get(parts, 0)),
                "bad format, should be: ssh-rsa AAAAB3...");
        stream = new ByteArrayInputStream(base64().decode(get(parts, 1)));
        String marker = new String(readLengthFirst(stream));
        checkArgument("ssh-rsa".equals(marker), "looking for marker ssh-rsa but got %s", marker);
        BigInteger publicExponent = new BigInteger(readLengthFirst(stream));
        BigInteger modulus = new BigInteger(readLengthFirst(stream));
        return new RSAPublicKeySpec(modulus, publicExponent);
    }

    public static String fingerprintPublicKey(String publicKeyOpenSSH) {
        RSAPublicKeySpec publicKeySpec = publicKeySpecFromOpenSSH(publicKeyOpenSSH);
        return fingerprint(publicKeySpec.getPublicExponent(), publicKeySpec.getModulus());
    }

    public static String fingerprint(BigInteger publicExponent, BigInteger modulus) {
        byte[] keyBlob = keyBlob(publicExponent, modulus);
        return hexColonDelimited(Hashing.md5().hashBytes(keyBlob));
    }

    private static String hexColonDelimited(HashCode hc) {
        return on(':').join(fixedLength(2).split(base16().lowerCase().encode(hc.asBytes())));
    }

    private static byte[] keyBlob(BigInteger publicExponent, BigInteger modulus) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            writeLengthFirst("ssh-rsa".getBytes(), out);
            writeLengthFirst(publicExponent.toByteArray(), out);
            writeLengthFirst(modulus.toByteArray(), out);
            return out.toByteArray();
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private static void writeLengthFirst(byte[] array, ByteArrayOutputStream out) throws IOException {
        out.write((array.length >>> 24) & 0xFF);
        out.write((array.length >>> 16) & 0xFF);
        out.write((array.length >>> 8) & 0xFF);
        out.write((array.length >>> 0) & 0xFF);
        if (array.length == 1 && array[0] == (byte) 0x00)
            out.write(new byte[0]);
        else
            out.write(array);
    }
}
