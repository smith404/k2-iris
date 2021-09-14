/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.persist.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "jsonClass")
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Cloneable
{
    // Enable enums to be represented as a list of BaseEntities
    public static <E extends Enum<?>> List<BaseEntity> iterateOverEnumsByClass(Class<E> c)
    {
        ArrayList<BaseEntity> al = new ArrayList<>();

        long i = 0;
        for (E object : c.getEnumConstants())
        {
            NamedEntity be = new NamedEntity();
            be.setId(++i);
            be.setName(object.name());
            be.setDescription(object.name());
            be.setShortName(object.name());
            al.add(be);
        }

        return al;
    }

    // Enable enums to be represented as a list of BaseEntities
    public static <E extends Enum<?>> List<String> enumsToStringList(Class<E> c)
    {
        ArrayList<String> al = new ArrayList<>();

        for (E object : c.getEnumConstants())
        {
            al.add(object.name());
        }

        return al;
    }

    @Transient
    protected String jsonClass;

    public String getJsonClass()
    {
        return this.getClass().getCanonicalName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private long createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    private long modifiedDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "modified_by")
    @LastModifiedBy
    private String modifiedBy;

    @Column
    private boolean active = true;

    @Transient
    protected String displayValue;

    @Transient
    protected boolean selected;

    public BaseEntity()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public long getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(long createdDate)
    {
        this.createdDate = createdDate;
    }

    public long getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate)
    {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getDisplayValue()
    {
        return displayValue;
    }

    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public String toString()
    {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public boolean equals(Object that)
    {
        if (!(that instanceof BaseEntity)) return false;


        return (this.getClass() == that.getClass() &&
                this.getId() == ((BaseEntity) that).getId());
    }

    public void beforeCreate()
    {

    }

    public void beforeUpdate(BaseEntity currentRecord)
    {

    }

    public void beforeDelete(BaseEntity currentRecord)
    {

    }

    public String parseAccessRights(String accessRights)
    {
        if (accessRights.length() != 4) return "-rwxr--r--";

        StringBuilder result = new StringBuilder();
        char c = accessRights.charAt(0);
        if (c == '1')
            result.append("S");
        else
            result.append("-");

        for(int i=1; i < 4; ++i)
        {
            c = accessRights.charAt(i);
            switch (c)
            {
                case '0':
                    result.append("---");
                    break;
                case '1':
                    result.append("--x");
                    break;
                case '2':
                    result.append("-w-");
                    break;
                case '3':
                    result.append("-wx");
                    break;
                case '4':
                    result.append("r--");
                    break;
                case '5':
                    result.append("r-x");
                    break;
                case '6':
                    result.append("rw-");
                    break;
                default:
                    result.append("rwx");
                    break;
            }
        }

        return result.toString();
    }
}
