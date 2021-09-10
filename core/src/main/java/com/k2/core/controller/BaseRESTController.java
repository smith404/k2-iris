/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.controller;

import com.k2.core.service.impl.EntityServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/entity")
public class BaseRESTController
{
    @Autowired
    private EntityServiceImpl entityService;

    @RequestMapping(method = RequestMethod.GET, value = "/init")
    public ResponseEntity<?> readEntity(@RequestParam(value = "entity") String entity,
                                        @RequestParam(value = "id") Long id, Model model)
    {
        Object object = entityService.findInstanceWithNameValuePair(entity, "id", id);

        return new ResponseEntity<>(object, HttpStatus.OK);
    }

}
