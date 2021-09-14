/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.ocr.controller;

import com.k2.core.controller.BaseController;
import com.k2.core.exception.BaseException;
import com.k2.core.model.TextResponse;
import com.k2.core.service.UtilsService;
import com.k2.ocr.model.DocMetaData;
import com.k2.ocr.utils.TiKaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ocr")
public class OCRController extends BaseController
{
    @RequestMapping(method = RequestMethod.POST, value = "/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "type", required = false, defaultValue = "content") String type,
                                        @RequestParam(value = "raw", required = false, defaultValue = "no") String raw, Model model)
    {
        List<DocMetaData> dmdList = new ArrayList<>();
        TextResponse tr = new TextResponse();
        boolean asList = false;

        try
        {
            if (file != null)
            {
                String fileName = file.getOriginalFilename();
                InputStream is = new ByteArrayInputStream(file.getBytes());

                if (type.equalsIgnoreCase("metadata"))
                {
                    Metadata theData = TiKaUtils.extractMetaDataUsingParser(is);
                    StringBuilder sb = new StringBuilder();
                    for(String name : theData.names())
                    {
                        DocMetaData dmd = new DocMetaData(name, theData.get(name));
                        sb.append(dmd.prettyPrint()).append(UtilsService.NEWLINE);
                    }
                    tr.setResult(sb.toString());
                }
                else if (type.equalsIgnoreCase("metadatalist"))
                {
                    asList = true;
                    Metadata theData = TiKaUtils.extractMetaDataUsingParser(is);
                    for(String name : theData.names())
                    {
                        DocMetaData dmd = new DocMetaData(name, theData.get(name));
                        dmdList.add(dmd);
                    }
                }
                else
                {
                    String content;

                    if (TiKaUtils.IsImageExtension(fileName))
                    {
                        content = TiKaUtils.extractJpgContentUsingOCR(is);
                    }
                    else
                    {
                        content = TiKaUtils.extractContentUsingParser(is);
                    }
                    if (content.trim().length() == 0 && fileName.endsWith("pdf"))
                    {
                        is.reset();
                        content = TiKaUtils.extractPdfContentUsingOCR(is);
                    }
                    if (type.equalsIgnoreCase("concat"))
                    {
                        content = content.replaceAll("(\\r|\\n)", (UtilsService.SPACE));
                        content = content.replaceAll("\\s{2,}", (UtilsService.SPACE)).trim();
                        if (!raw.equalsIgnoreCase("yes")) content = content.replaceAll("/[^A-Za-z0-9 ]/", "");
                    }
                    tr.setResult(content);
                }

                is.close();

                if (asList)
                    return new ResponseEntity<>(dmdList, HttpStatus.OK);
                else
                    return new ResponseEntity<>(tr, HttpStatus.OK);
            }

            return new ResponseEntity<>(tr, HttpStatus.NO_CONTENT);
        }
        catch (Exception ex)
        {
            throw new BaseException(ex.getMessage(), ex);
        }
    }
}
