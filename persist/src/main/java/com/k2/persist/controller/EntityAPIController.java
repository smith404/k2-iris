/*
 * Copyright (c) 2021. K2-Software
 * All software, both binary and source published by K2-Software (hereafter, Software) is copyrighted by the author (hereafter, K2-Software) and ownership of all right, title and interest in and to the Software remains with K2-Software. By using or copying the Software, User agrees to abide by the terms of this Agreement.
 */

package com.k2.persist.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.k2.core.PlatformClassLoader;
import com.k2.core.controller.BaseController;
import com.k2.core.exception.BaseException;
import com.k2.persist.model.BaseEntity;
import com.k2.core.model.CommandResult;
import com.k2.persist.model.TextResponse;
import com.k2.persist.service.UtilsService;
import com.k2.core.util.SimpleCipher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/eapi")
public class EntityAPIController extends BaseController
{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init()
    {
        FilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("HideRestrictedFilter", SimpleBeanPropertyFilter.filterOutAllExcept("password"))
                .addFilter("HidePersonalFilter", SimpleBeanPropertyFilter.filterOutAllExcept("firstName"));
    }

    @GetMapping("/currentUser")
    public Object getCurrentUser(Principal principal)
    {
        return principal;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/encrypt")
    public ResponseEntity<?> encrypt(@RequestParam(value = "secret") String secret,
                                     @RequestParam(value = "algo") String algo,
                                     @RequestParam(value = "keystr", required = false, defaultValue = "1234567887654321") String keystr,
                                     @RequestParam(value = "enc", required = false, defaultValue = "enc") String enc, Model model)
    {
        TextResponse tr = new TextResponse();

        try
        {
            if (algo.equalsIgnoreCase("InternalPassword"))
            {
                tr.setResult(passwordEncoder.encode(secret));
            }
            else if (algo.equalsIgnoreCase("InternalAlgorithm"))
            {
                if (enc.equalsIgnoreCase("enc"))
                    tr.setResult(SimpleCipher.encrypt(secret));
                else if (enc.equalsIgnoreCase("dec"))
                    tr.setResult(SimpleCipher.decrypt(secret));
            }
            else
            {
                try
                {
                    Cipher cipher = Cipher.getInstance(algo);
                    byte[] plainText = secret.getBytes(StandardCharsets.UTF_8);
                    byte[] cipherText;

                    String[] tokens = algo.split("/");
                    SecretKey secretKey = new SecretKeySpec(keystr.getBytes(), tokens[0]);

                    if (enc.equalsIgnoreCase("enc"))
                    {
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                        cipherText = cipher.doFinal(plainText);
                        tr.setResult(Arrays.toString(cipherText));
                    }
                    else if (enc.equalsIgnoreCase("dec"))
                    {
                        cipher.init(Cipher.DECRYPT_MODE, secretKey);
                        cipherText = cipher.doFinal(plainText);
                        tr.setResult(Arrays.toString(cipherText));
                    }
                }
                catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex)
                {
                    // Do nothing
                    log.warn(UtilsService.makeExceptionWarning(ex), "CRYPT");
                }
            }
        }
        catch (Exception ex)
        {
            // Do nothing
            log.debug("CRYPT ** Exception {} : {} ", ex.getClass().getName(), ex.getMessage());
        }

        return new ResponseEntity<>(tr, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/hash")
    public ResponseEntity<?> hashString(@RequestParam(value = "algo") String algo,
                                        @RequestParam(value = "from") String from, Model model)
    {
        TextResponse tr = new TextResponse();

        try
        {
            tr.setResult(SimpleCipher.hash(from, algo));
        }
        catch (Exception ex)
        {
            // Do nothing
            log.debug("IRIS ** Exception {} : {} ", ex.getClass().getName(), ex.getMessage());
        }

        return new ResponseEntity<>(tr, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/enum")
    public ResponseEntity<?> listEnum(@RequestParam(value = "clazz") String clazz, Model model)
    {
        List<String> dataSourcesTypes = new ArrayList<>();

        try
        {
            Class<?> enumClass = PlatformClassLoader.getInstance().loadClass(clazz);
            //Class<?> enumClass = Class.forName(clazz);

            if (enumClass.isEnum()) dataSourcesTypes = BaseEntity.enumsToStringList((Class<Enum>) enumClass);
        }
        catch (ClassNotFoundException ex)
        {
            // Do nothing either class wasn't found or isn't an enum
            log.debug("IRIS ** Exception {} : {} ", ex.getClass().getName(), ex.getMessage());
        }

        return new ResponseEntity<>(dataSourcesTypes, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/execservice/{service}/{action}")
    public ResponseEntity<?> callService(@PathVariable("service") String service,
                                         @PathVariable("action") String action,
                                         @RequestBody BaseEntity entity, Model model)
    {
        CommandResult retVal = new CommandResult();

        try
        {
            Object serviceObject = context.getBean(service);

            Method serviceMethod = serviceObject.getClass().getDeclaredMethod(action, entity.getClass());
            serviceMethod.setAccessible(true);
            retVal.setResultOutput(serviceMethod.invoke(serviceObject, entity).toString());
        }
        catch (BaseException ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw new BaseException(ex.getMessage(), ex);
        }

        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}

