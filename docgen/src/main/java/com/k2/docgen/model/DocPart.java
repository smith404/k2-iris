/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

public class DocPart
{
    private DocUnit docUnit;

    public DocUnit getDocUnit()
    {
        return docUnit;
    }

    public void setDocUnit(DocUnit docUnit)
    {
        this.docUnit = docUnit;
    }

    @Override
    public String toString()
    {
        return "DocPart{" +
                "docUnit=" + docUnit +
                "} " + super.toString();
    }
}

