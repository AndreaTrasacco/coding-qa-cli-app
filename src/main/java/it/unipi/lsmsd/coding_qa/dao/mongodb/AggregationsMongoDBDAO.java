package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import it.unipi.lsmsd.coding_qa.dao.AggregationsDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.LevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
import javafx.util.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.*;

public class AggregationsMongoDBDAO extends BaseMongoDBDAO implements AggregationsDAO {
    @Override
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() throws DAOException {
        List<ExperienceLevelDTO> experienceLevelDTOList = new ArrayList<>();
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("users");

            Bson match = match(exists("country"));
            //{ $project : {country: 1, exp_level: { $cond: [ { $gte: ["$score", 3]}, { $cond: [{ $gte: ["$score", 20]}, "advanced", "intermediate"]}, "beginner" ] } } }
            Bson project1 = new Document("$project", new Document("country", 1)
                    .append("exp_level", new Document("$cond", Arrays.asList(new Document("$gte", Arrays.asList("$score", 3)),
                            new Document("$cond", Arrays.asList(new Document("$gte", Arrays.asList("$score", 20)), "advanced", "intermediate")), "beginner"))));

            // { $group : { _id: {country: "$country", exp_level: "$exp_level"}, numUsers : { $sum : 1 } } }
            Bson group1 = new Document("$group",
                    new Document("_id", new Document("country", "$country")
                            .append("exp_level", "$exp_level"))
                            .append("numUsers", new Document("$sum", 1)));

            // { $group: { _id: "$_id.country",total: { $sum: "$numUsers" },exp_levels: { $push: { exp_level: "$_id.exp_level", numUsers: "$numUsers" } } } }
            Bson group2 = new Document("$group", new Document("_id", "$_id.country")
                    .append("total", new Document("$sum", "$numUsers"))
                    .append("exp_levels", new Document("$push", new Document("exp_level", "$_id.exp_level")
                            .append("numUsers", "$numUsers"))));

            //{ $project: { _id: 0, country: "$_id", levels: { $map: { input: "$exp_levels", in: { $mergeObjects: [ "$$this", { percentage : { $multiply: [ { $divide: ["$$this.numUsers", "$total"]}, 100] } } ] } } } } }
            Bson project2 = new Document("$project", new Document("_id", 0)
                    .append("country", "$_id")
                    .append("levels", new Document("$map", new Document("input", "$exp_levels")
                            .append("in", new Document("$mergeObjects", Arrays.asList("$$this", new Document("percentage", new Document("$multiply",
                                    Arrays.asList(new Document("$divide", Arrays.asList("$$this.numUsers", "$total")), 100)))))))));


            Bson project3 = new Document("$project", new Document("levels.numUsers", 0));
            Bson sort = new Document("$sort", new Document("country", 1));

            collectionQuestions.aggregate(Arrays.asList(match, project1, group1, group2, project2, project3, sort)).forEach(doc -> {
                List<LevelDTO> levels = new ArrayList<>();
                for (Document level : doc.getList("levels", Document.class)) {
                    LevelDTO expLevel = new LevelDTO(level.getString("exp_level"), level.getDouble("percentage"));
                    levels.add(expLevel);
                }
                ExperienceLevelDTO temp = new ExperienceLevelDTO(doc.getString("country"), levels);
                experienceLevelDTOList.add(temp);

            });

            return experienceLevelDTOList;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    // Method that retrieves the % of users of different experience level per country
    @Override
    public List<QuestionScoreDTO> getUsefulQuestions() throws DAOException {
        List<QuestionScoreDTO> questionScoreDTOList = new ArrayList<>();
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            Bson match = match(exists("answers", true));
            Bson project1 = project(fields(include("topic", "title"), new Document("score", new Document("$sum", "$answers.score"))));
            Bson sort1 = sort(descending("score"));
            Bson group = group("$topic", first("first", "$title"), first("firstScore", "$score"));
            Bson sort2 = sort(descending("firstScore"));
            Bson project2 = project(fields(excludeId(), computed("topic", "$_id"), include("first", "firstScore")));

            collectionQuestions.aggregate(Arrays.asList(match, project1, sort1, group, sort2, project2)).forEach(doc -> {
                QuestionScoreDTO temp = new QuestionScoreDTO(doc.getString("first"), doc.getInteger("firstScore"), doc.getString("topic"));
                questionScoreDTOList.add(temp);
            });

            return questionScoreDTOList;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }

    // Method that retrieves the rank of each topic during the week
    @Override
    public List<TopicDTO> getTopicRank() throws DAOException {
        List<TopicDTO> topicDTOList = new ArrayList<>();
        try {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

            Bson match = match(and(exists("answers", true), gte("createdDate", sevenDaysAgo)));
            Bson project1 = project(fields(include("topic", "createdDate"), new Document("answerCount", new Document("$size", "$answers")))); // There are no helpers in java driver for $size operator, so I use the notation with "new Document(..)"
            Bson group = group("$topic", sum("count", "$answerCount"));
            Bson sort = sort(descending("count"));
            Bson project2 = project(fields(excludeId(), computed("topic", "$_id"), include("count")));

            collectionQuestions.aggregate(Arrays.asList(match, project1, group, sort, project2)).forEach(doc -> {
                TopicDTO temp = new TopicDTO(doc.getString("topic"), doc.getInteger("count"));
                topicDTOList.add(temp);
            });
            return topicDTOList;
        } catch (Exception ex) {
            throw new DAOException(ex);
        }
    }
}
