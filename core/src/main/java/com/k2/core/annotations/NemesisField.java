/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.annotations;

import com.k2.core.enums.FieldType;

public class NemesisField
{
    private Long id;

    private FieldType fieldType = FieldType.String;

    private String fieldName = "";

    private String importColumn = "";

    private String defaultValue = "\"\"";

    private boolean readable = true;

    private boolean writable = true;

    private boolean inline = false;

    private boolean displayValue = false;

    private boolean identity = false;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public NemesisField(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType)
    {
        this.fieldType = fieldType;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public boolean isReadable()
    {
        return readable;
    }

    public void setReadable(boolean readable)
    {
        this.readable = readable;
    }

    public boolean isWritable()
    {
        return writable;
    }

    public void setWritable(boolean writable)
    {
        this.writable = writable;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public String getImportColumn()
    {
        return importColumn;
    }

    public void setImportColumn(String importColumn)
    {
        this.importColumn = importColumn;
    }

    public boolean isInline()
    {
        return inline;
    }

    public void setInline(boolean inline)
    {
        this.inline = inline;
    }

    public boolean isDisplayValue()
    {
        return displayValue;
    }

    public void setDisplayValue(boolean displayValue)
    {
        this.displayValue = displayValue;
    }

    public boolean isIdentity()
    {
        return identity;
    }

    public void setIdentity(boolean identity)
    {
        this.identity = identity;
    }

    @Override
    public String toString()
    {
        return "NemesisField{" +
                "fieldType=" + fieldType +
                ", fieldName='" + fieldName + '\'' +
                ", importColumn='" + importColumn + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", readable=" + readable +
                ", writable=" + writable +
                ", inline=" + inline +
                ", displayValue=" + displayValue +
                ", identity=" + identity +
                '}';
    }
}