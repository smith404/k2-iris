/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlg.model;

import com.google.common.collect.Iterables;
import com.k2.nlg.service.impl.NLGServiceImpl;
import simplenlg.features.Feature;
import simplenlg.features.Tense;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.phrasespec.VPPhraseSpec;

import java.util.ArrayList;
import java.util.List;

public class Sentence
{
    private int index = 0;
    private String text;

    private List<LexToken> tokens = new ArrayList<>();
    private List<Chunk> rawChunks = new ArrayList<>();
    private List<Chunk> compressedChunks = new ArrayList<>();

    // Default constructor needed for Jackson serialization/de-serialization
    public Sentence()
    {
    }
    public Sentence(String text)
    {
        this.text = text;
/*
        // Get the tokens and suggested tags
        boolean[] raw = new boolean[tokens.length];

        // Find any hard coded POS tags
        for(int i=0; i < tokens.length; ++i)
        {
            raw[i] = false;
            if (tokens[i].contains("_"))
            {
                String userToken = tokens[i];
                String userTag = userToken.substring(userToken.indexOf("_")+1);
                userToken =  userToken.substring(0, userToken.indexOf("_"));

                tokens[i] = userToken;
                if (userTag.equalsIgnoreCase("RAW"))
                {
                    raw[i] = true;
                }
                else
                {
                    tags[i] = userTag;
                }
            }
        }

        // Convert tokens to lemmas and chunks
        String[] lemmas = nlpService.lemmas(tokens, tags);
        String[] chunks = nlpService.chunks(tokens, tags);

        // Store the raw tokens
        for(int i=0; i < tokens.length; ++i)
            this.tokens.add(new LexToken(tokens[i], tags[i], lemmas[i], chunks[i], raw[i], ""));

        // Convert the tokens to chunks
        reset();
        while(!isEnd())
        {
            Chunk chunk = getNextChunk();

            rawChunks.add(chunk);
            compressedChunks.add(compressChunk(chunk));
        }
*/
    }

    public DocumentElement getDocumentElement(NLGServiceImpl nlgService)
    {
        return getDocumentElement(nlgService, null);
    }

    public DocumentElement getDocumentElement(NLGServiceImpl nlgService, Tense tense)
    {
        DocumentElement de = nlgService.createSentence();

        for(Chunk chunk : compressedChunks)
        {
            NLGElement element = nlgService.makePhraseElement(chunk);
            if (tense != null && element instanceof VPPhraseSpec)
            {
                // We have a verb phrase so let's set a tense
                element.setFeature(Feature.TENSE, tense);
            }

            de.addComponent(element);
        }

        return de;
    }

    public void reset()
    {
        index = 0;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public boolean isEnd()
    {
        return (index >= tokens.size());
    }

    private String getChunckTag(String tag)
    {
        if (tag.length() > 2)
        {
            tag = tag.substring(2);
        }

        return tag;
    }

    public Chunk getNextChunk()
    {
        String chunkTag = "";
        List<LexToken> tokenList = new ArrayList<>();

        if (index < tokens.size())
        {
            LexToken rawToken = tokens.get(index);
            LexToken newToken = new LexToken(rawToken.getText(), rawToken.getPosTag(), rawToken.getLemmaText(), rawToken.getChunkTag(), rawToken.isRaw(), rawToken.getDecoration());

            tokenList.add(newToken);
            chunkTag = getChunckTag(rawToken.getChunkTag());
            ++index;
        }

        while(index < tokens.size() && tokens.get(index).getChunkTag().startsWith("I"))
        {
            LexToken rawToken = tokens.get(index);
            LexToken newToken = new LexToken(rawToken.getText(), rawToken.getPosTag(), rawToken.getLemmaText(), rawToken.getChunkTag(), rawToken.isRaw(), rawToken.getDecoration());

            tokenList.add(newToken);
            ++index;
        }

        if (tokenList.size() > 0)
            return new Chunk(chunkTag, tokenList);
        else
            return null;
    }

    public Chunk compressChunk(final Chunk chunk)
    {
        List<LexToken> tokenList = new ArrayList<>();
        Chunk returnChunk = new Chunk(chunk.getChunkTag(), tokenList);

        chunk.getTokens().forEach(token -> {
            LexToken last = null;

            if (tokenList.size() > 0) last = Iterables.getLast(tokenList);

            if (last != null && last.canAppend(token, true))
                last.appendToken(token);
            else
            {
                tokenList.add(new LexToken(token.getText(), token.getPosTag(), token.getLemmaText(), token.getChunkTag(), token.isRaw(), token.getDecoration()));
            }
        });

        return returnChunk;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public List<LexToken> getTokens()
    {
        return tokens;
    }

    public void setTokens(List<LexToken> tokens)
    {
        this.tokens = tokens;
    }

    public List<Chunk> getRawChunks()
    {
        return rawChunks;
    }

    public void setRawChunks(List<Chunk> rawChunks)
    {
        this.rawChunks = rawChunks;
    }

    public List<Chunk> getCompressedChunks()
    {
        return compressedChunks;
    }

    public void setCompressedChunks(List<Chunk> compressedChunks)
    {
        this.compressedChunks = compressedChunks;
    }

    @Override
    public String toString()
    {
        return "\nSentence{" +
                "text='" + text + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
