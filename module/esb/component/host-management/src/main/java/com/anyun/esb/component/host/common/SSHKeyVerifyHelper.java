package com.anyun.esb.component.host.common;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.size;
import static com.google.common.io.BaseEncoding.base64;

/**
 * Created by mr-tan on 16-6-15.
 */
public class SSHKeyVerifyHelper {
    public static void verify(String Key) throws Exception {

        ByteSource byteSource = ByteSource.wrap(Key.getBytes());
        publicKeySpecFromOpenSSH(byteSource);
    }

    public static String toStringAndClose(InputStream input) throws IOException {
        checkNotNull(input, "input");
        try {
            return CharStreams.toString(new InputStreamReader(input, Charsets.UTF_8));
        } finally {
            Closeables.closeQuietly(input);
        }
    }


    public static void publicKeySpecFromOpenSSH(ByteSource supplier) throws Exception {
        InputStream stream = supplier.openStream();
        Iterable<String> parts = Splitter.on(' ').split(toStringAndClose(stream).trim());
        checkArgument(size(parts) >= 2 && "ssh-rsa".equals(get(parts, 0)),
                "bad format, should be: ssh-rsa AAAAB3...");
        stream = new ByteArrayInputStream(base64().decode(get(parts, 1)));
        String marker = new String(readLengthFirst(stream));
        checkArgument("ssh-rsa".equals(marker), "looking for marker ssh-rsa but got %s", marker);
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
}
