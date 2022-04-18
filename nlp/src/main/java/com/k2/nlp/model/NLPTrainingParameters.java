/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp.model;

public class NLPTrainingParameters
{
    private String trainingDataLocation;
    private String modelRootLocation;
    private String nlpRootLocation;
    private boolean trainingSystem;
    private int iterations;
    private int cutOff;
    private String algorithm;
    private String dataIndexer;
    private String tokenizerModel;
    private String taggerModel;
    private String languageModel;

    public NLPTrainingParameters()
    {
        trainingDataLocation = ".";
        modelRootLocation = ".";
        nlpRootLocation = ".";
        trainingSystem = false;
        iterations = 200;
        cutOff = 5;
        algorithm = "NAIVEBAYES";
        dataIndexer = "TwoPass";
        tokenizerModel = "eng-token.bin";
        taggerModel = "eng-pos-maxent.bin";
        languageModel = "lang-detect.bin";
    }

    public String getTrainingDataLocation()
    {
        return trainingDataLocation;
    }

    public void setTrainingDataLocation(String trainingDataLocation)
    {
        this.trainingDataLocation = trainingDataLocation;
    }

    public String getModelRootLocation()
    {
        return modelRootLocation;
    }

    public void setModelRootLocation(String modelRootLocation)
    {
        this.modelRootLocation = modelRootLocation;
    }

    public String getNlpRootLocation()
    {
        return nlpRootLocation;
    }

    public void setNlpRootLocation(String nlpRootLocation)
    {
        this.nlpRootLocation = nlpRootLocation;
    }

    public boolean isTrainingSystem()
    {
        return trainingSystem;
    }

    public void setTrainingSystem(boolean trainingSystem)
    {
        this.trainingSystem = trainingSystem;
    }

    public int getIterations()
    {
        return iterations;
    }

    public void setIterations(int iterations)
    {
        this.iterations = iterations;
    }

    public int getCutOff()
    {
        return cutOff;
    }

    public void setCutOff(int cutOff)
    {
        this.cutOff = cutOff;
    }

    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    public String getDataIndexer()
    {
        return dataIndexer;
    }

    public void setDataIndexer(String dataIndexer)
    {
        this.dataIndexer = dataIndexer;
    }

    public String getTokenizerModel()
    {
        return tokenizerModel;
    }

    public void setTokenizerModel(String tokenizerModel)
    {
        this.tokenizerModel = tokenizerModel;
    }

    public String getTaggerModel()
    {
        return taggerModel;
    }

    public void setTaggerModel(String taggerModel)
    {
        this.taggerModel = taggerModel;
    }

    public String getLanguageModel()
    {
        return languageModel;
    }

    public void setLanguageModel(String languageModel)
    {
        this.languageModel = languageModel;
    }

    @Override
    public String toString()
    {
        return "TrainingParameters{" +
                "trainingDataLocation='" + trainingDataLocation + '\'' +
                ", modelRootLocation='" + modelRootLocation + '\'' +
                ", nlpRootLocation='" + nlpRootLocation + '\'' +
                ", trainingSystem=" + trainingSystem +
                ", iterations=" + iterations +
                ", cutOff=" + cutOff +
                ", algorithm='" + algorithm + '\'' +
                ", dataIndexer='" + dataIndexer + '\'' +
                ", tokenizerModel='" + tokenizerModel + '\'' +
                ", taggerModel='" + taggerModel + '\'' +
                ", languageModel='" + languageModel + '\'' +
                '}';
    }
}
