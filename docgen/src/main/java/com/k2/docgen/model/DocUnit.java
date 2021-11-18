/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

import com.k2.docgen.enums.DocumentFormat;

public class DocUnit
{
    private byte[] fileData;

    private String fileName = "";

    private DocumentFormat format = DocumentFormat.Text;

    public byte[] getFileData()
    {
        return fileData;
    }

    public void setFileData(byte[] fileData)
    {
        this.fileData = fileData;
    }

    public DocumentFormat getFormat()
    {
        return format;
    }

    public void setFormat(DocumentFormat format)
    {
        this.format = format;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return "DocUnit{" +
                ", fileName='" + fileName + '\'' +
                ", format=" + format +
                "} " + super.toString();
    }
}
