package com.k2.nlp.model;

import opennlp.tools.util.Span;

public class Sentence
{
    private NamedEntity theSentence;

    public Sentence(NamedEntity theSentence)
    {
        this.theSentence = theSentence;
    }

    public Sentence(Span theSentence, String text)
    {
        this.theSentence = new NamedEntity(theSentence, text);
    }

    public NamedEntity getTheSentence()
    {
        return theSentence;
    }

    public void setTheSentence(NamedEntity theSentence)
    {
        this.theSentence = theSentence;
    }
}
