package it.unipi.lsmsd.coding_qa.dao.base;

import com.mongodb.client.*;

public abstract class BaseMongoDBDAO {

    private final static String URI = "mongodb://localhost:27017"; // mettere macchine virtuali UNIPI
    protected static final String DB_NAME = "CodingQ&A";
    private static MongoClient connection = null;


    private static void createConnection(){
        connection = MongoClients.create(URI);
    }

    public static void closeConnection(){
        connection.close();
    }

    public static MongoClient getConnection(){
        if(connection == null)
            createConnection();
        return connection;
    }
}
