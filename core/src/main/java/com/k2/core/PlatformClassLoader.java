/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core;

import com.k2.core.model.CommandResult;
import com.k2.core.service.UtilsService;
import com.k2.core.service.impl.GroovyServiceImpl;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyScriptEngine;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.File;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;

@Slf4j
public class PlatformClassLoader extends GroovyClassLoader
{
    // Singleton instnance
    private static PlatformClassLoader instance = null;

    // Execution engine
    private static GroovyScriptEngine scriptEngine = null;

    private final CompilerConfiguration config;
    private final ClassLoader parent;

    // Cache for our classes
    LinkedHashMap<String, Class<?>> clazzes = new LinkedHashMap<>();

    // Private constructor
    private PlatformClassLoader(ClassLoader parent, CompilerConfiguration config)
    {
        super(parent, config);
        this.parent = parent;
        this.config = config;
    }

    // Access method
    public static PlatformClassLoader getInstance()
    {
        if (instance != null) return instance;

        //ClassLoader cLoader = Thread.currentThread().getContextClassLoader();
        URLClassLoader cLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

        instance = new PlatformClassLoader(cLoader, new CompilerConfiguration());

        return instance;
    }

    public static String getPathToClass(Class<?> clazz)
    {
        if (clazz == null)
        {
            return "";
        }

        return clazz.getName().replace(".", File.separator);
    }

    public static String getPathToPackage(String packageName)
    {
        if (packageName == null)
        {
            return "";
        }

        return packageName.replace(".", File.separator);
    }

    public CommandResult addClass(String packageName, String className, String src)
    {
        String fullClassName = packageName + "." + className;
        String filePath = GroovyServiceImpl.GROOVY_TEMP_CLASSES + File.separator;
        if (packageName.length() > 0) filePath += getPathToPackage(packageName) + File.separator;
        filePath += className + ".class";

        log.debug("Path to class: " + filePath);

        CommandResult retVal = new CommandResult();
        try
        {
            Class<?> clazz = this.parseClass(src, filePath);
            if (clazz.getName().equals(fullClassName))
            {
                retVal.setResultOutput("Class: " + fullClassName + " has been added");
                clazzes.put(fullClassName, clazz);
            }
            else
            {
                retVal.setResultOutput("Class: " + clazz.getName() + " does not match expected class of: " + fullClassName);
            }
        }
        catch (CompilationFailedException ex)
        {
            retVal.appendResultOutput("Class " + fullClassName + " failed compilation with exception: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            retVal.appendResultOutput("Class " + fullClassName + " failed to load with exception: " + ex.getMessage());
        }

        return retVal;
    }

    // Return the class instance
    public Class<?> getClass(String packageName, String className)
    {
        String fullClassName = packageName + "." + className;
        try
        {
            if (clazzes.containsKey(fullClassName))
            {
                return clazzes.get(fullClassName);
            }

            return null;
        }
        catch (Exception ex)
        {
            log.warn(UtilsService.makeExceptionWarning(ex));
        }

        return null;
    }
}
