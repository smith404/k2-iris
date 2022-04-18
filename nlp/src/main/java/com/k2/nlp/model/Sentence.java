package com.k2.nlp.model;

import opennlp.tools.util.Span;

public class Sentence
{
    private NLPEntity theSentence;

    public Sentence(NLPEntity theSentence)
    {
        this.theSentence = theSentence;
    }

    public Sentence(Span theSentence, String text)
    {
        this.theSentence = new NLPEntity(theSentence, text, "SEN");
    }

    public NLPEntity getTheSentence()
    {
        return theSentence;
    }

    public void setTheSentence(NLPEntity theSentence)
    {
        this.theSentence = theSentence;
    }
}
