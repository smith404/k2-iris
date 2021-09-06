/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DocumentFormat
{
    @JsonProperty("Word")
    Word,
    @JsonProperty("Excel")
    Excel,
    @JsonProperty("PowerPoint")
    PowerPoint,
    @JsonProperty("Pdf")
    Pdf,
    @JsonProperty("Rtf")
    Rtf,
    @JsonProperty("Text")
    Text,
    @JsonProperty("Xml")
    Xml,
    @JsonProperty("Html")
    Html,
    @JsonProperty("Groovy")
    Groovy,
    @JsonProperty("Other")
    Other;

    public static String toExtension(final DocumentFormat format)
    {
        switch (format)
        {
            case Word: return "docx";
            case Excel: return "xlsx";
            case PowerPoint: return "pptx";
            case Pdf: return "pdf";
            case Rtf: return "rtf";
            case Xml: return "xml";
            case Html: return "html";
            case Groovy: return "groovy";
            default: return "txt";
        }
    }

    public static DocumentFormat fromExtension(final String extension)
    {
        switch (extension)
        {
            case "docx":
            case "docm":
            case "dot":
            case "dotm":
            case "dotx":
            case "doc": return Word;
            case "csv":
            case "xlsb":
            case "xlsx":
            case "xlsm":
            case "xls": return Excel;
            case "pps":
            case "pptb":
            case "pptx":
            case "pptm":
            case "odp":
            case "ppt": return PowerPoint;
            case "pdf": return Pdf;
            case "rtf": return Rtf;
            case "xml": return Xml;
            case "html": return Html;
            case "groovy": return Groovy;
            default: return Text;
        }
    }
}
