/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.ocr.utils;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class TiKaUtils
{
    private static final String[] validExtensions = {"jpg","bmp","gif","png"};

    public static boolean IsImageExtension(String fileName)
    {
        for (String validExtension : validExtensions)
            if (fileName.endsWith(validExtension)) return true;

        return false;
    }

    public static Metadata extractMetaDataUsingParser(InputStream stream) throws IOException, SAXException, TikaException
    {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        parser.parse(stream, handler, metadata, context);
        return metadata;
    }

    public static String extractContentUsingParser(InputStream stream) throws IOException, TikaException, SAXException
    {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        parseContext.set(Parser.class, parser);

        parser.parse(stream, handler, metadata, parseContext);
        return handler.toString();
    }

    public static String detectDocTypeUsingDetector(InputStream stream) throws IOException
    {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        MediaType mediaType = detector.detect(stream, metadata);
        return mediaType.toString();
    }

    public static String extractPdfContentUsingOCR(InputStream stream) throws IOException, TikaException, SAXException
    {
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);

        TesseractOCRConfig config = new TesseractOCRConfig();
        PDFParserConfig pdfConfig = new PDFParserConfig();
        pdfConfig.setExtractInlineImages(true);

        ParseContext parseContext = new ParseContext();
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        parseContext.set(Parser.class, parser);

        Metadata metadata = new Metadata();
        parser.parse(stream, handler, metadata, parseContext);

        return handler.toString();
    }

    public static String extractJpgContentUsingOCR(InputStream stream) throws IOException, TikaException, SAXException
    {
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        ParseContext parseContext = new ParseContext();
        TesseractOCRConfig config = new TesseractOCRConfig();
        JpegParser parser = new JpegParser();
        Metadata metadata = new Metadata();

        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(Parser.class, parser);
        parser.parse(stream, handler, metadata, parseContext);

        return handler.toString();
    }
}