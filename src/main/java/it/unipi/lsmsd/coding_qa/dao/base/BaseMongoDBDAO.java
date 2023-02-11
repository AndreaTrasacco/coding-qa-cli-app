package it.unipi.lsmsd.coding_qa.dao.base;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;

public abstract class BaseMongoDBDAO {

    private final static String URI = "mongodb://10.1.1.15:27020,10.1.1.16:27020,10.1.1.17:27020/?retryWrites=true&w=majority&wtimeout=10000";
    protected static final String DB_NAME = "codingqa";

    public static MongoClient getConnection(){
        return MongoClients.create(URI);
    }

    public static TransactionOptions txnOptions = TransactionOptions.builder()
            .readPreference(ReadPreference.primary())
            .readConcern(ReadConcern.LOCAL)
            .writeConcern(WriteConcern.MAJORITY)
            .build();
}
