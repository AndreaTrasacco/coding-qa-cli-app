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
import it.unipi.lsmsd.coding_qa.model.Admin;
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
                    .append("score", user.getScore());
            String website = user.getWebsite();
            if(!website.equals(""))
                docUser.append("website", user.getWebsite());
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

            if(temp.getString("nickname").equals("admin")){
                Admin user = new Admin();
                user.setId(temp.getObjectId("_id").toString());
                user.setNickname(temp.getString("nickname"));
                user.setFullName(temp.getString("fullName"));
                user.setEncPassword(encPassword);
                return user;
            } else {
                RegisteredUser user = new RegisteredUser();
                user.setId(temp.getObjectId("_id").toString());
                user.setNickname(temp.getString("nickname"));
                user.setFullName(temp.getString("fullName"));
                user.setEncPassword(encPassword);
                user.setScore(temp.getInteger("score"));
                if(temp.containsKey("website"))
                    user.setWebsite(temp.getString("website"));
                user.setBirthdate(temp.getDate("createdDate"));
                user.setCreatedDate(temp.getDate("createdDate"));
                user.setCountry(temp.getString("country"));
                return user;
            }
        } catch (Exception e){
            throw new DAOException(e);
        }
    }

    @Override
    public RegisteredUser updateInfo(RegisteredUser user) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            Document doc = collectionUser.findOneAndUpdate(Filters.eq("nickname", user.getNickname()),
                    Updates.combine(Updates.set("fullName", user.getFullName()),
                            Updates.set("encPassword", user.getEncPassword()),
                            Updates.set("birthdate", user.getBirthdate()),
                            Updates.set("country", user.getCountry()),
                            Updates.set("website", user.getWebsite()))); // TODO user potrebbe non avere website (lasciare così)

            RegisteredUser oldUser = null;
            if(doc != null){
                oldUser = new RegisteredUser();
                oldUser.setFullName(doc.getString("fullName"));
                if(doc.containsKey("website"))
                    oldUser.setWebsite(doc.getString("website"));
                oldUser.setBirthdate(doc.getDate("birthdate"));
                oldUser.setCountry(doc.getString("country"));
                oldUser.setEncPassword(doc.getString("encPassword"));
                oldUser.setScore(doc.getInteger("score"));
                oldUser.setCreatedDate(doc.getDate("createdDate"));
                oldUser.setId(user.getId());
                oldUser.setNickname(doc.getString("nickname"));
            }
            return oldUser;
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

            if(userDoc.containsKey("deleted") && userDoc.getBoolean("deleted")){
                return null;
            }

            UserDTO user = new UserDTO(userDoc.getObjectId("_id").toHexString(),
                    userDoc.getString("fullName"),
                    userDoc.getString("nickname"),
                    userDoc.getDate("birthdate"),
                    userDoc.getString("country"),
                    userDoc.getDate("createdDate"),
                    userDoc.containsKey("website") ? userDoc.getString("website") : "",
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
    public void delete(String nickname) throws DAOException {
        try(MongoClient mongoClient = getConnection()) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionUser = database.getCollection("users");
            collectionUser.replaceOne(
                   Filters.eq("nickname", nickname),
                   new Document("nickname", nickname)
                           .append("deleted", true)
                   );
        } catch(Exception e){
            throw new DAOException(e);
        }
    }
}
