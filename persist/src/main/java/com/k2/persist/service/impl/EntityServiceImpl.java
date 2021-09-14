/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.persist.service.impl;

import com.k2.core.component.BaseComponent;
import com.k2.core.service.EntityService;
import com.k2.core.service.UtilsService;
import com.k2.persist.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Slf4j
@Service("EntityService")
public class EntityServiceImpl extends BaseComponent implements EntityService
{
    // Inject some global properties via application.properties
    @Value("${application.refreshEntityData:false}")
    protected String refreshEntityData;

    private static final HashMap<String, Class<?>> theClasses = new LinkedHashMap<>();

    @PersistenceContext
    private EntityManager entityManager;

    public HashMap<String, Class<?>> getTheClasses()
    {
        return theClasses;
    }

    @PostConstruct
    public void init()
    {
    }

    private static boolean isAssignableTo(Class<?> from, Class<?> to)
    {
        if (to.isAssignableFrom(from))
        {
            return true;
        }
        if (from.isPrimitive())
        {
            return EntityService.isPrimitiveWrapperOf(to, from);
        }
        if (to.isPrimitive())
        {
            return EntityService.isPrimitiveWrapperOf(from, to);
        }

        return false;
    }

    public String getEntityName(String rawClassName)
    {
        if (rawClassName.contains("."))
            return rawClassName.substring(rawClassName.lastIndexOf('.') + 1);
        else
            return rawClassName;
    }

    public Class<?> getClassFromName(String rawClassName)
    {
        Class<?> clazz = null;
/*
        try
        {
            clazz = PlatformClassLoader.getInstance().loadClass(rawClassName);
        }
        catch (ClassNotFoundException ex)
        {
            // No class found so
        }
*/
        return clazz;
    }

    private final Map<String, Class<?>> classCache = new HashMap<>();

    @Transactional
    public List<?> executeJQPL(String theQuery, Map<String, Object> params)
    {
        Query q = entityManager.createQuery(theQuery);

        for (String key : params.keySet())
        {
            q.setParameter(key, params.get(key));
        }

        return q.getResultList();
    }

    @Transactional
    public List<?> executeJQPL(String theQuery)
    {
        return entityManager.createQuery(theQuery).getResultList();
    }

    public BaseEntity findById(Class<?> clazz, Long id)
    {
        return (BaseEntity) entityManager.find(clazz, id);
    }

    public List<BaseEntity> findAll(String entity, String orderBy)
    {
        String jqpl = "SELECT inst FROM " + entity + " inst order by " + orderBy;

        return (List<BaseEntity>) entityManager.createQuery(jqpl).getResultList();
    }

    public BaseEntity findInstanceWithNameValuePair(String theEntity, String theField, Object theValue)
    {
        theEntity = getEntityName(theEntity);
        String jqpl = "SELECT inst FROM " + theEntity + " inst WHERE " + theField + " = ?1";

        List<?> results = entityManager.createQuery(jqpl).setParameter(1, theValue).getResultList();

        if (results != null && results.size() > 0)
            return (BaseEntity) results.get(0);
        else
            return null;
    }

    public List<BaseEntity> findInstancesWithNameValuePair(String theEntity, String theField, Object theValue, String orderBy)
    {
        theEntity = getEntityName(theEntity);
        String jqpl = "SELECT inst FROM " + theEntity + " inst WHERE " + theField + " = ?1 order by " + orderBy;

        return (List<BaseEntity>) entityManager.createQuery(jqpl).setParameter(1, theValue).getResultList();
    }

    public List<BaseEntity> findInstancesContainingNameValuePair(String theEntity, String theField, Object theValue, String orderBy)
    {
        theEntity = getEntityName(theEntity);
        String jqpl = "SELECT inst FROM " + theEntity + " inst WHERE lower(" + theField + ") like lower(concat('%', ?1,'%')) order by " + orderBy;

        return (List<BaseEntity>) entityManager.createQuery(jqpl).setParameter(1, theValue).getResultList();
    }

    public List<BaseEntity> findInstancesWithQuery(String theQuery)
    {
        return (List<BaseEntity>) entityManager.createQuery(theQuery).getResultList();
    }

    public List<BaseEntity> findInstancesWithName(String theEntity, String orderBy)
    {
        theEntity = getEntityName(theEntity);
        String jqpl = "SELECT inst FROM " + theEntity + " inst order by " + orderBy;

        return (List<BaseEntity>) entityManager.createQuery(jqpl).getResultList();
    }

    public List<Field> getAllFields(List<Field> fields, Class<?> type)
    {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null)
        {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }

    public Class<?> getTypeOfAttribute(String rawClassName, String attributeName)
    {
        try
        {
            Class<?> clazz = getClassFromName(rawClassName);
            Field f = clazz.getDeclaredField(attributeName);
            return f.getType();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public Class<?> getParameterizedTypeAttribute(String rawClassName, String attributeName)
    {
        try
        {
            Class<?> clazz = getClassFromName(rawClassName);
            Field f = clazz.getDeclaredField(attributeName);
            ParameterizedType listType = (ParameterizedType) f.getGenericType();
            return (Class<?>) listType.getActualTypeArguments()[0];
        }
        catch(Exception ex)
        {
            log.warn(UtilsService.makeExceptionWarning(ex));
        }

        return null;
    }

}