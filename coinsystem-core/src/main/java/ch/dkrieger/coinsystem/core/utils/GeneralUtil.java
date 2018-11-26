package ch.dkrieger.coinsystem.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.util.Random;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 24.11.18 16:16
 *
 */

public class GeneralUtil {

    public static final Random RANDOM = new Random();
    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().setPrettyPrinting();
    public static Gson GSON = GSON_BUILDER.create();
    public static Gson GSON_NOT_PRETTY = new Gson();
    public static final JsonParser PARSER = new JsonParser();

    public static void createGSON(){
        GSON = GSON_BUILDER.create();
    }
    public static String getRandomString(final int size){
        char data = ' ';
        String dat = "";
        for(int i=0;i<=size;i++) {
            data = (char)(RANDOM.nextInt(25)+97);
            dat = data+dat;
        }
        return dat;
    }
    public static boolean isNumber(String value){
        try{
            Long.parseLong(value);
            return true;
        }catch(NumberFormatException exception){
            return false;
        }
    }
}
