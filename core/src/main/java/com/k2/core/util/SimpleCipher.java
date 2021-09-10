/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

public class SimpleCipher
{
    private static SecretKeySpec syskey = null;

    private static void makeKey()
    {
        String password = "YuWllNverGessThs";
        syskey = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static String encrypt(String string)
    {
        try
        {
            if (syskey == null)
            {
                makeKey();
            }
            return encrypt(string, syskey);
        }
        catch (GeneralSecurityException ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    public static String encrypt(String property, SecretKeySpec inkey) throws GeneralSecurityException
    {
        if (inkey == null) return "";
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, inkey);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    public static String base64Encode(byte[] bytes)
    {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String string)
    {
        try
        {
            if (syskey == null)
            {
                makeKey();
            }

            return decrypt(string, syskey);
        }
        catch (GeneralSecurityException ex)
        {
            ex.printStackTrace();
        }

        return "";
    }

    public static String decrypt(String string, SecretKeySpec inkey) throws GeneralSecurityException
    {
        if (inkey == null) return "";
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, inkey, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    public static byte[] base64Decode(String property)
    {
        return Base64.getDecoder().decode(property);
    }

    public static String bytesToHex(byte[] hash)
    {
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash)
        {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String hash(String inString, String algo)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance(algo);
            byte[] encodedhash = digest.digest(inString.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(encodedhash);
        }
        catch (GeneralSecurityException ex)
        {
            ex.printStackTrace();
        }
        return "";
    }

}