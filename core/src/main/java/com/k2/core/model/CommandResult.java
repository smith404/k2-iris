/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.model;

import com.k2.core.service.UtilsService;

public class CommandResult
{
    private boolean result;

    private String resultOutput;

    private Object returnValue;

    public CommandResult()
    {
        this.resultOutput = "";
        this.returnValue = null;
        this.result = false;
    }

    public CommandResult(String resultOutput)
    {
        this.resultOutput = resultOutput;
    }

    public String getResultOutput()
    {
        return resultOutput;
    }

    public void setResultOutput(String resultOutput)
    {
        this.resultOutput = resultOutput;
    }

    public Object getReturnValue()
    {
        return returnValue;
    }

    public void setReturnValue(Object returnValue)
    {
        this.returnValue = returnValue;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public void appendResultOutput(String resultOutput)
    {
        if (this.resultOutput.length() > 0)
        {
            this.resultOutput += UtilsService.NEWLINE;
            this.resultOutput += resultOutput;
        }
        else
            this.setResultOutput(resultOutput);
    }

    @Override
    public String toString()
    {
        return "CommandResult{" +
                "result=" + result +
                ", resultOutput='" + resultOutput + '\'' +
                ", returnValue=" + returnValue +
                '}';
    }
}
