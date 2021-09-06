/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.model;

import com.k2.templateservice.enums.PropertyType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.Borders;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Slf4j
public abstract class POIDocment extends RawDocument
{
    public final static Borders[] leftRightBoarders = {Borders.NONE, Borders.NONE, Borders.BASIC_THIN_LINES, Borders.BASIC_THIN_LINES};
    public final static Borders[] topBottomBoarders = {Borders.BASIC_THIN_LINES, Borders.BASIC_THIN_LINES, Borders.NONE, Borders.NONE};
    public final static Borders[] defauiltBoarders = {Borders.BASIC_THIN_LINES, Borders.BASIC_THIN_LINES, Borders.BASIC_THIN_LINES, Borders.BASIC_THIN_LINES};

    protected POIXMLDocument baseDoc;

    public POIDocment()
    {
    }

    protected void setBaseDoc(POIXMLDocument baseDoc)
    {
        this.baseDoc = baseDoc;
    }

    public POIXMLDocument getBaseDoc()
    {
        return baseDoc;
    }

    public PropertyType getPropertyType(CTProperty property)
    {
        if (property.isSetLpwstr()
                || property.isSetLpstr())
        {
            return PropertyType.String;
        }
        else if (property.isSetDate()
                || property.isSetFiletime())
        {
            return PropertyType.Date;
        }
        else if (property.isSetBool())
        {
            return PropertyType.Boolean;
        }
        else if (property.isSetI1() ||
                property.isSetI2() ||
                property.isSetI4() ||
                property.isSetI8() ||
                property.isSetInt())
        {
            return PropertyType.Integer;
        }
        else if (property.isSetUi1() ||
                property.isSetUi2() ||
                property.isSetUi4() ||
                property.isSetUi8() ||
                property.isSetUint())
        {
            return PropertyType.Integer;
        }
        else if (property.isSetR4() ||
                property.isSetR8())
        {
            return PropertyType.Number;
        }
        else if (property.isSetDecimal())
        {
            return PropertyType.Number;
        }

        return PropertyType.String;
    }

    @Override
    public boolean isPlainText()
    {
        return false;
    }

    @Override
    public boolean isPOI()
    {
        return true;
    }

    @Override
    public void setTitle(String title)
    {
        super.setTitle(title);

        baseDoc.getProperties().getCoreProperties().setTitle(title);
    }

    @Override
    public void setAuthor(String author)
    {
        super.setAuthor(author);

        baseDoc.getProperties().getCoreProperties().setCreator(author);
    }

    @Override
    public void setCustomProperty(String name, Object value)
    {
        if (value == null) return;

        super.setCustomProperty(name, value);

        setCustomProperty(name, value.toString());
    }

    @Override
    public void setCustomProperty(String name, String value)
    {
        super.setCustomProperty(name, value);

        POIXMLProperties.CustomProperties customproperties = baseDoc.getProperties().getCustomProperties();

        if (!customproperties.contains(name))
        {
            customproperties.addProperty(name, value);
        }
        else
        {
            customproperties.getProperty(name).setLpwstr(value);
        }
    }

    @Override
    public void setCustomProperty(String name, LocalDate value)
    {
        super.setCustomProperty(name, value);

        setCustomProperty(name, value.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
    }

    @Override
    public void setCustomProperty(String name, Integer value)
    {
        super.setCustomProperty(name, value);

        POIXMLProperties.CustomProperties customproperties = baseDoc.getProperties().getCustomProperties();

        if (!customproperties.contains(name))
        {
            customproperties.addProperty(name, value);
        }
        else
        {
            customproperties.getProperty(name).setInt(value);
        }
    }

    @Override
    public void setCustomProperty(String name, Double value)
    {
        super.setCustomProperty(name, value);

        POIXMLProperties.CustomProperties customproperties = baseDoc.getProperties().getCustomProperties();

        if (!customproperties.contains(name))
        {
            customproperties.addProperty(name, value);
        }
        else
        {
            customproperties.getProperty(name).setR8(value);
        }
    }

    @Override
    public void setCustomProperty(String name, Boolean value)
    {
        super.setCustomProperty(name, value);

        POIXMLProperties.CustomProperties customproperties = baseDoc.getProperties().getCustomProperties();

        if (!customproperties.contains(name))
        {
            customproperties.addProperty(name, value);
        }
        else
        {
            customproperties.getProperty(name).setBool(value);
        }
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baseDoc.write(baos);

        return baos.toByteArray();
    }
}
