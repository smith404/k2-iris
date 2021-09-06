/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class WordDocument extends POIDocment
{
    private XWPFDocument theDocument;

    public WordDocument(byte[] theBytes) throws IOException
    {
        if (theBytes == null)
            init(new XWPFDocument());
        else
            init(new XWPFDocument(new ByteArrayInputStream(theBytes)));
    }

    private void init(XWPFDocument theDocument)
    {
        this.theDocument = theDocument;
        setBaseDoc(theDocument);
    }

    public void mangle()
    {
        for (XWPFParagraph paragraph : theDocument.getParagraphs())
        {
            XmlCursor cursor = paragraph.getCTP().newCursor();
            cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");

            List<XmlObject> ctrsintxtbx = new ArrayList<XmlObject>();

            while(cursor.hasNextSelection())
            {
                cursor.toNextSelection();
                XmlObject obj = cursor.getObject();
                ctrsintxtbx.add(obj);
            }
            for (XmlObject obj : ctrsintxtbx)
            {
                try
                {
                    CTR ctr = CTR.Factory.parse(obj.xmlText());
                    XWPFRun bufferrun = new XWPFRun(ctr, (IRunBody) paragraph);
                    String text = bufferrun.getText(0);
                    String newText = searchAndReplace(text);

                    if (!text.equalsIgnoreCase(newText))
                    {
                        bufferrun.setText(newText, 0);
                    }

                    obj.set(bufferrun.getCTR());
                }
                catch (XmlException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }



    @Override
    public void enforceUpdateFields()
    {
        theDocument.enforceUpdateFields();
    }

    @Override
    public void addDocument(RawDocument document) throws Exception
    {
        XWPFDocument docToAdd = null;

        if (document.isPlainText())
        {
            docToAdd = createDocument(new String(document.getBytes()));
        }

        if (document.isPOI())
        {
            POIDocment poiDocment = (POIDocment) document;

            if (poiDocment.getBaseDoc() instanceof XWPFDocument)
            {
                docToAdd = (XWPFDocument) poiDocment.getBaseDoc();
            }
            else
                log.info("IRIS ** Tried to add an incompatible document to a Word document: Ignored");
        }

        if (docToAdd != null)
        {
            CTBody addBody = docToAdd.getDocument().getBody();
            theDocument.getDocument().addNewBody().set(addBody);
        }
    }

    public void addHeader(String text) throws Exception
    {
        XWPFHeader head = theDocument.createHeader(HeaderFooterType.DEFAULT);
        head.createParagraph().createRun().setText(text);
    }

    public void addFooter(String text) throws Exception
    {
        XWPFFooter foot = theDocument.createFooter(HeaderFooterType.DEFAULT);
        foot.createParagraph().createRun().setText(text);
    }

    public void boarderParagraph(XWPFParagraph paragraph)
    {
        boarderParagraph(paragraph, defauiltBoarders);
    }

    public void boarderParagraph(XWPFParagraph paragraph, Borders[] boarders)
    {
        paragraph.setBorderTop(boarders[0]);
        paragraph.setBorderBottom(boarders[1]);
        paragraph.setBorderLeft(boarders[2]);
        paragraph.setBorderRight(boarders[3]);
    }

    public void addTable(List<Map<String, Object>> records)
    {
        // Create a simple table using the document.
        XWPFTable table = theDocument.createTable();

        records.forEach(record -> {
            XWPFTableRow tableRow = table.getRow(0);
            record.keySet().forEach(field -> {
                XWPFTableCell tableCell = tableRow.addNewTableCell();
                tableCell.setText(record.get(field).toString());
            });
        });
    }

    private XWPFDocument createDocument(String paragraph)
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

    @Override
    public String getExtension()
    {
        return "docx";
    }
}
