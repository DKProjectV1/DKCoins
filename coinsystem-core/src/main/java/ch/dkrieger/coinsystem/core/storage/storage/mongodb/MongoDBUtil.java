package ch.dkrieger.coinsystem.core.storage.storage.mongodb;

import ch.dkrieger.coinsystem.core.utils.GeneralUtil;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 18:51
 *
 */

public class MongoDBUtil {

    public static JsonWriterSettings MONGOJSONSETTINGS = JsonWriterSettings.builder()
            .int64Converter((value, writer) -> writer.writeNumber(value.toString())).build();

    public static <O> O toObject(Document document, Class<O> clazz){
        return GeneralUtil.GSON.fromJson(document.toJson(MONGOJSONSETTINGS),clazz);
    }
    public static Document toDocument(Object object){
        return Document.parse(GeneralUtil.GSON.toJson(object));
    }
    public static void insertOne(MongoCollection collection, Object object){
        collection.insertOne(toDocument(object));
    }
    public static void insertMany(MongoCollection collection, Object... objects){
        List<Document> docuemtns = new LinkedList<>();
        for(Object object : objects) docuemtns.add(toDocument(object));
        collection.insertMany(docuemtns);
    }
    public static void updateOne(MongoCollection collection, String identifier, Object identifierObject, String valueIdentifier, Object value) {
        MongoDBUtil.updateOne(collection, new org.bson.Document(identifier, identifierObject)
                , new org.bson.Document("$set",new Document().append(valueIdentifier,value)));
    }
    public static void updateOne(MongoCollection collection,Bson bson, Object object){
        collection.updateOne(bson,toDocument(object));
    }
    public static void updateOne(MongoCollection collection,Bson bson1, Bson bson2){
        collection.updateOne(bson1,bson2);
    }
    public static void updateMany(MongoCollection collection,Bson bson, Object object){
        collection.updateMany(bson,toDocument(object));
    }
    public static void replaceOne(MongoCollection collection,Bson bson, Object object){
        collection.replaceOne(bson,toDocument(object));
    }
    public static void deleteOne(MongoCollection collection,Bson bson){
        collection.deleteOne(bson);
    }
    public static void deleteMany(MongoCollection collection,Bson bson){
        collection.deleteOne(bson);
    }
    public static <O> List<O> findALL(MongoCollection collection,Class<O> clazz){
        FindIterable<Document> documents = collection.find();
        List<O> list = new LinkedList<>();
        if(documents != null) for(Document document : documents) list.add(toObject(document,clazz));
        return list;
    }
    public static <O> List<O> findAndSort(MongoCollection collection,Class<O> clazz, String sort, int limit){
        FindIterable<Document> documents = collection.find().sort(new Document(sort,-1)).limit(limit);
        List<O> list = new LinkedList<>();
        if(documents != null) for(Document document : documents) list.add(toObject(document,clazz));
        return list;
    }
    public static <O> List<O> find(MongoCollection collection, Bson bson, Class<O> clazz){
        FindIterable<Document> documents = collection.find(bson);
        List<O> list = new LinkedList<>();
        if(documents != null) for(Document document : documents) list.add(toObject(document,clazz));
        return list;
    }


    public static <O> O findFirst(MongoCollection collection,Bson bson,Class<O> clazz){
        Document document = (Document) collection.find(bson).first();
        if(document != null) return toObject(document,clazz);
        return null;
    }
}