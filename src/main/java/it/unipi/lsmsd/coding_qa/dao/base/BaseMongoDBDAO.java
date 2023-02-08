package it.unipi.lsmsd.coding_qa.dao.base;

import com.mongodb.client.*;

public abstract class BaseMongoDBDAO {

    private final static String URI = "mongodb://localhost:27017"; // mettere macchine virtuali UNIPI
    protected static final String DB_NAME = "mongo_practice";

    public static MongoClient getConnection(){
        return MongoClients.create(URI);
    }
}
