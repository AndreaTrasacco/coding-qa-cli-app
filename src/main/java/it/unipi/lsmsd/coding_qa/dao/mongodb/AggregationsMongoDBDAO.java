package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import it.unipi.lsmsd.coding_qa.dao.AggregationsDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
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
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() {
        List<ExperienceLevelDTO> ExperienceLevelDTOList = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            //{ $project : {
            //
            //country: 1,
            //
            //exp_level: { $cond: [ { $gte: ["$score", 10]}, { $cond: [{ $gte: ["$score", 100]}, "advanced", "intermediate"]}, "beginner" ] } } }
            Bson project1 = project(fields(
                    include("country"),
                    computed("exp_level", new Document("$cond", Arrays.asList(
                            new Document("$gte", Arrays.asList("$score", 10)),
                            new Document("$cond", Arrays.asList(
                                    new Document("$gte", Arrays.asList("$score", 100)),
                                    "advanced",
                                    "intermediate"
                            )),
                            "beginner"
                    )))
            ));


            // { $group : { _id: {country: "$country", exp_level: "$exp_level"}, numUsers : { $sum : 1 } } },
            Bson group1 = group(
                    new Document("_id", new Document("country", "$country").append("exp_level", "$exp_level")),
                    sum("numUsers", 1)
            );

            // { $group: { _id: "$_id.country",total: { $sum: "$numUsers" },exp_levels: { $push: { exp_level: "$_id.exp_level", numUsers: "$numUsers" } } } },
            Bson group2 = group("$_id.country",
                    sum("total", "$numUsers"),
                    Accumulators.push("exp_levels",
                            new Document("exp_level", "$_id.exp_level")
                                    .append("numUsers", "$numUsers")));


            //{ $project: { _id: 0, country: "$_id", levels: { $map: { input: "$exp_levels", in: { $mergeObjects: [ "$$this", { percentage : { $multiply: [ { $divide: ["$$this.numUsers", "$total"]}, 100] } } ] } } } } }
            Bson project2 = Projections.fields(
                    Projections.computed("_id", new Document("$ifNull", Arrays.asList("$_id", 0))),
                    Projections.include("country"),
                    Projections.computed("levels",
                            new Document("$map",
                                    new Document("input", "$exp_levels")
                                            .append("as", "el")
                                            .append("in",
                                                    new Document("$mergeObjects",
                                                            Arrays.asList("$$el",
                                                                    new Document("percentage",
                                                                            new Document("$multiply",
                                                                                    Arrays.asList(
                                                                                            new Document("$divide",
                                                                                                    Arrays.asList("$$el.numUsers", "$total")
                                                                                            ),
                                                                                            100
                                                                                    )
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                            )
                    ));


            Bson project3 = Projections.exclude("levels.numUsers");
            Bson sort = sort(Sorts.ascending("country"));

            collectionQuestions.aggregate(Arrays.asList(project1, group1, group2, project2, project3, sort)).forEach(doc -> {
                ExperienceLevelDTO temp = new ExperienceLevelDTO(doc.getString("country"), doc.getList("exp_level", ExperienceLevelDTO.Level.class));
                ExperienceLevelDTOList.add(temp);
            });


            return ExperienceLevelDTOList;
        }
    }

    // Method that retrieves the % of users of different experience level per country
    @Override
    public List<QuestionScoreDTO> getUsefulQuestions() throws DAOException {
        List<QuestionScoreDTO> questionScoreDTOList = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

            Bson match = match(exists("answers", true));
            Bson project1 = project(fields(include("topic","title"),new Document("score", new Document("$sum", "$answers.score"))));
            Bson sort1 = sort(descending("score"));
            Bson group = group("$topic", first("first", "$title"), first("firstScore", "$score"));
            Bson sort2 = sort(descending("firstScore"));
            Bson project2 = project(fields(excludeId(), computed("topic", "_id"), include("first", "firstScore")));

            collectionQuestions.aggregate(Arrays.asList(match, project1, sort1, group, sort2, project2)).forEach(doc -> {
                QuestionScoreDTO temp = new QuestionScoreDTO(doc.getString("title"), doc.getInteger("firstScore"), doc.getString("topic"));
                questionScoreDTOList.add(temp);
            });

            return questionScoreDTOList;
        } catch (Exception ex){
            throw new DAOException(ex);
        }
    }

    // Method that retrieves the rank of each topic during the week
    @Override
    public List<TopicDTO> getTopicRank() throws DAOException {
        List<TopicDTO> topicDTOList = new ArrayList<>();
        try (MongoClient mongoClient = getConnection()) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            LocalDateTime currentDate = LocalDateTime.now();

            Bson match = match(and(exists("answers", true),gte("createdDate", sevenDaysAgo), lt("createdDate", currentDate)));
            Bson project1 = project(fields(include("topic", "createdDate"), new Document("answerCount", new Document("$size", "$answers")))); // There are no helpers in java driver for $size operator so i use the notation with "new Document(..)"
            Bson group = group("$topic", sum("count", "$answerCount"));
            Bson sort = sort(descending("count"));
            Bson project2 = project(fields(excludeId(), computed("_id", "topic")));

            collectionQuestions.aggregate(Arrays.asList(match, project1, group, sort, project2)).forEach(doc -> {
                TopicDTO temp = new TopicDTO(doc.getString("topic"), doc.getInteger("count"));
                topicDTOList.add(temp);
            });
            return topicDTOList;
        } catch (Exception ex){
            throw new DAOException(ex);
        }
    }
}
