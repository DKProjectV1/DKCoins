package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

public class Query {

    protected Connection connection;
    protected String query;
    protected boolean firstvalue;
    protected boolean and;
    protected boolean comma;
    protected List<Object> values;

    public Query(Connection connection, String query){
        this.connection = connection;
        this.query = query;
        this.firstvalue = true;
        this.comma = false;
        this.and = false;
        this.values = new LinkedList<>();
    }
    public Connection getConnection() {
        return connection;
    }
    public String toString(){
        return this.query;
    }
    public List<Object> getValues(){
        return this.values;
    }
}
