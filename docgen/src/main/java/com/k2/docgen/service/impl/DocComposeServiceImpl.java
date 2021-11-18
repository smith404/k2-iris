/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.service.impl;

import com.k2.docgen.enums.DocumentFormat;
import com.k2.docgen.model.*;
import com.k2.docgen.service.DocComposeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("DocComposeService")
public class DocComposeServiceImpl implements DocComposeService
{
    @PostConstruct
    public void init()
    {
    }

    public RawDocument makeDocument(DocUnit unit) throws IOException
    {
        if (unit.getFormat() == DocumentFormat.Word)
        {
            return new WordDocument(unit.getFileData());
        }
        else if (unit.getFormat() == DocumentFormat.Excel)
        {
            return new ExcelSpreadSheet(unit.getFileData());
        }
        else if (unit.getFormat() == DocumentFormat.PowerPoint)
        {
            return new PowerPointPresentation(unit.getFileData());
        }
        else if (unit.getFormat() == DocumentFormat.Rtf)
        {
            return new RTFDocument(unit.getFileData());
        }
        else return new TxtDocument(unit.getFileData());
    }

    public RawDocument processDocTemplate(DocTemplate theTemplate) throws Exception
    {
        // Create the new document with part "1" as the basis
        RawDocument targetDocument = makeDocument(theTemplate.getParts().get(0).getDocPart().getDocUnit());

        Map<String, Object> bindMap = new LinkedHashMap<>();

        targetDocument.setBindMap(bindMap);
        addStandardProperties(targetDocument);
        addMapAsProperties(targetDocument, bindMap);
        targetDocument.mangle();

        for(int i = 1; i < theTemplate.getParts().size(); ++i)
        {
            TemplatePart templatePart = theTemplate.getParts().get(i);

            boolean shouldInclude = true;

            /*
            if (templatePart.getIncludeFilter() != null)
            {
                // TODO: Execute filter
            }

            if (templatePart.getExcludeFilter() != null)
            {
                // TODO: Execute filter
            }
            */

            if (!shouldInclude) continue;

            // Make the document from the current document part
            DocPart docPart = templatePart.getDocPart();
            DocUnit docUnit = docPart.getDocUnit();

            RawDocument documentPart = makeDocument(docUnit);
            documentPart.setBindMap(bindMap);
            documentPart.mangle();

            // Process the document part with bind-map
            targetDocument.addDocument(documentPart);
        }

        return targetDocument;
    }

    public void addStandardProperties(RawDocument theDocument)
    {
    }

    public void addMapAsProperties(RawDocument theDocument, Map<String, Object> bindMap)
    {
        for (Map.Entry<String, Object> entry : bindMap.entrySet())
        {
            theDocument.setCustomProperty(entry.getKey(), entry.getValue());
        }
    }

    public XWPFDocument stringToWordDocument(String paragraph)
    {
        XWPFDocument newDoc = new XWPFDocument();

        String lines[] = paragraph.split("\\r?\\n");

        for(String line : lines)
        {
            XWPFParagraph newParagraph = newDoc.createParagraph();
            XWPFRun newRun = newParagraph.createRun();
            newRun.setText(line);
        }

        return newDoc;
    }
}
