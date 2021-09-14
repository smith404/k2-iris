/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.persist.component;

import org.springframework.beans.factory.annotation.Value;

public class BaseComponent
{
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    // Inject some global properties via application.yaml
    @Value("${application.baseName:nemesis}")
    protected String baseName;

    @Value("${application.baseVersion:1.0.0}")
    protected String baseVersion;

    @Value("${application.environment:PROD}")
    protected String environment;

    @Value("${application.default.lang:eng}")
    protected String defaultLang;

    @Value("${application.ttName:ClockUp}")
    protected String ttName;

    @Value("${application.botService:false}")
    protected String withBot;

    @Value("${spring.datasource.hikari.schema:PUBLIC}")
    protected String SCHEMA;

    @Value("${application.lucene:false}")
    protected String withLucene;

    @Value("${application.dtree:false}")
    protected String withDtree;

    @Value("${application.nlp:false}")
    protected String withNLP;

    @Value("${application.sp:false}")
    protected String withSP;

    @Value("${application.fileData:false}")
    protected String withFileData;
}