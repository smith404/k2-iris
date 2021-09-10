/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.model;

import java.util.ArrayList;
import java.util.List;

public class TextRecord
{
    private String text = "";
    private List<CustomPair> record = new ArrayList<>();

    public TextRecord()
    {
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<CustomPair> getRecord()
    {
        return record;
    }

    public void setRecord(List<CustomPair> record)
    {
        this.record = record;
    }
}
