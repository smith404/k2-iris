/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.model;

import java.util.Random;

public class Chooser
{
    static private Random rnd = new Random();

    public static String chooseAtRandom(String... options)
    {
        int max = options.length-1;
        int min = 0;

        int index = rnd.nextInt(max - min + 1) + min;

        return options[index];
    }
}
