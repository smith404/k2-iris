/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.service.impl;

import com.k2.core.component.BaseComponent;
import com.k2.core.exception.BaseException;
import com.k2.core.model.Category;
import com.k2.core.model.Clause;
import com.k2.core.model.Prediction;
import com.k2.core.service.UtilsService;
import com.k2.nlp.NLPProperties;
import com.k2.nlp.model.NamedEntity;
import com.k2.nlp.service.NLPService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.doccat.*;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.namefind.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service("NLPServiceImpl")
public class NLPServiceImpl extends BaseComponent implements NLPService
{
    @Autowired
    private NLPProperties nlpProperties;

    // Open NLP variables
    private LanguageDetector ld;
    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME tagger;
    private DictionaryLemmatizer lemmatizer;
    private ChunkerME chunker;

    // Model stores
    private final Map<Category, DocumentCategorizer> categorizers = new LinkedHashMap<>();
    private final Map<Category, TokenNameFinder> nameFinders = new LinkedHashMap<>();

    @PostConstruct
    public void init()
    {
        // Standard NLP Models
        initTokenizer();
        initTagger();
        initSentenceDetector();
        initLemmatizer();
        initChunker();

        // Language detection model
        initLanguageDetection();

        // Entity and category Models
        initDictEntityModels();
        initRegExEntityModels();
        initRegExCatEntityModels();
        initCompiledEntityModels();
        initCompiledCategoryModels();
    }

    public List<Category> getCategories()
    {

        return new ArrayList<>(categorizers.keySet());
    }

    public List<Category> getEntityModels()
    {

        return new ArrayList<>(nameFinders.keySet());
    }

    public void initTokenizer()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getTokenizerModel());
            InputStream dataIn = new FileInputStream(modelFile);

            TokenizerModel tokenModel = new TokenizerModel(dataIn);
            tokenizer = new TokenizerME(tokenModel);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initTagger()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getTaggerModel());
            InputStream dataIn = new FileInputStream(modelFile);

            POSModel taggerModel = new POSModel(dataIn);
            tagger = new POSTaggerME(taggerModel);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initSentenceDetector()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getSentModel());
            InputStream dataIn = new FileInputStream(modelFile);

            SentenceModel sentenceModel = new SentenceModel(dataIn);
            sentenceDetector = new SentenceDetectorME(sentenceModel);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initLemmatizer()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getLemmatizerDict());
            InputStream dataIn = new FileInputStream(modelFile);

            lemmatizer = new DictionaryLemmatizer(dataIn);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initChunker()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getChunkerModel());
            InputStream dataIn = new FileInputStream(modelFile);

            ChunkerModel chunkerModel = new ChunkerModel(dataIn);
            chunker = new ChunkerME(chunkerModel);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initLanguageDetection()
    {
        try
        {
            Path location = Paths.get(nlpProperties.getNlpRootLocation());
            File modelFile = new File(location + "/" + nlpProperties.getLanguageModel());
            InputStream dataIn = new FileInputStream(modelFile);

            LanguageDetectorModel languageModel = new LanguageDetectorModel(dataIn);
            ld = new LanguageDetectorME(languageModel);
        }
        catch(Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }

    }

    public void initDictEntityModels()
    {
        try
        {
            File f = new File(nlpProperties.getModelRootLocation() + "/entity/");
            File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".dict"));

            assert matchingFiles != null;
            for (File file : matchingFiles)
            {
                try
                {
                    Category cat = new Category(file.getName(), "ner-dict");

                    InputStream dataIn = new FileInputStream(file);

                    opennlp.tools.dictionary.Dictionary model = new opennlp.tools.dictionary.Dictionary();
                    List<String> lines = new BufferedReader(new InputStreamReader(dataIn, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
                    for(String line: lines)
                    {
                        StringList sl = new StringList(line.split("\\s+"));
                        model.put(sl);
                    }

                    DictionaryNameFinder nameFinder = new DictionaryNameFinder(model);

                    nameFinders.put(cat, nameFinder);

                    log.info("NLP ** Processing entity model, found file for entity type: {}", cat.getFriendlyName());
                }
                catch (FileNotFoundException ex)
                {
                    //ex.printStackTrace();
                    log.warn("NLP ** Processing entity model, could not find file for entity type: {}", file.getName());
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initRegExEntityModels()
    {
        try
        {
            File f = new File(nlpProperties.getModelRootLocation() + "/entity/");
            File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".regex"));

            assert matchingFiles != null;
            for (File file : matchingFiles)
            {
                try
                {
                    Category cat = new Category(file.getName(), "ner-regex");

                    InputStream dataIn = new FileInputStream(file);

                    List<String> regexs = new BufferedReader(new InputStreamReader(dataIn, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

                    List<Pattern> patterns = new ArrayList<>();
                    for (String regex : regexs)
                    {
                        Pattern p = Pattern.compile(regex);
                        patterns.add(p);
                        //out.println("Adding pattern: " + p);
                    }

                    RegexNameFinder nameFinder = new RegexNameFinder(patterns.toArray(new Pattern[0]), cat.getName());
                    nameFinders.put(cat, nameFinder);

                    log.info("NLP ** Processing entity model, found file for entity type: {}", cat.getFriendlyName());
                }
                catch (FileNotFoundException ex)
                {
                    //ex.printStackTrace();
                    log.warn("NLP ** Processing entity model, could not find file for entity type: {}", file.getName());
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initRegExCatEntityModels()
    {
        try
        {
            File f = new File(nlpProperties.getModelRootLocation() + "/entity/");
            File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".rxcat"));

            assert matchingFiles != null;
            for (File file : matchingFiles)
            {
                try
                {
                    Category cat = new Category(file.getName(), "ner-regex");

                    InputStream dataIn = new FileInputStream(file);

                    List<String> regexs = new BufferedReader(new InputStreamReader(dataIn, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

                    Map<String,List<Pattern>> entryMap = new HashMap<>();
                    List<Pattern> patterns;
                    for (String regex : regexs)
                    {
                        String[] parts = regex.split("\t");
                        if (parts.length == 2)
                        {
                            patterns = entryMap.get(parts[0]);
                            if (patterns == null) patterns = new ArrayList<>();
                            Pattern p = Pattern.compile(parts[1]);
                            patterns.add(p);
                            entryMap.put(parts[0], patterns);
                        }
                    }

                    Map<String,Pattern[]> regexMap = new HashMap<>();

                    for (String key : entryMap.keySet())
                    {
                        regexMap.put(key, entryMap.get(key).toArray(new Pattern[0]));
                    }

                    RegexNameFinder nameFinder = new RegexNameFinder(regexMap);
                    nameFinders.put(cat, nameFinder);

                    log.info("NLP ** Processing entity model, found file for entity type: {}", cat.getFriendlyName());
                }
                catch (FileNotFoundException ex)
                {
                    //ex.printStackTrace();
                    log.warn("NLP ** Processing entity model, could not find file for entity type: {}", file.getName());
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initCompiledEntityModels()
    {
        try
        {
            File f = new File(nlpProperties.getModelRootLocation() + "/entity/");
            File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".bin"));

            assert matchingFiles != null;
            for (File file : matchingFiles)
            {
                try
                {
                    Category cat = new Category(file.getName(), "ner-me");

                    InputStream dataIn = new FileInputStream(file);
                    TokenNameFinderModel model = new TokenNameFinderModel(dataIn);
                    NameFinderME nameFinder = new NameFinderME(model);

                    nameFinders.put(cat, nameFinder);

                    log.info("NLP ** Processing entity model, found file for entity type: {}", cat.getFriendlyName());
                }
                catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                    log.warn("NLP ** Processing entity model, could not find file for entity type: {}", file.getName());
                }
            }
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public void initCompiledCategoryModels()
    {
        try
        {
            File f = new File(nlpProperties.getModelRootLocation() + "/category/");
            File[] matchingFiles = f.listFiles((dir, name) -> name.endsWith(".bin"));

            assert matchingFiles != null;
            for (File file : matchingFiles)
            {
                try
                {
                    Category cat = new Category(file.getName(), "docat-me");

                    DoccatModel model = new DoccatModel(file);
                    DocumentCategorizer categorizer = new DocumentCategorizerME(model);

                    categorizers.put(cat, categorizer);
                    log.info("NLP ** Processing categorizers, found file for category: {}", cat.getFriendlyName());
                }
                catch (FileNotFoundException ex)
                {
                    ex.printStackTrace();
                    log.warn("NLP ** Processing categorizers, could not find file for category: {}", file.getName());
                }
            }
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            log.warn(UtilsService.makeExceptionWarning(ex),"NLP");
        }
    }

    public String[] sentences(String text)
    {
        return sentenceDetector.sentDetect(text);
    }

    public Span[] sentencesPos(String text)
    {
        return sentenceDetector.sentPosDetect(text);
    }

    public String[] tokenize(String text)
    {
        return tokenizer.tokenize(text);
    }

    public Span[] tokenizePos(String text)
    {
        return tokenizer.tokenizePos(text);
    }

    public NamedEntity[] tokenizeValues(String text)
    {
        Span[] spans =  tokenizer.tokenizePos(text);
        String[] values = tokenizer.tokenize(text);

        if (spans.length != values.length)
        {
            log.warn("Error creating token array");
            return new NamedEntity[0];
        }

        NamedEntity[] tokens = new NamedEntity[spans.length];
        for(int i=0; i<spans.length; ++i)
        {
            NamedEntity t = new NamedEntity(spans[i], values[i]);
            tokens[i] = t;
        }

        return tokens;
    }

    public String[] tags(String[] tokens)
    {
        return tagger.tag(tokens);
    }

    public String[] lemmas(String[] tokens, String[] tags)
    {
        return lemmatizer.lemmatize(tokens, tags);
    }

    public String[] chunks(String[] tokens, String[] tags)
    {
        return chunker.chunk(tokens, tags);
    }

    private static final String CLAUSE_SPLIT_REGEX = "\\r\\n|\\n|\\r";
    public List<Clause> splitClauses(String text)
    {
        List<Clause> clauses = new ArrayList<>();

        String[] paragraphs = text.split(CLAUSE_SPLIT_REGEX);
        for (String paragraph : paragraphs)
        {
            if (paragraph.trim().length()>0)
            {
                Clause c = new Clause();
                c.setBody(paragraph.trim());
                c.setType("Auto Generated");
                c.setSubtype("Who knows...");
                clauses.add(c);
            }
        }

        return clauses;
    }

    public List<NamedEntity> entityDetect(Category entityModel, String text, double tolerance)
    {
        List<NamedEntity> results = new ArrayList<>();

        // Convert to a percentage
        if (tolerance > 1)
        {
            tolerance = tolerance / 100D;
        }

        String[] tokens = tokenize(text);
/*
        for (int i=0; i< tokens.length; ++i)
        {
            System.out.print("[" + tokens[i] + "],");
        }
        System.out.println();
*/
        TokenNameFinder nameFinder = nameFinders.get(entityModel);
        if (nameFinder != null)
        {
            // Forgets all adaptive data which was collected during previous calls
            nameFinder.clearAdaptiveData();

            Span[] nameSpans;
            boolean useTokenPos = true;

            // For RegEx models we use character position not token position
            if (nameFinder instanceof  RegexNameFinder)
            {
                nameSpans = ((RegexNameFinder) nameFinder).find(text);
                useTokenPos = false;
                //System.out.println("Spans found: " + nameSpans.length);
            }
            else
                nameSpans = nameFinder.find(tokens);

            double[] probs= new double[0];

            // If we have a maximum-entropy-based name finder we can find probabilities
            if (nameFinder instanceof NameFinderME)
            {
                nameSpans = NameFinderME.dropOverlappingSpans(nameSpans);
                probs = ((NameFinderME)nameFinder).probs();
            }

            // Finding the names and locations
            for (int ni = 0; ni < nameSpans.length; ni++)
            {
                Span s = nameSpans[ni];
                //System.out.println("Found: " + s);
                NamedEntity ne = new NamedEntity();
                ne.setName(entityModel.getName());
                ne.setType(s.getType());
                ne.setStart(s.getStart());
                ne.setLength(s.length());
                StringBuilder val = new StringBuilder();

                if (useTokenPos)
                {
                    for (int l=s.getStart(); l < s.getStart() + s.length(); ++l)
                        val.append(tokens[l]).append(" ");
                }
                else
                {
                    // Simple substring operation for character positions
                    val.append(text, s.getStart(), s.getEnd());
                }
                ne.setValue(val.toString().trim());

                if (probs.length > 0)
                    ne.setProbability(probs[ni]);
                else
                    ne.setProbability(1);

                results.add(ne);
                //System.out.println("Found: " + ne);
            }
        }

        return results;
    }

    public List<NamedEntity> entityDetect(String entityModel, String text, double tolerance)
    {
        AtomicReference<Category> cat = new AtomicReference<>();
        for (Map.Entry<Category, TokenNameFinder> entry : nameFinders.entrySet())
        {
            Category k = entry.getKey();
            if (k.getFriendlyName().equalsIgnoreCase(entityModel)) cat.set(k);
        }

        if (cat.get() != null)
            return entityDetect(cat.get(), text, tolerance);
        else
            return new ArrayList<>();
    }

    public List<NamedEntity> entityDetect(String text, double tolerance)
    {
        List<NamedEntity> results = new ArrayList<>();

        for (Category entityModel : nameFinders.keySet())
        {
            List<NamedEntity> nes = entityDetect(entityModel, text, tolerance);

            results.addAll(nes);
        }

        return results;
    }

    public List<Prediction> classifyContent(String text, String name)
    {
        List<Prediction> predictions = new ArrayList<>();

        DocumentCategorizer categorizer = categorizers.get(name);
        if (categorizer != null)
        {
            return classifyContent(categorizer, text, name);
        }
        return predictions;
    }

    public List<Prediction> classifyContent(DocumentCategorizer categorizer, String text, String name)
    {
        List<Prediction> predictions = new ArrayList<>();

        String[] tokens = tokenize(text);
        if (categorizer != null)
        {
            Map<String, Double> map = categorizer.scoreMap(tokens);
            for (Map.Entry<String, Double> item : map.entrySet())
            {
                Prediction p = new Prediction();
                p.setModel(name);
                p.setCategory(item.getKey());
                p.setProbability(item.getValue());
                predictions.add(p);
            }
        }

        return predictions;
    }

    public Prediction detectLanguage(String text)
    {
        Prediction pr = new Prediction();
        pr.setCategory("eng");

        if (ld == null) return pr;

        Language[] languages = ld.predictLanguages(text);

        double score = -1;
        for (Language lg : languages)
        {
            if (lg.getConfidence() > score)
            {
                score = lg.getConfidence();
                pr.setCategory(lg.getLang());
                pr.setProbability(lg.getConfidence());
            }
        }

        return pr;
    }

    public void createCategoryModel(String data, String category, String lang)
    {
        try
        {
            if (data != null)
            {
                Path location = Paths.get(nlpProperties.getModelRootLocation());

                File modelFile = new File(location + "/category/" + lang + "-" + category + ".bin");

                File tempFile = File.createTempFile("model", ".tmp");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                writer.write(data);

                // Read, process and store the training data
                InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(tempFile);
                ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
                ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
                TrainingParameters params = new TrainingParameters();
                params.put(TrainingParameters.ITERATIONS_PARAM, nlpProperties.getIterations());
                params.put(TrainingParameters.CUTOFF_PARAM, nlpProperties.getCutOff());
                params.put("DataIndexer", nlpProperties.getDataIndexer());
                params.put(TrainingParameters.ALGORITHM_PARAM, nlpProperties.getAlgorithm());
                DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

                DocumentCategorizer categorizer = new DocumentCategorizerME(model);

                BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
                model.serialize(modelOut);
                modelOut.close();

                categorizers.put(new Category(category,"doccat"), categorizer);

                tempFile.deleteOnExit();
            }
        }
        catch (Exception ex)
        {
            throw new BaseException(ex.getMessage(), ex);
        }
    }

    public void createEntityModel(String data, String category, String lang)
    {
        try
        {
            if (data != null)
            {
                Path location = Paths.get(nlpProperties.getModelRootLocation());

                File modelFile = new File(location + "/entity/" + lang + "-" + category + ".bin");

                File tempFile = File.createTempFile("model", ".tmp");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                writer.write(data);

                // Read, process and store the training data
                InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(tempFile);
                ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");

                ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

                TrainingParameters params = new TrainingParameters();
                params.put(TrainingParameters.ITERATIONS_PARAM, nlpProperties.getIterations());
                params.put(TrainingParameters.CUTOFF_PARAM, nlpProperties.getCutOff());
                params.put(TrainingParameters.TRAINER_TYPE_PARAM, EventTrainer.EVENT_VALUE);
                params.put(TrainingParameters.ALGORITHM_PARAM, nlpProperties.getAlgorithm());

                TokenNameFinderFactory nameFinderFactory = TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec());
                TokenNameFinderModel model = NameFinderME.train("en", category, sampleStream, params, nameFinderFactory);

                NameFinderME nameFinder = new NameFinderME(model);

                BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
                model.serialize(modelOut);
                modelOut.close();

                nameFinders.put(new Category(category,"ner"), nameFinder);
                tempFile.deleteOnExit();
            }
        }
        catch (Exception ex)
        {
            throw new BaseException(ex.getMessage(), ex);
        }
    }
}
