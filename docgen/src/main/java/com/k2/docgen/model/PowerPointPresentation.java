/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
public class PowerPointPresentation extends POIDocment
{
    private XMLSlideShow theSlideShow;

    public PowerPointPresentation(byte[] theBytes) throws IOException
    {
        if (theBytes == null)
            init(new XMLSlideShow());
        else
            init(new XMLSlideShow(new ByteArrayInputStream(theBytes)));
    }

    private void init(XMLSlideShow theSlideShow)
    {
        this.theSlideShow = theSlideShow;
        setBaseDoc(theSlideShow);
    }

    public void mangle()
    {
        for (XSLFSlide slide : theSlideShow.getSlides())
        {
            CTSlide ctSlide = slide.getXmlObject();
            XmlObject[] allText = ctSlide.selectPath(
                    "declare namespace a='http://schemas.openxmlformats.org/drawingml/2006/main' " +
                            ".//a:t"
            );

            for (int i = 0; i < allText.length; i++)
            {
                if (allText[i] instanceof XmlString)
                {
                    XmlString xmlString = (XmlString) allText[i];
                    String text = xmlString.getStringValue();
                    String newText = searchAndReplace(text);

                    if (!text.equalsIgnoreCase(newText))
                    {
                        xmlString.setStringValue(newText);
                    }
                }
            }
        }
    }

    @Override
    public void enforceUpdateFields()
    {

    }

    @Override
    public void addDocument(RawDocument document) throws Exception
    {
        if (document.isPOI())
        {
            POIDocment poiDocment = (POIDocment) document;

            if (poiDocment.getBaseDoc() instanceof XMLSlideShow)
            {
                XMLSlideShow pptDocument = (XMLSlideShow) poiDocment.getBaseDoc();

                // Try and make sure the source document is the same page format as the target
                pptDocument.setPageSize(theSlideShow.getPageSize());

                for (XSLFSlide slide : pptDocument.getSlides())
                {
                    XSLFSlide newSlide =  theSlideShow.createSlide();

                    newSlide.setFollowMasterGraphics(true);
                    newSlide.setFollowMasterObjects(true);

                    newSlide.importContent(slide);
                }
            }
            else
                log.info("IRIS ** Tried to add an incompatible document to a PowerPoint document: Ignored");
        }
        else
        {
            // TODO: Add text document
        }


    }

    @Override
    public String getExtension()
    {
        return "pptx";
    }
}
