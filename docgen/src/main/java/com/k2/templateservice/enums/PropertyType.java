/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PropertyType
{
    @JsonProperty("Boolean")
    Boolean,
    @JsonProperty("String")
    String,
    @JsonProperty("Date")
    Date,
    @JsonProperty("Integer")
    Integer,
    @JsonProperty("Number")
    Number;
}
