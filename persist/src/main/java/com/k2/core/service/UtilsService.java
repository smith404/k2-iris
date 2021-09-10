/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.service;

import com.k2.core.exception.BaseException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface UtilsService
{
    String DEFAULT_LOCALE = "en_ch";
    String NEWLINE = "\n";
    String TAB = "\t";
    String SPACE = " ";

    static String hashToHEX(String originalString)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(encodedhash);
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }

        return "hashfail";
    }

    static String bytesToHex(byte[] hash)
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

    static String makeHTMLExceptionMessage(Throwable ex)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("<strong>").append(ex.getClass().getSimpleName()).append("</strong>")
                .append("<br>").append("<i>").append(ex.getLocalizedMessage()).append("</i>");

        while (!(null == (ex = ex.getCause())))
        {
            sb.append("<hr>").append("<strong>").append(ex.getClass().getSimpleName()).append("</strong>")
                    .append("<br>").append("<i>").append(ex.getLocalizedMessage()).append("</i>");
        }

        return sb.toString();
    }

    static String makeExceptionWarning(Throwable ex, String domain)
    {
        return String.format("%s ** Exception %s : %s in %s::%s@%s",
                domain,
                ex.getClass().getName(),
                ex.getMessage(),
                ex.getStackTrace()[0].getClassName(),
                ex.getStackTrace()[0].getMethodName(),
                ex.getStackTrace()[0].getLineNumber());
    }

    static String makeExceptionWarning(Throwable ex)
    {
        return makeExceptionWarning(ex, "IRIS");
    }


    static BaseException makeBaseException(String description, Throwable ex)
    {
        String message = String.format("Exception %s : %s in %s::%s@%s",
                description,
                ex.getMessage(),
                ex.getStackTrace()[0].getClassName(),
                ex.getStackTrace()[0].getMethodName(),
                ex.getStackTrace()[0].getLineNumber());

        return new BaseException(message, ex);
    }
}