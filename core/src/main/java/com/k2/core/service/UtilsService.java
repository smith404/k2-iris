/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.service;

import com.k2.core.exception.BaseException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public interface UtilsService
{
    String DEFAULT_LOCALE = "en_ch";
    String NEWLINE = "\n";
    String TAB = "\t";
    String SPACE = " ";

    static double jaccard(List<String> doc1, List<String> doc2)
    {
        Set<String> intersection = new HashSet<>();

        for(String word: doc1)
        {
            if (doc2.contains((word)))
            {
                intersection.add(word);
            }
        }

        return intersection.size() / (doc1.size() + doc2.size());
    }

    static double tf(List<String> doc, String term)
    {
        double result = 0;
        for (String word : doc)
        {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    static double idf(List<List<String>> docs, String term)
    {
        double n = 0;
        for (List<String> doc : docs)
        {
            for (String word : doc)
            {
                if (term.equalsIgnoreCase(word))
                {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }

    static double tfIdf(List<String> doc, List<List<String>> docs, String term)
    {
        return tf(doc, term) * idf(docs, term);
    }

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