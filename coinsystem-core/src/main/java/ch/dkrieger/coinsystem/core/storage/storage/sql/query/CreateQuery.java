package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

public class CreateQuery extends Query {

    public CreateQuery(Connection connection, String query){
        super(connection, query);
        firstvalue = true;
    }
    public CreateQuery create(String value) {
        if(!firstvalue) query = query.substring(0,query.length()-1)+",";
        else firstvalue = false;
        query += value+")";
        return this;
    }
    public void execute(){
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.execute(query);
            statement.close();
        } catch(SQLException exception){
            exception.printStackTrace();
        }
    }
}
