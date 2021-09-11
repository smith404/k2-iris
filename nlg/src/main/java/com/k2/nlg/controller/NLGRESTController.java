/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlg.controller;

import com.k2.core.model.CustomPair;
import com.k2.core.model.TextRecord;
import com.k2.nlg.service.impl.NLGServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nlg")
public class NLGRESTController
{
    @Autowired
    NLGServiceImpl nlgService;

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
/*
    @RequestMapping(method = RequestMethod.POST, value = "/realize")
    public ResponseEntity<?> realize(@RequestBody String sentence,
                                     @RequestParam(value = "tense", required = false, defaultValue = "pr") String tense)
    {
        return new ResponseEntity<>(nlgService.realize(sentence, tense), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/nlg/process")
    public ResponseEntity<?> getParagraph(@RequestBody String sentence)
    {
        return new ResponseEntity<>(nlgService.processTextToParagraph(sentence), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/nlg/sentences")
    public ResponseEntity<?> getSentences(@RequestBody String text)
    {
        return new ResponseEntity<>(nlgService.getSentences(text), HttpStatus.OK);
    }
*/
}
