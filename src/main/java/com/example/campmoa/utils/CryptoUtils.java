package com.example.campmoa.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoUtils {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final String DEFAULT_FALLBACK = null;

    public static String  hashSha512(String i) {
        return CryptoUtils.hashSha512(i, CryptoUtils.DEFAULT_FALLBACK, CryptoUtils.DEFAULT_CHARSET);
    }
    public static String hashSha512(String i, Charset c) {
        return CryptoUtils.hashSha512(i, CryptoUtils.DEFAULT_FALLBACK, c);
    }
    public static String hashSha512(String i, String f) {
        return CryptoUtils.hashSha512(i, f, CryptoUtils.DEFAULT_CHARSET);
    }

    public static String hashSha512(String i, String f, Charset c) {
        try {
            return CryptoUtils.hashSha512Unsafe(i,c);
        } catch (NoSuchAlgorithmException ignored) {
            return f;
        }
    }

    public static String hashSha512Unsafe(String i) throws NoSuchAlgorithmException {
        return CryptoUtils.hashSha512Unsafe(i, CryptoUtils.DEFAULT_CHARSET);
    }

    public static String hashSha512Unsafe(String i, Charset c) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(i.getBytes(c));
        StringBuilder hb = new StringBuilder();
        for (byte hashByte : md.digest()) {
            hb.append(String.format("%02x", hashByte));
        }
        return hb.toString();
    }

    public CryptoUtils() {}
}
