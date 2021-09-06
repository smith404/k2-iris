/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

abstract public class RawDocument
{
    public static String BeginMarker = "%";
    public static String EndMarker = "%";

    protected Map<String, Object> bindMap = new LinkedHashMap<>();

    public RawDocument()
    {
    }

    abstract public void mangle();

    public String searchAndReplace(String inText)
    {
        if (inText == null || inText.trim().length() == 0) return "";

        String result = inText;
        for (String key : bindMap.keySet())
        {
            try
            {
                String value = bindMap.get(key).toString();

                result = result.replace(BeginMarker + key + EndMarker, value);
                result = result.replace(BeginMarker + key.toUpperCase() + EndMarker, value);
                result = result.replace(BeginMarker + key.toLowerCase() + EndMarker, value);
            }
            catch(java.lang.ClassCastException ex)
            {
                // What ever it was it couldn't be cast to a string - so let's ignore it
            }
        }

        return result;
    }

    public Map<String, Object> getBindMap()
    {
        return bindMap;
    }

    public void setBindMap(Map<String, Object> bindMap)
    {
        this.bindMap = bindMap;
    }

    abstract public boolean isPlainText();

    abstract public boolean isPOI();

    abstract public void enforceUpdateFields();

    abstract public void addDocument(RawDocument document) throws Exception;

    public void setTitle(String title)
    {
        bindMap.put("Title", title);
    }

    public void setAuthor(String author)
    {
        bindMap.put("Author", author);
    }

    public void setCustomProperty(String name, Object value)
    {
        setCustomProperty(name, value.toString());
    }

    public void setCustomProperty(String name, String value)
    {
        bindMap.put(name, value);
    }

    public void setCustomProperty(String name, LocalDate value)
    {
        bindMap.put(name, value);
    }

    public void setCustomProperty(String name, Integer value)
    {
        bindMap.put(name, value);
    }

    public void setCustomProperty(String name, Double value)
    {
        bindMap.put(name, value);
    }

    public void setCustomProperty(String name, Boolean value)
    {
        bindMap.put(name, value);
    }

    abstract public byte[] getBytes() throws IOException;

    abstract public String getExtension();
}
