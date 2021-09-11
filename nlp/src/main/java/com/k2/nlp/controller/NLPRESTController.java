/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.controller;

import com.k2.core.model.CustomPair;
import com.k2.core.model.TextRecord;
import com.k2.nlp.service.impl.NLPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nlp")
public class NLPRESTController
{
    @Autowired
    NLPServiceImpl nlpService;

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public ResponseEntity<?> makeNew(Model model)
    {
        TextRecord tr = new TextRecord();

        tr.setText("The text");

        tr.getRecord().add(new CustomPair("name", "Mark"));
        tr.getRecord().add(new CustomPair("age", 51));
        tr.getRecord().add(new CustomPair("sex", "Yes Please"));

        return new ResponseEntity<>(tr, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/lang")
    public ResponseEntity<?> detectLanguage(@RequestBody String text, Model model)
    {
        return new ResponseEntity<>(nlpService.detectLanguage(text), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sentences")
    public ResponseEntity<?> detectSentences(@RequestBody String text, Model model)
    {
        return new ResponseEntity<>(nlpService.sentencesPos(text), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tokens")
    public ResponseEntity<?> detectTokens(@RequestBody String text, Model model)
    {
        return new ResponseEntity<>(nlpService.tokenizeValues(text), HttpStatus.OK);
    }

}
