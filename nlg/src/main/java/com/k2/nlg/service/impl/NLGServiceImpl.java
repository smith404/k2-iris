package com.k2.nlg.service.impl;

import com.k2.nlg.model.Chunk;
import com.k2.nlg.model.LexToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import simplenlg.features.Feature;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.DocumentElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("NLGServiceImpl")
public class NLGServiceImpl
{
    private final Lexicon lexicon = Lexicon.getDefaultLexicon();
    private final NLGFactory nlgFactory = new NLGFactory(lexicon);

    /*
        public Paragraph processTextToParagraph(String paragraph)
        {
            Lexicon lexicon = Lexicon.getDefaultLexicon();
            NLGFactory nlgFactory = new NLGFactory(lexicon);
            Realiser realiser = new Realiser(lexicon);

            if (paragraph.trim().length() > 0)
            {
                try
                {
                    Paragraph para = new Paragraph(paragraph, nlpService);
                    para.setLang(nlpService.detectLanguage(paragraph).getCategory());
                    return para;
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            return null;
        }

        public List<Sentence> getSentences(String paragraph)
        {
            Lexicon lexicon = Lexicon.getDefaultLexicon();
            NLGFactory nlgFactory = new NLGFactory(lexicon);
            Realiser realiser = new Realiser(lexicon);

            if (paragraph.trim().length() > 0)
            {
                try
                {
                    Paragraph para = new Paragraph(paragraph, nlpService);

                    return para.getSentences();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            return new ArrayList<>();
        }

        public TextResponse realize(Sentence sentence, String tense)
        {
            Tense t = Tense.PRESENT;

            if (tense.equalsIgnoreCase("fu")) t = Tense.FUTURE;
            else if (tense.equalsIgnoreCase("pa")) t = Tense.PAST;

            return realize(sentence, t);
        }

        public TextResponse realize(Sentence sentence, Tense tense)
        {
            Lexicon lexicon = Lexicon.getDefaultLexicon();
            NLGFactory nlgFactory = new NLGFactory(lexicon);
            Realiser realiser = new Realiser(lexicon);

            TextResponse retVal = new TextResponse();

            try
            {
                DocumentElement de = sentence.getDocumentElement(this, tense);
                retVal.setResult(realiser.realiseSentence(de));
            }
            catch (Exception ex)
            {
                retVal.setResult(ex.getMessage());
                retVal.setSucess(false);
                ex.printStackTrace();
            }

            return retVal;
        }
    */
    public DocumentElement createSentence()
    {
        return nlgFactory.createSentence();
    }

    public SPhraseSpec createClause(Chunk chunk)
    {
        SPhraseSpec clause = nlgFactory.createClause();

        List<LexToken> tokenList = new ArrayList<>();

        chunk.getTokens().forEach(token -> {

        });

        return clause;
    }

    public SPhraseSpec createPhrase(List<LexToken> chunk)
    {
        SPhraseSpec clause = nlgFactory.createClause();

        List<LexToken> tokenList = new ArrayList<>();

        chunk.forEach(token -> {

        });

        return clause;
    }

    public NPPhraseSpec makeNounPhrase(List<LexToken> tokens)
    {
        NPPhraseSpec np = nlgFactory.createNounPhrase();

        for(LexToken t : tokens)
        {
            if (t.isDeterminer()) addNounDeterminer(np, t);
            else if (t.isNoun())
            {
                np.setNoun(t.getDecoratedText());
            }
            else np.addModifier(makePhraseElement(t));
        }

        return np;
    }

    public VPPhraseSpec makeVerbPhrase(List<LexToken> tokens)
    {
        VPPhraseSpec vp = nlgFactory.createVerbPhrase();

        boolean foundVerb = false;
        for(LexToken t : tokens)
        {
            if (t.isVerb())
            {
                vp.setVerb(t.getDecoratedText());
                foundVerb = true;
            }
            else if (foundVerb)
            {
                vp.addPostModifier(makePhraseElement(t));
            }
            else
            {
                vp.addPreModifier(makePhraseElement(t));
            }
        }

        return vp;
    }

    public AdjPhraseSpec makeAdjectivePhrase(List<LexToken> tokens)
    {
        AdjPhraseSpec ap = nlgFactory.createAdjectivePhrase();

        boolean foundAdjective = false;
        for(LexToken t : tokens)
        {
            if (t.isAdjective())
            {
                ap.setAdjective(t.getDecoratedText());
                foundAdjective = true;
            }
            else if (foundAdjective)
            {
                ap.addPostModifier(makePhraseElement(t));
            }
            else
            {
                ap.addPreModifier(makePhraseElement(t));
            }
        }

        return ap;
    }

    public AdvPhraseSpec makeAdverbPhrase(List<LexToken> tokens)
    {
        AdvPhraseSpec ap = nlgFactory.createAdverbPhrase();

        boolean foundAdverb = false;
        for(LexToken t : tokens)
        {
            if (t.isAdverb())
            {
                ap.setAdverb(t.getDecoratedText());
                foundAdverb = true;
            }
            else if (foundAdverb)
            {
                ap.addPostModifier(makePhraseElement(t));
            }
            else
            {
                ap.addPreModifier(makePhraseElement(t));
            }
        }

        return ap;
    }

    public PPPhraseSpec makePrepositionPhrase(List<LexToken> tokens)
    {
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();

        boolean Preposition = false;
        for(LexToken t : tokens)
        {
            if (t.isPreposition())
            {
                pp.setPreposition(t.getDecoratedText());
                Preposition = true;
            }
            else if (Preposition)
            {
                pp.addPostModifier(makePhraseElement(t));
            }
            else
            {
                pp.addPreModifier(makePhraseElement(t));
            }
        }

        return pp;
    }

    public NLGElement makeSimplePhrase(List<LexToken> tokens)
    {
        NLGElement element = null;

        if (tokens.size() == 1)
            element = nlgFactory.createWord(tokens.get(0).getDecoratedText(), tokens.get(0).getLexicalCategory());
        else if (tokens.size() > 1)
        {
            element = nlgFactory.createNLGElement(tokens.get(0).getDecoratedText(), tokens.get(0).getLexicalCategory());
            // Need to add rest
        }

        return element;
    }

    public NLGElement makePhraseElement(Chunk chunk)
    {
        if (chunk.isNoun())
            return makeNounPhrase(chunk.getTokens());
        else if (chunk.isVerb())
            return makeVerbPhrase(chunk.getTokens());
        else if (chunk.isAdjective())
            return makeAdjectivePhrase(chunk.getTokens());
        else if (chunk.isAdverb())
            return makeAdverbPhrase(chunk.getTokens());
        else if (chunk.isPreposition())
            return makePrepositionPhrase(chunk.getTokens());

        return makeSimplePhrase(chunk.getTokens());
    }

    public NLGElement makePhraseElement(LexToken token)
    {
        if (token.isNoun())
            return nlgFactory.createNounPhrase(token.getDecoratedText());
        else if (token.isVerb())
            return nlgFactory.createVerbPhrase(token.getDecoratedText());
        else if (token.isAdjective())
            return nlgFactory.createAdjectivePhrase(token.getDecoratedText());
        else if (token.isAdverb())
            return nlgFactory.createAdverbPhrase(token.getDecoratedText());
        else if (token.isPreposition())
            return nlgFactory.createPrepositionPhrase(token.getDecoratedText());

        return nlgFactory.createStringElement(token.getDecoratedText());
    }

    public void addNounDeterminer(NPPhraseSpec noun, LexToken determiner)
    {
        NPPhraseSpec np = nlgFactory.createNounPhrase(determiner.getDecoratedText());
        noun.setDeterminer(np);
    }

    public void addNounPreModifier(NPPhraseSpec noun, LexToken determiner)
    {
        AdjPhraseSpec adj = nlgFactory.createAdjectivePhrase(determiner.getDecoratedText());
        noun.addPreModifier(adj);
    }

    public PPPhraseSpec makePrepositionPhrase(LexToken prep, NLGElement noun)
    {
        PPPhraseSpec pp = nlgFactory.createPrepositionPhrase(prep.getDecoratedText());
        pp.addComplement(noun);

        return pp;
    }

    public CoordinatedPhraseElement combine(Object first, Object second)
    {
        return nlgFactory.createCoordinatedPhrase(first, second);
    }

    public void addCoordinate(CoordinatedPhraseElement list, Object object)
    {
        list.addCoordinate(object);
    }

    public void makeOrList(CoordinatedPhraseElement list)
    {
        list.setFeature(Feature.CONJUNCTION, "or");
    }
}
