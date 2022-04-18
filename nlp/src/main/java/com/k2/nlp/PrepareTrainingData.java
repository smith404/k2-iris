/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp;

import com.k2.core.exception.BaseException;
import com.k2.nlp.model.NLPTrainingParameters;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import org.apache.commons.cli.*;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrepareTrainingData
{
    private final List<String> files = new ArrayList<>();
    private final String category;
    private NLPTrainingParameters tp = null;

    public static Options makeOptions()
    {
        Options options = new Options();

        Option category = new Option("c", "category", true, "category");
        category.setRequired(true);
        options.addOption(category);

        Option output = new Option("t", "training-data", true, "training data file path");
        output.setRequired(true);
        options.addOption(output);

        Option directory = new Option("d", "input", true, "input directory path");
        directory.setRequired(false);
        options.addOption(directory);

        Option language = new Option("l", "lang", true, "language of training data (default eng)");
        language.setRequired(false);
        options.addOption(language);

        Option extension = new Option("e", "extension", true, "extension of files to load (default txt)");
        extension.setRequired(false);
        options.addOption(extension);

        Option generate = new Option("g", "generate", false, "generate a category model (default no)");
        generate.setRequired(false);
        options.addOption(generate);

        Option append = new Option("a", "append", false, "append to supplied output file (default no)");
        append.setRequired(false);
        options.addOption(append);

        return options;
    }

    public static void main(String[] args)
    {
        Options options = makeOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try
        {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }

        String outputCategory = cmd.getOptionValue("category");
        String trainingDataFilePath = cmd.getOptionValue("training-data");
        String inputDirectoryPath = cmd.getOptionValue("input");
        if (inputDirectoryPath == null)
        {
            inputDirectoryPath = "";
        }
        String language = cmd.getOptionValue("language");
        if (language == null)
        {
            language = "eng";
        }
        String extension = cmd.getOptionValue("extension");
        if (extension == null)
        {
            extension = ".txt";
        }
        if (!extension.startsWith("."))
        {
            extension = "." + extension;
        }
        boolean appendToFile = cmd.hasOption("append");
        boolean generateModel = cmd.hasOption("generate");

        PrepareTrainingData trainingData = new PrepareTrainingData(inputDirectoryPath, extension, outputCategory);

        try
        {
            trainingData.writeData(trainingDataFilePath, appendToFile);

            if (generateModel)
            {
                trainingData.createCategoryModel(readContentsAsString(trainingDataFilePath, false), outputCategory, language);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public PrepareTrainingData(String sourceDirectory, String extension, String category)
    {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("training.yaml");


        tp = yaml.load(inputStream);

        this.category = category;

        if (sourceDirectory.length() > 0)
        {
            File folder = new File(sourceDirectory);
            if (folder.isDirectory())
            {
                listFilesInFolder(folder, extension, files);
            }
        }
    }

    public void listFilesInFolder(final File folder, String extension, List<String> files)
    {
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles()))
        {
            if (fileEntry.getName().endsWith(extension))
            {
                files.add(fileEntry.getAbsolutePath());
            }
        }
    }

    public void writeData(String fileName, boolean append) throws IOException
    {
        if (files.size() == 0)
        {
            return;
        }

        FileWriter myWriter = new FileWriter(fileName, append);

        for (String path : files)
        {
            String contents = readContentsAsString(path, true);
            myWriter.write(category);
            myWriter.write("\t");
            myWriter.write(contents);
            myWriter.write(System.lineSeparator());
        }
        myWriter.close();
    }

    public void createCategoryModel(String data, String category, String lang)
    {
        try
        {
            if (data != null)
            {
                Path location = Paths.get(tp.getModelRootLocation());
                File modelFile = new File(location + "/" + lang + "-" + category + ".bin");

                File tempFile = File.createTempFile("model", ".tmp");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                writer.write(data);

                // Read, process and store the training data
                InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(tempFile);
                ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
                ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

                TrainingParameters params = new TrainingParameters();
                params.put(TrainingParameters.ITERATIONS_PARAM, tp.getIterations());
                params.put(TrainingParameters.CUTOFF_PARAM, tp.getCutOff());
                params.put("DataIndexer", tp.getDataIndexer());
                params.put(TrainingParameters.ALGORITHM_PARAM, tp.getAlgorithm());

                DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
                BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
                model.serialize(modelOut);
                modelOut.close();

                tempFile.deleteOnExit();
            }
        }
        catch (Exception ex)
        {
            throw new BaseException(ex.getMessage(), ex);
        }
    }

    public static String readContentsAsString(String path, boolean concat) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                if (concat)
                {
                    sb.append(" ");
                }
                else
                {
                    sb.append(System.lineSeparator());
                }
                line = br.readLine();
            }
        } finally {
            br.close();
        }

        return sb.toString().replaceAll("( )+", " ");
    }
}
