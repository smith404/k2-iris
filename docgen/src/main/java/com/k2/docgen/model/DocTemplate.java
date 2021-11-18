/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

import java.util.ArrayList;
import java.util.List;

public class DocTemplate
{
    private String targetFileName = "target";

    private List<TemplatePart> parts = new ArrayList<>();

    public List<TemplatePart> getParts()
    {
        return parts;
    }

    public void setParts(List<TemplatePart> parts)
    {
        this.parts = parts;
    }

    public String getTargetFileName()
    {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName)
    {
        this.targetFileName = targetFileName;
    }

    @Override
    public String toString()
    {
        return "DocTemplate{" +
                "targetFileName='" + targetFileName + '\'' +
                ", parts=" + parts +
                "} " + super.toString();
    }
}

