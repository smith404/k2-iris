/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelSpreadSheet extends POIDocment
{
    private XSSFWorkbook theWorkbook;

    public ExcelSpreadSheet(byte[] theBytes) throws IOException
    {
        if (theBytes == null)
            init(new XSSFWorkbook());
        else
            init(new XSSFWorkbook(new ByteArrayInputStream(theBytes)));
    }

    private void init(XSSFWorkbook theWorkbook)
    {
        this.theWorkbook = theWorkbook;
        setBaseDoc(theWorkbook);
    }

    public void mangle()
    {
    }

    public static Object convertCell(Cell cell)
    {
        switch (cell.getCellType())
        {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell))
                {
                    return cell.getLocalDateTimeCellValue();
                }
                else
                {
                    double theValue = cell.getNumericCellValue();

                    // Check if it's an integer
                    if (Math.floor(theValue) == theValue)
                    {
                        return (int) theValue;
                    }

                    return cell.getNumericCellValue();
                }
            case FORMULA:
                if (cell.getCachedFormulaResultType() == CellType.NUMERIC)
                {
                    if (DateUtil.isCellDateFormatted(cell))
                    {
                        return cell.getLocalDateTimeCellValue();
                    }
                    else
                    {
                        return cell.getNumericCellValue();
                    }
                }
                else
                {
                    return cell.getRichStringCellValue().getString().trim();
                }
            case BLANK:
            default:
                return "";
        }
    }

    public XSSFSheet getSheet(String sheetName)
    {
        return theWorkbook.getSheet(sheetName);
    }

    public XSSFSheet getSheet(int sheetNumber)
    {
        return theWorkbook.getSheetAt(sheetNumber);
    }

    public List<String> getSheets()
    {
        List<String> theSheets = new ArrayList<>();

        theWorkbook.forEach(sheet -> theSheets.add(sheet.getSheetName()));

        return theSheets;
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

            if (poiDocment.getBaseDoc() instanceof XSSFWorkbook)
            {
                XSSFWorkbook excelDocument = (XSSFWorkbook) poiDocment.getBaseDoc();
            }
            else
                log.info("IRIS ** Tried to add an incompatible document to a Excel document: Ignored");
        }
        else
        {
            // TODO: Add text document
        }
    }

    @Override
    public String getExtension()
    {
        return "xlsx";
    }
}
