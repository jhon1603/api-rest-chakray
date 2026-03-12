package com.example.apirestchakray.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public final class AesUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET = "ChakraySecretKey2026";

    private AesUtil() {
    }

    public static String encrypt(String value) {
        try {
            SecretKeySpec secretKey = buildKey(SECRET);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible cifrar el password", ex);
        }
    }

    public static boolean matches(String rawValue, String encryptedValue) {
        return encrypt(rawValue).equals(encryptedValue);
    }

    private static SecretKeySpec buildKey(String secret) throws Exception {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32);
        return new SecretKeySpec(key, ALGORITHM);
    }
}