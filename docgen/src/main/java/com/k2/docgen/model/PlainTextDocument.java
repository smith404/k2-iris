/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;

@Slf4j
public abstract class PlainTextDocument extends RawDocument
{
    protected String thePlainText;

    public PlainTextDocument(byte[] theBytes)
    {
        if (theBytes == null)
            thePlainText = "";
        else
            thePlainText = new String(theBytes);
    }

    public void mangle()
    {
        thePlainText = searchAndReplace(thePlainText);
    }

    public XSLFSlide createSlide(String title, XMLSlideShow ppt)
    {
        return createSlide(title, ppt, ppt.getSlideMasters().get(0));
    }

    public XSLFSlide createSlide(String title, XMLSlideShow ppt, XSLFSlideMaster slideMaster)
    {
        XSLFSlideLayout titleLayout = slideMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
        XSLFSlide slide = ppt.createSlide(titleLayout);
        XSLFTextShape titleArea = slide.getPlaceholder(0);

        titleArea.setText(title);
        XSLFTextShape bodyArea = slide.getPlaceholder(1);
        bodyArea.setText(thePlainText);

        return slide;
    }

    public XWPFParagraph createParagraph(XWPFDocument doc)
    {
        return createParagraph(doc, false);
    }

    public XWPFParagraph createParagraph(XWPFDocument doc, boolean pageBreak)
    {
        XWPFParagraph para = doc.createParagraph();
        para.setPageBreak(pageBreak);
        XWPFRun run = para.createRun();

        run.setText(thePlainText);

        return para;
    }

    @Override
    public boolean isPlainText()
    {
        return true;
    }

    @Override
    public boolean isPOI()
    {
        return false;
    }

    @Override
    public void enforceUpdateFields()
    {
        // Nothing to do
    }

    @Override
    public void addDocument(RawDocument document) throws Exception
    {
        if (document.isPlainText())
        {
            thePlainText += ((PlainTextDocument) document).thePlainText;
        }
        else
        {
            log.info("IRIS ** Tried to add a POI document to a text document: Ignored");
        }
    }

    @Override
    public byte[] getBytes() throws IOException
    {
        return thePlainText.getBytes();
    }

    @Override
    public String getExtension()
    {
        return "txt";
    }
}

