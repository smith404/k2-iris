/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.nlp;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrepareTrainingData
{
    private List<String> files = new ArrayList<>();
    private String category;

    public static Options makeOptions()
    {
        Options options = new Options();

        Option append = new Option("a", "append", false, "append");
        append.setRequired(false);
        options.addOption(append);

        Option category = new Option("c", "category", true, "category");
        category.setRequired(true);
        options.addOption(category);

        Option input = new Option("i", "input", true, "input direcotry path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        return options;
    }

    public static void main(String[] args)
    {
        Options options = makeOptions();

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

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

        String inputDirectoryPath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String outputCategory = cmd.getOptionValue("category");
        boolean appendToFile = cmd.hasOption("append");

        //System.out.println("inputDirectoryPath: " + inputDirectoryPath);
        //System.out.println("outputFilePath: " + outputFilePath);
        //System.out.println("outputCategory: " + outputCategory);
        //System.out.println("appendToFile: " + appendToFile);

        PrepareTrainingData trainingData = new PrepareTrainingData(inputDirectoryPath,"txt", outputCategory);

        try
        {
            trainingData.writeData(outputFilePath, appendToFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public PrepareTrainingData(String sourceDirectory, String extension, String category)
    {
        this.category = category;

        File folder = new File(sourceDirectory);
        if (folder.isDirectory())
        {
            listFilesInFolder(folder, extension, files);
        }
    }

    public void listFilesInFolder(final File folder, String extension, List<String> files)
    {
        for (final File fileEntry : folder.listFiles())
        {
            if (fileEntry.getName().endsWith(extension))
            {
                files.add(fileEntry.getAbsolutePath());
            }
        }
    }

    public void writeData(String fileName, boolean append) throws IOException
    {
        FileWriter myWriter = new FileWriter(fileName, append);
        for (String path : files)
        {
            String contents = readContentsAsString(path);
            myWriter.write(category);
            myWriter.write("\t");
            myWriter.write(contents);
            myWriter.write(System.lineSeparator());
        }
        myWriter.close();
    }

    public String readContentsAsString(String path) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(" ");
                line = br.readLine();
            }
        } finally {
            br.close();
        }

        return sb.toString().replaceAll("( )+", " ");
    }
}
