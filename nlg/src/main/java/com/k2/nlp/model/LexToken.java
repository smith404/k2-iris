package com.k2.nlp.model;

import org.apache.commons.lang3.StringUtils;
import simplenlg.framework.LexicalCategory;

public class LexToken
{
    public final static String decorationPlaceHolder = "{t}";
    public final static String SYMBOLS = "%!";

    private String text;

    /**
     * Part-Of-Speech tags
     *
     * CC	Coordinating conjunction
     * CD	Cardinal number
     * DT	Determiner
     * EX	Existential there
     * FW	Foreign word
     * IN	Preposition or subordinating conjunction
     * JJ	Adjective
     * JJR	Adjective, comparative
     * JJS	Adjective, superlative
     * LS	List item marker
     * MD	Modal
     * NN	Noun, singular or mass
     * NNS	Noun, plural
     * NNP	Proper noun, singular
     * NNPS	Proper noun, plural
     * PDT	Predeterminer
     * POS	Possessive ending
     * PRP	Personal pronoun
     * PRP$	Possessive pronoun
     * RB	Adverb
     * RBR	Adverb, comparative
     * RBS	Adverb, superlative
     * RP	Particle
     * SYM	Symbol
     * TO	to
     * UH	Interjection
     * VB	Verb, base form
     * VBD	Verb, past tense
     * VBG	Verb, gerund or present participle
     * VBN	Verb, past participle
     * VBP	Verb, non-3rd person singular present
     * VBZ	Verb, 3rd person singular present
     * WDT	Wh-determiner
     * WP	Wh-pronoun
     * WP$	Possessive wh-pronoun
     * WRB	Wh-adverb
     */

    private String posTag;

    private String lemmaText;

    /**
     * Chunk tags
     *
     * S: Simple declarative clause, i.e. one that is not introduced by a (possible empty) subordinating conjunction or a wh-word and that does not exhibit subject-verb inversion.
     * SBAR: Clause introduced by a (possibly empty) subordinating conjunction.
     * SBARQ: Direct question introduced by a wh-word or a wh-phrase. Indirect questions and relative clauses should be bracketed as SBAR, not SBARQ.
     * SINV: Inverted declarative sentence, i.e. one in which the subject follows the tensed verb or modal.
     * SQ: Inverted yes/no question, or main clause of a wh-question, following the wh-phrase in SBARQ.
     * ADJP: Adjective Phrase.
     * ADVP: Adverb Phrase.
     * CONJP: Conjunction Phrase.
     * FRAG: Fragment.
     * INTJ: Interjection. Corresponds approximately to the part-of-speech tag UH.
     * LST: List marker. Includes surrounding punctuation.
     * NAC: Not a Constituent; used to show the scope of certain prenominal modifiers within an NP.
     * NP: Noun Phrase.
     * NX: Used within certain complex NPs to mark the head of the NP. Corresponds very roughly to N-bar
     * PP: Prepositional Phrase.
     * PRN: Parenthetical.
     * PRT: Particle. Category for words that should be tagged RP.
     * QP: Quantifier Phrase (i.e. complex measure/amount phrase); used within NP.
     * RRC: Reduced Relative Clause.
     * UCP: Unlike Coordinated Phrase.
     * VP: Verb Phrase.
     * WHADJP: Wh-adjective Phrase. Adjectival phrase containing a wh-adverb, as in how hot.
     * WHAVP: Wh-adverb Phrase. Introduces a clause with an NP gap. May be null (containing the 0 complementizer) or lexical, containing a wh-adverb such as how or why.
     * WHNP: Wh-noun Phrase. Introduces a clause with an NP gap. May be null (containing the 0 complementizer) or lexical, containing some wh-word, e.g. who, which book, whose daughter, none of which, or how many leopards.
     * WHPP: Wh-prepositional Phrase. Prepositional phrase containing a wh-noun phrase (such as of which or by whose authority) that either introduces a PP gap or is contained by a WHNP.
     * X: Unknown, uncertain, or un-bracketable. X is often used for bracketing typos and in bracketing the...the-constructions.
     */

    private String chunkTag;

    /**
     * String in the form text{t}text
     * there {t} is replaced by the lemma text
     */
    private String decoration;

    public LexToken(String text, String posTag, String lemmaText, String chunkTag, String decoration)
    {
        this.text = text;
        this.posTag = posTag;
        this.lemmaText = lemmaText;
        this.chunkTag = chunkTag;
        this.decoration = decoration;
    }

    public boolean canAppend(LexToken tokenToAppend)
    {
        return canAppend(tokenToAppend, false);
    }

    public boolean canAppend(LexToken tokenToAppend, boolean aggressive)
    {
        if (tokenToAppend == null) return false;

        if (aggressive)
            if (getPosTag().length() > 1 && tokenToAppend.getPosTag().length() > 1)
            {
                // Like "10 Elephants"
                if (isNumber()) return true;

                // Create the infinitive form
                if (isTo() && tokenToAppend.isVerb()) return true;

                // Only check first two characters of the POS tag
                return (tokenToAppend.getPosTag().substring(0,1).equalsIgnoreCase(getPosTag().substring(0,1)));
            }

        return (tokenToAppend.getPosTag().equalsIgnoreCase(getPosTag()));
    }

    public void appendToken(final LexToken tokenToAppend)
    {
        String spacer = " ";

        if (tokenToAppend.isSymbol()) spacer = "";

        this.text = this.text +  spacer + tokenToAppend.getText();
        this.lemmaText = this.lemmaText + spacer + tokenToAppend.getLemmaText();

        // The last token wins the POS tag prize
        this.posTag = tokenToAppend.posTag;
    }

    public String getDecoratedText()
    {
        if (decoration.length() == 0) return getLemmaText();
        else if (decoration.contains(decorationPlaceHolder))
        {
            return decoration.replace(decorationPlaceHolder, getLemmaText());
        }
        else return decoration + getLemmaText();
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String getPosTag()
    {
        return posTag;
    }

    public void setPosTag(String posTag)
    {
        this.posTag = posTag;
    }

    public String getLemmaText()
    {
        if (posTag.equalsIgnoreCase("NNP"))
        {
            return StringUtils.capitalize(text);
        }

        return lemmaText.startsWith("O") ? text : lemmaText;
    }

    public void setLemmaText(String lemmaText)
    {
        this.lemmaText = lemmaText;
    }

    public String getChunkTag()
    {
        return chunkTag;
    }

    public void setChunkTag(String chunkTag)
    {
        this.chunkTag = chunkTag;
    }

    public String getDecoration()
    {
        return decoration;
    }

    public void setDecoration(String decoration)
    {
        // Don't want a null string
        if (decoration == null) return;

        this.decoration = decoration.trim();
    }

    public boolean isSymbol()
    {
        return SYMBOLS.contains(text);
    }

    public boolean isNumber()
    {
        return posTag.equalsIgnoreCase("CD");
    }

    public boolean isPreposition()
    {
        return posTag.equalsIgnoreCase("IN");
    }

    public boolean isDeterminer()
    {
        return posTag.equalsIgnoreCase("DT");
    }

    public boolean isConjunction()
    {
        return posTag.equalsIgnoreCase("CC") ||
                posTag.equalsIgnoreCase("IN");
    }

    public boolean isForeign()
    {
        return posTag.equalsIgnoreCase("FW");
    }

    public boolean isPossessive()
    {
        return posTag.equalsIgnoreCase("PRP$") ||
                posTag.equalsIgnoreCase("WP$");
    }

    public boolean isProNoun()
    {
        return posTag.equalsIgnoreCase("PRP") ||
                posTag.equalsIgnoreCase("PRP$") ||
                posTag.equalsIgnoreCase("WP") ||
                posTag.equalsIgnoreCase("WP$");
    }

    public boolean isNoun()
    {
        return posTag.equalsIgnoreCase("NN") ||
                posTag.equalsIgnoreCase("NNS") ||
                posTag.equalsIgnoreCase("NNP") ||
                posTag.equalsIgnoreCase("NNPS");
    }

    public boolean isProperNoun()
    {
        return posTag.equalsIgnoreCase("NNP") ||
                posTag.equalsIgnoreCase("NNPS");
    }

    public boolean isTo()
    {
        return posTag.equalsIgnoreCase("TO");
    }

    public boolean isPlural()
    {
        return posTag.equalsIgnoreCase("NNS") ||
                posTag.equalsIgnoreCase("NNPS");
    }

    public boolean isVerb()
    {
        return posTag.equalsIgnoreCase("VB") ||
                posTag.equalsIgnoreCase("VBD") ||
                posTag.equalsIgnoreCase("VBG") ||
                posTag.equalsIgnoreCase("VBN") ||
                posTag.equalsIgnoreCase("VBP") ||
                posTag.equalsIgnoreCase("VBZ");
    }

    public boolean isAdverb()
    {
        return posTag.equalsIgnoreCase("RB") ||
                posTag.equalsIgnoreCase("RBR") ||
                posTag.equalsIgnoreCase("RBS") ||
                posTag.equalsIgnoreCase("WRB");
    }

    public boolean isAdjective()
    {
        return posTag.equalsIgnoreCase("JJ") ||
                posTag.equalsIgnoreCase("JJR") ||
                posTag.equalsIgnoreCase("JJS");
    }

    public LexicalCategory getLexicalCategory()
    {
        if (isNoun()) return LexicalCategory.NOUN;
        else if (isVerb()) return LexicalCategory.VERB;
        else if (isSymbol()) return LexicalCategory.SYMBOL;
        else if (isAdverb()) return LexicalCategory.ADVERB;
        else if (isAdjective()) return LexicalCategory.ADJECTIVE;
        else if (isPreposition()) return LexicalCategory.PREPOSITION;
        else if (isProNoun()) return LexicalCategory.PRONOUN;
        else if (isDeterminer()) return LexicalCategory.DETERMINER;
        else if (isConjunction()) return LexicalCategory.CONJUNCTION;
        else return LexicalCategory.ANY;
    }

    @Override
    public String toString()
    {
        return "LexToken{" +
                "text='" + text + '\'' +
                ", posTag='" + posTag + '\'' +
                ", lemmaText='" + lemmaText + '\'' +
                ", chunkTag='" + chunkTag + '\'' +
                ", decoration='" + decoration + '\'' +
                '}';
    }
}

