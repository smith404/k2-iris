/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.ocr.model;

public class DocMetaData
{
    private String level1Key = "";

    private String level2Key = "";

    private String level3Key = "";

    private String level4Key = "";

    private String value = "";

    public DocMetaData()
    {
    }

    public DocMetaData(String property, String val)
    {
        this(property, val, ":");
    }

    public DocMetaData(String property, String val, String delimiter)
    {
        value = val;
        String[] props = property.trim().split("\\s*" + delimiter + "\\s*");

        for(int i=0; i< props.length; ++i)
        {
            if (i==0) level1Key = props[i];
            if (i==1) level2Key = props[i];
            if (i==2) level3Key = props[i];
            if (i==3) level4Key = props[i];
        }
    }

    public String getLevel1Key()
    {
        return level1Key;
    }

    public void setLevel1Key(String level1Key)
    {
        this.level1Key = level1Key;
    }

    public String getLevel2Key()
    {
        return level2Key;
    }

    public void setLevel2Key(String level2Key)
    {
        this.level2Key = level2Key;
    }

    public String getLevel3Key()
    {
        return level3Key;
    }

    public void setLevel3Key(String level3Key)
    {
        this.level3Key = level3Key;
    }

    public String getLevel4Key()
    {
        return level4Key;
    }

    public void setLevel4Key(String level4Key)
    {
        this.level4Key = level4Key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "DocMetaData{" + super.toString() + ": " + level1Key +
                ((level2Key.length()>0) ? ":" + level2Key : "") +
                ((level3Key.length()>0) ? ":" + level3Key : "") +
                ((level4Key.length()>0) ? ":" + level4Key : "") +
                " = " + value + "}";
    }

    public String prettyPrint()
    {
        return level1Key +
                ((level2Key.length()>0) ? ":" + level2Key : "") +
                ((level3Key.length()>0) ? ":" + level3Key : "") +
                ((level4Key.length()>0) ? ":" + level4Key : "") +
                " = " + value;
    }
}

