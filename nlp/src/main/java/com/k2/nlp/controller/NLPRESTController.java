/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.controller;

import com.k2.core.model.CustomPair;
import com.k2.core.model.TextRecord;
import com.k2.nlp.model.NamedEntity;
import com.k2.nlp.service.impl.NLPServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nlp")
public class NLPRESTController
{
    @Autowired
    NLPServiceImpl nlpService;

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
    public ResponseEntity<?> detectTokenValues(@RequestBody String text, Model model)
    {
        return new ResponseEntity<>(nlpService.tokenizeValues(text), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tokenize")
    public ResponseEntity<?> detectTokens(@RequestBody String text, Model model)
    {
        return new ResponseEntity<>(nlpService.tokenize(text), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/tags")
    public ResponseEntity<?> detectTags(@RequestBody String text, Model model)
    {
        String[] tokens = nlpService.tokenize(text);

        return new ResponseEntity<>(nlpService.tags(tokens), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/lemmas")
    public ResponseEntity<?> detectLemmas(@RequestBody String text, Model model)
    {
        String[] tokens = nlpService.tokenize(text);
        String[] tags = nlpService.tags(tokens);

        return new ResponseEntity<>(nlpService.lemmas(tokens, tags), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/entities")
    public ResponseEntity<?> detectEntities(@RequestBody String text, Model model)
    {
        List<NamedEntity> found = nlpService.entityDetect(text, 0.8);

        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/category")
    public ResponseEntity<?> classifyContents(@RequestBody String text, @RequestParam(name = "type") String type, Model model)
    {
        return new ResponseEntity<>(nlpService.classifyContent(text, type), HttpStatus.OK);
    }
}
