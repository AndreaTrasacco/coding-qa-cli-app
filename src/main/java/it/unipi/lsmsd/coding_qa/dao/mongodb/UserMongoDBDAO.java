package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import org.bson.Document;

public class UserMongoDBDAO extends BaseMongoDBDAO implements UserDAO {

    public RegisteredUser register(RegisteredUser user){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("User");
        Document docUser = new Document("_id", user.getId())
                .append("nickname", user.getNickname())
                .append("fullName", user.getFullName())
                .append("encPassword", user.getEncPassword())
                .append("birthdate", user.getBirthdate())
                .append("country", user.getCountry())
                .append("createdDate", user.getCreatedDate())
                .append("website", user.getWebsite())
                .append("score", user.getScore());
        collectionUser.insertOne(docUser);

        return user;
    }

    public boolean authenticate(String username, String password){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("User");
        Document user = collectionUser.find(Filters.and(Filters.eq("nickname", username),
                        Filters.eq("encPassword", password))).first();
        if (user == null) {
            return false;
        }

        return true;
    }

    public RegisteredUser updateInfo(RegisteredUser user){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("User");
        collectionUser.updateOne(Filters.eq("nickname", user.getNickname()),
                Updates.combine(Updates.set("fullName", user.getFullName()),
                        Updates.set("encPassword", user.getEncPassword()),
                        Updates.set("birthdate", user.getBirthdate()),
                        Updates.set("country", user.getCountry()),
                        Updates.set("createdDate", user.getCreatedDate()),
                        Updates.set("website", user.getWebsite()),
                        Updates.set("score", user.getScore())));
        return user;
    }

    public RegisteredUser getInfo(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("User");
        Document userDoc = collectionUser.find(Filters.eq("_id", id)).first();

        RegisteredUser user = new RegisteredUser(userDoc.getObjectId("_id").toHexString(),
                userDoc.getString("fullName"),
                userDoc.getString("nickname"),
                userDoc.getString("encPassword"),
                userDoc.getDate("birthdate"),
                userDoc.getString("country"),
                userDoc.getDate("createdDate"),
                userDoc.getString("website"),
                userDoc.getInteger("score"));

        return user;
    }

    public void delete(RegisteredUser user){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("User");
        collectionUser.deleteOne(Filters.eq("_id", user.getId()));
    }
}
