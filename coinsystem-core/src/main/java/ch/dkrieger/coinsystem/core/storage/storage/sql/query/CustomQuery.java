package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

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

    private PreparedStatement pstatement;

    public CustomQuery(Connection connection) {
        super(connection, "");
    }

    public void execute(){
        throw new UnsupportedOperationException("Not allowed in custom query");
    }
    public void execute(String query) {
        this.query = query;
        try {
            pstatement = connection.prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                pstatement.setString(i, object.toString());
                i++;
            }
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ResultSet executeAndGetResult(String query) throws SQLException{
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for(Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        ResultSet result = pstatement.executeQuery();
        return result;
    }
    public void executeSave(String query) throws SQLException{
        this.query = query;
        PreparedStatement pstatement;
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for (Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        pstatement.executeUpdate();
        pstatement.close();
    }
    public void executeWithOutError(String query){
        try{ executeSave(query);
        }catch (Exception exception){}
    }
    public void close() throws SQLException{
        if(pstatement != null)  pstatement.close();
    }
}
