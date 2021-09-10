/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

@Slf4j
public class BaseController
{
    // Inject some global properties via application.properties
    @Value("${application.baseName:nemesis}")
    protected String baseName;

    @Value("${application.baseVersion:1.0.0}")
    protected String baseVersion;

    @Value("${notification.refresh:60000}")
    protected String refreshTime;

    @Value("${application.bannerMessage:Welcome to K2-Nemesis}")
    protected String bannerMessage;

    @Value("${application.environment:PROD}")
    protected String environment;

    protected void fillModel(Model model)
    {
        model.addAttribute("refreshtime", refreshTime);
    }

    public String getBaseName()
    {
        return baseName;
    }

    public void setBaseName(String baseName)
    {
        this.baseName = baseName;
    }

    public String getBaseVersion()
    {
        return baseVersion;
    }

    public void setBaseVersion(String baseVersion)
    {
        this.baseVersion = baseVersion;
    }

    public String getRefreshTime()
    {
        return refreshTime;
    }

    public void setRefreshTime(String refreshTime)
    {
        this.refreshTime = refreshTime;
    }

    public String getBannerMessage()
    {
        return bannerMessage;
    }

    public void setBannerMessage(String bannerMessage)
    {
        this.bannerMessage = bannerMessage;
    }

    public String getEnvironment()
    {
        return environment;
    }

    public void setEnvironment(String environment)
    {
        this.environment = environment;
    }
}