package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

public class Query {

    protected SQLCoinStorage storage;
    protected String query;
    protected boolean firstvalue;
    protected boolean and;
    protected boolean comma;
    protected List<Object> values;

    public Query(SQLCoinStorage storage, String query){
        this.storage = storage;
        this.query = query;
        this.firstvalue = true;
        this.comma = false;
        this.and = false;
        this.values = new LinkedList<>();
    }

    public Connection getConnection() {
        return storage.getConnection();
    }

    public String toString(){
        return this.query;
    }

    public List<Object> getValues(){
        return this.values;
    }
}
