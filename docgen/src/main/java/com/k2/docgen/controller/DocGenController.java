/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.docgen.controller;

import com.k2.core.model.CustomPair;
import com.k2.core.model.TextRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/docgen")
public class DocGenController
{
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
}
