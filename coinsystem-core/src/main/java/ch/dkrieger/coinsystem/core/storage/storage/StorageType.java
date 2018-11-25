package ch.dkrieger.coinsystem.core.storage.storage;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 19:24
 *
 */

public enum StorageType {

    JSON(),
    SQLITE(),
    MYSQL(),
    MONGODB();

    public static StorageType parse(String name){
        try{
            return valueOf(name.toUpperCase());
        }catch (Exception exception){}
        return JSON;
    }
}
