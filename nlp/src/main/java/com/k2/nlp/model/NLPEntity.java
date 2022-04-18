/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.model;

import opennlp.tools.util.Span;

public class NLPEntity
{
    private String type = "" ;

    private String lemma = "";

    private String value = "";

    private double probability = 0D;

    private int start = -1;

    private int length = 0;

    public NLPEntity()
    {
    }

    public NLPEntity(Span span, String value)
    {
        this.value = value;
        this.lemma = value;
        this.type = (span.getType() == null) ? "UNKNOWN" : span.getType();
        this.start = span.getStart();
        this.length = span.length();
        this.probability = span.getProb();
    }

    public NLPEntity(Span span, String value, String type)
    {
        this.value = value;
        this.lemma = value;
        this.type = (span.getType() == null) ? type : span.getType();
        this.start = span.getStart();
        this.length = span.length();
        this.probability = span.getProb();
    }

    public NLPEntity(Span span, String value, String lemma, String type)
    {
        this.value = value;
        this.lemma = lemma;
        this.type = (span.getType() == null) ? type : span.getType();
        this.start = span.getStart();
        this.length = span.length();
        this.probability = span.getProb();
    }

    public NLPEntity(String type, String value, double probability, int start, int length)
    {
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

    public String getLemma()
    {
        return lemma;
    }

    public void setLemma(String lemma)
    {
        this.lemma = lemma;
    }

    public boolean better(NLPEntity that)
    {
        return (this.probability >= that.probability);
    }

    @Override
    public String toString()
    {
        return "NamedEntity{" +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", probability=" + probability +
                ", start=" + start +
                ", length=" + length +
                '}';
    }

    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof NLPEntity)) return false;

        NLPEntity that = (NLPEntity) obj;
        return (this.start == that.start && this.length == that.length && this.probability == that.probability);
    }
}
