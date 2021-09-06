/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the
 * author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains
 * with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlg.model;

import java.util.List;

public class Chunk
{
    private String chunkTag;

    private List<LexToken> tokens;

    public Chunk(String chunkTag, List<LexToken> tokens)
    {
        this.chunkTag = chunkTag;
        this.tokens = tokens;
    }

    public boolean isPreposition()
    {
        return chunkTag.equalsIgnoreCase("PP") ||
                chunkTag.equalsIgnoreCase("WHPP");
    }

    public boolean isConjunction()
    {
        return chunkTag.equalsIgnoreCase("CONJP");
    }

    public boolean isNoun()
    {
        return chunkTag.equalsIgnoreCase("NP") ||
                chunkTag.equalsIgnoreCase("NX") ||
                chunkTag.equalsIgnoreCase("WHNP");
    }

    public boolean isDeclarative()
    {
        return chunkTag.equalsIgnoreCase("S");
    }

    public boolean isVerb()
    {
        return chunkTag.equalsIgnoreCase("VP");
    }

    public boolean isAdverb()
    {
        return chunkTag.equalsIgnoreCase("ADVP") ||
                chunkTag.equalsIgnoreCase("WHAVP");
    }

    public boolean isAdjective()
    {
        return chunkTag.equalsIgnoreCase("ADJP") ||
                chunkTag.equalsIgnoreCase("WHADJP");
    }

    public String getChunkTag()
    {
        return chunkTag;
    }

    public void setChunkTag(String chunkTag)
    {
        this.chunkTag = chunkTag;
    }

    public List<LexToken> getTokens()
    {
        return tokens;
    }

    public void setTokens(List<LexToken> tokens)
    {
        this.tokens = tokens;
    }

    @Override
    public String toString()
    {
        return "Chunk{" +
                "chunkTag='" + chunkTag + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
