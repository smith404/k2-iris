/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.exception;

public class BaseException extends RuntimeException
{
    private String userMessage;

    public BaseException(String message)
    {
        super(message);

        userMessage = message;
    }

    public BaseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public String getUserMessage()
    {
        return userMessage;
    }

    public void setUserMessage(String userMessage)
    {
        this.userMessage = userMessage;
    }
}
