/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.templateservice.controller;

import com.k2.core.model.Chooser;
import com.k2.core.model.CustomPair;
import com.k2.core.model.TextRecord;
import com.k2.core.model.TextResponse;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;
import java.io.StringWriter;

@RestController
@RequestMapping("/nlp")
public class TemplateRESTController
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

    @RequestMapping(method = RequestMethod.POST, value = "/realize")
    public ResponseEntity<?> realize(@RequestBody TextRecord template)
    {
        TextResponse tr = new TextResponse();

        try
        {
            RuntimeServices rs = RuntimeSingleton.getRuntimeServices();
            StringReader sr = new StringReader(template.getText());
            SimpleNode sn = rs.parse(sr, "Transient Template");

            Template t = new Template();
            t.setRuntimeServices(rs);
            t.setData(sn);
            t.initDocument();

            VelocityContext vc = new VelocityContext();
            vc.put("math", new MathTool());
            vc.put("chooser", new Chooser());
            vc.put("symbol", "AMX");
            vc.put("past-month", 15);
            vc.put("this-month", 10);

            StringWriter sw = new StringWriter();
            t.merge(vc, sw);
            tr.setResult(sw.toString());
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
            tr.setResult(ex.getMessage());
            tr.setSucess(false);
        }

        return new ResponseEntity<>(tr, HttpStatus.OK);
    }
}
