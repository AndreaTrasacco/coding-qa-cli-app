package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

public class UserMongoDBDAO extends BaseMongoDBDAO implements UserDAO {

    public RegisteredUser register(RegisteredUser user){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("users");
        Document docUser = new Document("nickname", user.getNickname())
                .append("fullName", user.getFullName())
                .append("encPassword", user.getEncPassword())
                .append("birthdate", user.getBirthdate())
                .append("country", user.getCountry())
                .append("createdDate", user.getCreatedDate())
                .append("website", user.getWebsite())
                .append("score", user.getScore());
        collectionUser.insertOne(docUser);
        // TODO SETTARE id UTENTE
        return user;
    }

    public User authenticate(String username, String encPassword){ // TODO
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("users");
        Document user = collectionUser.find(Filters.and(Filters.eq("nickname", username),
                        Filters.eq("encPassword", encPassword))).first();
        if (user == null) {
            return false;
        }

        return true;
    }

    public void updateInfo(RegisteredUser user){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("users");
        collectionUser.updateOne(Filters.eq("nickname", user.getNickname()),
                Updates.combine(Updates.set("fullName", user.getFullName()),
                        Updates.set("encPassword", user.getEncPassword()),
                        Updates.set("birthdate", user.getBirthdate()),
                        Updates.set("country", user.getCountry()),
                        Updates.set("createdDate", user.getCreatedDate()),
                        Updates.set("website", user.getWebsite()),
                        Updates.set("score", user.getScore())));

    }

    public UserDTO getInfo(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("users");
        Document userDoc = collectionUser.find(Filters.eq("_id", new ObjectId(id))).first();

        UserDTO user = new UserDTO(userDoc.getObjectId("_id").toHexString(),
                userDoc.getString("fullName"),
                userDoc.getString("nickname"),
                userDoc.getDate("birthdate"),
                userDoc.getString("country"),
                userDoc.getDate("createdDate"),
                userDoc.getString("website"),
                userDoc.getInteger("score"));

        return user;
    }

    public void delete(String id){
        MongoDatabase database = getDB();
        MongoCollection<Document> collectionUser = database.getCollection("users");
        collectionUser.deleteOne(Filters.eq("_id", new ObjectId(id)));
    }

}
