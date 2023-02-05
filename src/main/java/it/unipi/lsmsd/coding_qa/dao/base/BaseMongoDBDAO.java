package it.unipi.lsmsd.coding_qa.dao.base;

import com.mongodb.client.*;

public abstract class BaseMongoDBDAO {

    private final static String URI = "mongodb://localhost:27017"; // mettere macchine virtuali UNIPI
    private static final String DB_NAME = "CodingQ&A";
    private static MongoClient connection = null;
    private static MongoDatabase database = null;


    private static void createConnection(){
        MongoClient myClient = MongoClients.create(URI);
    }

    public static void closeConnection(){
        connection.close();
    } // TODO SERVE?

    public static MongoDatabase getDB(){
        if(database == null)
            createConnection();
        return database;
    }
}
