/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.core.service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface EntityService
{
    Map<Class<?>, Class<?>> primitiveWrapperMap =
            Stream.of(new Class<?>[][]{
                    {boolean.class, Boolean.class},
                    {byte.class, Byte.class},
                    {char.class, Character.class},
                    {double.class, Double.class},
                    {float.class, Float.class},
                    {int.class, Integer.class},
                    {long.class, Long.class},
                    {short.class, Short.class}}).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    static boolean isPrimitiveWrapperOf(Class<?> targetClass, Class<?> primitive)
    {
        if (!primitive.isPrimitive())
        {
            throw new IllegalArgumentException("First argument has to be primitive type");
        }
        return EntityService.primitiveWrapperMap.get(primitive) == targetClass;
    }
}