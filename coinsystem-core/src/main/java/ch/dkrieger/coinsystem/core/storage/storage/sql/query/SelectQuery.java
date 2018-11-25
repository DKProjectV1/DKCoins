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

public class SelectQuery extends  Query{

    private PreparedStatement pstatement;

    public SelectQuery(Connection connection, String query) {
        super(connection, query);
    }
    public SelectQuery where(String key, Object value) {
        if(!and){
            query += " WHERE";
            and = true;
        }else query += " AND";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }
    public SelectQuery whereWithOr(String key, Object value) {
        if(!and){
            query += " WHERE";
            and = true;
        }else query += " or";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }
    public SelectQuery noCase(){
        query += " COLLATE NOCASE";
        return this;
    }
    public ResultSet execute() throws SQLException{
        pstatement = connection.prepareStatement(query);
        int i = 1;
        for (Object object : values) {
            pstatement.setString(i, object.toString());
            i++;
        }
        ResultSet result = pstatement.executeQuery();
        return result;
    }
    public void close() throws SQLException{
        if(pstatement != null)  pstatement.close();
    }
}
