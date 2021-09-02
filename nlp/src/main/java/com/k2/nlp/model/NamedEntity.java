/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.model;

import opennlp.tools.util.Span;

public class NamedEntity
{
    private String name = "";

    private String type = "" ;

    private String value = "";

    private double probability = 0D;

    private int start = -1;

    private int length = 0;

    public NamedEntity()
    {
    }

    public NamedEntity(Span span, String value)
    {
        this.name = span.getType();
        this.type = span.getType();
        this.start = span.getStart();
        this.length = span.length();
        this.probability = span.getProb();

        this.value = value;
    }

    public NamedEntity(String name, String type, String value, double probability, int start, int length)
    {
        this.name = name;
        this.type = type;
        this.value = value;
        this.probability = probability;
        this.start = start;
        this.length = length;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public double getProbability()
    {
        return probability;
    }

    public void setProbability(double probability)
    {
        this.probability = probability;
    }

    public int getStart()
    {
        return start;
    }

    public void setStart(int start)
    {
        this.start = start;
    }

    public int getLength()
    {
        return length;
    }

    public void setLength(int length)
    {
        this.length = length;
    }

    @Override
    public String toString()
    {
        return "NamedEntity{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", proabability=" + probability +
                ", start=" + start +
                ", length=" + length +
                '}';
    }
}
