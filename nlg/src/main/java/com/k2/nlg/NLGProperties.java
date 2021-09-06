/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlg;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("nlg")
public class NLGProperties
{
    private boolean trainingSystem = false;

    // Data directories
    private String modelRootLocation = "nlg-data-root";

    private String defaultLanguage = "eng";

    public String getModelRootLocation()
    {
        return modelRootLocation;
    }

    public void setModelRootLocation(String modelRootLocation)
    {
        this.modelRootLocation = modelRootLocation;
    }

    public String getDefaultLanguage()
    {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage)
    {
        this.defaultLanguage = defaultLanguage;
    }
}
