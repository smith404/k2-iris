/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

public class TemplatePart
{
    private DocTemplate docTemplate;

    private int priority;

    private boolean followMasterGraphics = true;

    private DocPart docPart;

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public boolean isFollowMasterGraphics()
    {
        return followMasterGraphics;
    }

    public void setFollowMasterGraphics(boolean followMasterGraphics)
    {
        this.followMasterGraphics = followMasterGraphics;
    }

    public DocPart getDocPart()
    {
        return docPart;
    }

    public void setDocPart(DocPart docPart)
    {
        this.docPart = docPart;
    }

    public DocTemplate getDocTemplate()
    {
        return docTemplate;
    }

    public void setDocTemplate(DocTemplate docTemplate)
    {
        this.docTemplate = docTemplate;
    }

    @Override
    public String toString()
    {
        return "TemplatePart{" +
                ", priority=" + priority +
                ", followMasterGraphics=" + followMasterGraphics +
                ", docPart=" + docPart +
                "} " + super.toString();
    }
}