/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.service.impl;

import com.k2.core.PlatformClassLoader;
import com.k2.core.component.BaseComponent;
import com.k2.core.exception.BaseException;
import com.k2.core.model.CommandResult;
import com.k2.core.service.GroovyService;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

@Slf4j
@Service("GroovyService")
public class GroovyServiceImpl extends BaseComponent implements GroovyService
{
    public static final String GROOVY_TEMP;
    public static final String GROOVY_TEMP_CLASSES;
    public static final String GROOVY_TEMP_SRC;

    static
    {
        GROOVY_TEMP = "./home/groovy";
        GROOVY_TEMP_CLASSES = GROOVY_TEMP + File.separator + "GroovyGen" + File.separator + "classes";
        GROOVY_TEMP_SRC = GROOVY_TEMP + File.separator + "GroovyGen" + File.separator + "src";
    }

    public GroovyServiceImpl()
    {
    }

    @PostConstruct
    public void init()
    {

    }

    public CommandResult executeScript(String script, Binding binding, boolean withThrow)
    {
        CommandResult retVal = new CommandResult();
        retVal.setResult(true);

        if (script != null && script.length() > 0)
        {
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                addServicesToBinding(binding);

                binding.setProperty("out", new PrintStream(baos));
                GroovyShell shell = new GroovyShell(PlatformClassLoader.getInstance(), binding);

                Object returnVal = shell.evaluate(script);

                retVal.setReturnValue(returnVal);
                retVal.setResultOutput(baos.toString());
            }
            catch (CompilationFailedException ex)
            {
                if (withThrow) throw new BaseException(ex.getMessage(), ex);
                retVal.setResult(false);
                retVal.appendResultOutput("Command failed compilation with exception: " + ex.getMessage());
            }
            catch (Exception ex)
            {
                if (withThrow) throw new BaseException(ex.getMessage(), ex);
                retVal.setResult(false);
                retVal.appendResultOutput("Command caused an exception: " + ex.getMessage());
            }
        }

        return retVal;
    }

    private void addServicesToBinding(Binding binding)
    {
        binding.setProperty("groovyService", this);
    }
}
