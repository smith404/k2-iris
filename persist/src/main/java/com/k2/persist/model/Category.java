/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.persist.model;

public class Category
{
    private String fullName = "";

    private String type = "";

    private String lang = "";

    private String name = "";

    public Category()
    {
    }

    public Category(String fullName, String type)
    {
        this.fullName = fullName;
        this.type = type;
        String[] parts = fullName.split("-");
        if (parts.length == 2)
        {
            lang = parts[0];
            name = parts[1];

        }
        else
        {
            lang = "eng";
            name = fullName;
        }

        // Remove the extension if we can find a recognized one
        if (name.endsWith(".bin")) name = name.substring(0, name.length()-4);
        else if (name.endsWith(".dict")) name = name.substring(0, name.length()-5);
        else if (name.endsWith(".regex")) name = name.substring(0, name.length()-6);
        else if (name.endsWith(".rxcat")) name = name.substring(0, name.length()-6);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getFullName()
    {
        return fullName;
    }

    public String getFriendlyName()
    {
        return lang + "-" + name;
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "fullName='" + fullName + '\'' +
                ", type='" + type + '\'' +
                ", lang='" + lang + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Category category = (Category) o;

        if (!type.equals(category.type)) return false;
        if (!lang.equals(category.lang)) return false;
        return name.equals(category.name);
    }

    @Override
    public int hashCode()
    {
        int result = type.hashCode();
        result = 31 * result + lang.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
