package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import it.unipi.lsmsd.coding_qa.dao.UserDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.UserDTO;
import it.unipi.lsmsd.coding_qa.model.RegisteredUser;
import it.unipi.lsmsd.coding_qa.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static com.mongodb.client.model.Projections.include;

public class UserMongoDBDAO extends BaseMongoDBDAO implements UserDAO {

    @Override
    public void register(RegisteredUser user) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            Document docUser = new Document("nickname", user.getNickname())
                    .append("fullName", user.getFullName())
                    .append("encPassword", user.getEncPassword())
                    .append("birthdate", user.getBirthdate())
                    .append("country", user.getCountry())
                    .append("createdDate", user.getCreatedDate())
                    .append("website", user.getWebsite())
                    .append("score", user.getScore());
            InsertOneResult result = collectionUser.insertOne(docUser);
            user.setId(docUser.getObjectId("_id").toString());
        } catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public User authenticate(String username, String encPassword) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            Document temp = collectionUser.find(Filters.and(Filters.eq("nickname", username),
                    Filters.eq("encPassword", encPassword))).first();
            if (temp == null) {
                return null;
            }

            User user = new RegisteredUser();
            user.setId(temp.getObjectId("_id").toString());
            user.setNickname(temp.getString("nickname"));
            user.setFullName(temp.getString("fullName"));
            user.setEncPassword(encPassword);
            return user;
        } catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public void updateInfo(RegisteredUser user) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            collectionUser.updateOne(Filters.eq("nickname", user.getNickname()),
                    Updates.combine(Updates.set("fullName", user.getFullName()),
                            Updates.set("encPassword", user.getEncPassword()),
                            Updates.set("birthdate", user.getBirthdate()),
                            Updates.set("country", user.getCountry()),
                            Updates.set("website", user.getWebsite())));
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public UserDTO getInfo(String id) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            Document userDoc = collectionUser.find(Filters.eq("_id", new ObjectId(id))).first();
            if (userDoc == null) {
                return null;
            }

            UserDTO user = new UserDTO(userDoc.getObjectId("_id").toHexString(),
                    userDoc.getString("fullName"),
                    userDoc.getString("nickname"),
                    userDoc.getDate("birthdate"),
                    userDoc.getString("country"),
                    userDoc.getDate("createdDate"),
                    userDoc.getString("website"),
                    userDoc.getInteger("score"));

            return user;
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public int getScore(String id) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            Document userDoc = collectionUser.find(Filters.eq("_id", new ObjectId(id))).projection(include("score")).first();

            return userDoc.getInteger("score");
        } catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public void updateScore(String nickname, int quantity) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");

            collectionUser.updateOne(Filters.eq("nickname", nickname),
                    Updates.inc("score", quantity));
        } catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(String id) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            collectionUser.deleteOne(Filters.eq("_id", new ObjectId(id)));
        } catch(Exception e){
            throw new DAOException(e);
        }
    }

    /* TODO funzione per convertire la stringa in formato criptato, mettere dentro il controller
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
*/

}
