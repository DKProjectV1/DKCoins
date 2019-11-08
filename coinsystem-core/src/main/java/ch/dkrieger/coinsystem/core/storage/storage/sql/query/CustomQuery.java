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

public class CustomQuery extends Query{

    public CustomQuery(SQLCoinStorage storage) {
        super(storage, "");
    }

    public void execute(){
        throw new UnsupportedOperationException("Not allowed in custom query");
    }

    public void execute(String query) {
        this.query = query;
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                statement.setString(i, object.toString());
                i++;
            }
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <R> R executeAndGetResult(String query, SelectQuery.SelectResult<R> consumer) {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                statement.setString(i, object.toString());
                i++;
            }
            ResultSet result =  statement.executeQuery();
            R object = consumer.get(result);
            result.close();
            statement.close();
            return object;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void executeSave(String query) throws SQLException{
        try (Connection connection = getConnection()){
            this.query = query;
            PreparedStatement statement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                statement.setString(i, object.toString());
                i++;
            }
            statement.executeUpdate();
            statement.close();
        }
    }
}
