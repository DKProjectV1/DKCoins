package ch.dkrieger.coinsystem.core.storage.storage.sql.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 20:57
 *
 */

public class QueryBuilder {

    private List<Query> querys;
    private String query;

    public QueryBuilder(Query... querys){
        this.querys = new LinkedList<>();
        this.querys.addAll(Arrays.asList(querys));
    }

    public QueryBuilder append(Query query){
        this.querys.add(query);
        return this;
    }

    public QueryBuilder remove(Query query){
        this.querys.remove(query);
        return this;
    }

    private QueryBuilder build(){
        for(Query query : this.querys){
            if(this.query == null) this.query = query.toString();
            else this.query += ";"+query.toString();
        }
        return this;
    }

    public void execute(){
        PreparedStatement pstatement;
        if(querys.size() <= 0) return;
        Connection connection = querys.get(0).getConnection();
        try {
            pstatement = connection.prepareStatement(query);
            int i = 1;
            for(Query query : this.querys){
                for(Object object : query.getValues()) {
                    pstatement.setString(i,object.toString());
                    i++;
                }
            }
            pstatement.executeUpdate();
            pstatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            for (Query query1 : querys) {
                if(query1.getConnection() != null) {
                    try {
                        query1.getConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void buildAndExecute(){
        build();
        execute();
    }
}
