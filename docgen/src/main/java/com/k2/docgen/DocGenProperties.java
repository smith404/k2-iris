/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("docgen")
public class DocGenProperties
{
    private String templateRootLocation = "nlp-data-root";
    private List<String> templates = new ArrayList<>();

    public String getTemplateRootLocation()
    {
        return templateRootLocation;
    }

    public void setTemplateRootLocation(String templateRootLocation)
    {
        this.templateRootLocation = templateRootLocation;
    }

    public List<String> getTemplates()
    {
        return templates;
    }

    public void setTemplates(List<String> templates)
    {
        this.templates = templates;
    }
}
