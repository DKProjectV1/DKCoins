package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

import ch.dkrieger.coinsystem.core.storage.storage.sql.SQLCoinStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

public class InsertQuery extends Query{

    public InsertQuery(SQLCoinStorage storage, String query) {
        super(storage, query);
    }

    public InsertQuery insert(String insert) {
        query += "`"+insert+"`,";
        return this;
    }

    public InsertQuery value(Object value) {
        query = query.substring(0, query.length() - 1);
        if(firstvalue){
            query += ") VALUES (?)";
            firstvalue = false;
        }else query += ",?)";
        values.add(value);
        return this;
    }

    public void execute(){
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                statement.setString(i, object.toString());
                i++;
            }
            statement.executeUpdate();
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object executeAndGetKey(){
        try(Connection connection = getConnection()) {
            PreparedStatement pstatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for(Object object : values) {
                pstatement.setString(i, object.toString());
                i++;
            }
            pstatement.executeUpdate();
            ResultSet result = pstatement.getGeneratedKeys();
            if(result != null){
                if(result.next()) return result.getObject(1);
                result.close();
            }
            pstatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeAndGetKeyInInt(){
        try(Connection connection = getConnection()) {
            PreparedStatement pstatement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for(Object object : values) {
                pstatement.setString(i, object.toString());
                i++;
            }
            pstatement.executeUpdate();
            ResultSet result = pstatement.getGeneratedKeys();
            if(result != null && result.next()) return result.getInt(1);
            if(result != null) result.close();
            pstatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
