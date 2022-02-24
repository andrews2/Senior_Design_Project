package com.example.iotcasinoapp;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DataEncryption {
    private static final String encKey = "z$C&F)J@NcRfUjXn2r5u8x/A%D*G-KaP";
    private static byte[] iv = "0000000000000000".getBytes();
    public static SecretKey generateKey() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        return new SecretKeySpec(encKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public static String encrypt(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, ShortBufferException {
        byte[] input= message.getBytes("utf-8");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
        byte[] cipherText = cipher.doFinal(input);
        String result = new String(Hex.encodeHex(cipherText));
        return result;
    }

}
