package it.unipi.lsmsd.coding_qa.dao.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import it.unipi.lsmsd.coding_qa.dao.AggregationsDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class AggregationsMongoDBDAO extends BaseMongoDBDAO implements AggregationsDAO {
    @Override
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() {
        List<ExperienceLevelDTO> ExperienceLevelDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        Bson project1 = project(include("country")); //TODO capire come fare prima project
        Bson group1 = group(new Document("country", 1).append("exp_level", 1), sum("numUser", 1));
        Bson group2 = group(); //TODO capire come fare

        //TODO finire query
        return null;
    }

    //methods that retrieve the % of users of different experience level per country
    @Override
    public List<QuestionScoreDTO> getUsefulQuestions() {
        List<QuestionScoreDTO> questionScoreDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        Bson match = match(exists("answers", true));
        Bson project1 = project(new Document("topic", 1).append("title", 1).append("score", new Document("$sum", "$answers.score"))); //TODO capire cosa va messo come secondo argomento
        Bson sort1 = sort(descending("score"));
        Bson group = group("$topic", first("first", "$title"), first("firstScore", "$score"));
        Bson sort2 = sort(descending("firstScore"));
        Bson project2 = project(fields(excludeId(), computed("topic", "$_id"),include("title", "firstScore")));  //TODO non so se sono giusti gli argomenti del computed

        collectionQuestions.aggregate(Arrays.asList(match, project1, sort1, group, sort2, project2)).forEach(doc -> {
            QuestionScoreDTO temp = new QuestionScoreDTO(doc.getString("title"), doc.getInteger("firstScore"), doc.getString("topic"));
            questionScoreDTOList.add(temp);
        });

        return questionScoreDTOList;
    }

    @Override
    public List<TopicDTO> getTopicRank() {
        List<TopicDTO> topicDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");
        Date sevenDaysAgo = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);
        Date currentDate = new Date(System.currentTimeMillis());

        Bson match1 = match(exists("answers", true));
        Bson project1 = project(new Document("topic", 1).append("createdDate", 1)
                .append("answerCount", new Document("$size", "$answers")));
        Bson match2 = match(and(gte("createdDate", sevenDaysAgo), lt("createdDate", currentDate)));
        Bson group = group("$topic", sum("count", "$answerCount"));
        Bson sort = sort(descending("count"));
        Bson project2 = project(new Document("_id", 1).append("count", 1));

        collectionQuestions.aggregate(Arrays.asList(match1, project1, match2, group, sort, project2)).forEach(doc -> {
            TopicDTO temp = new TopicDTO(doc.getString("_id"), doc.getInteger("count"));
            topicDTOList.add(temp);
        });
        return topicDTOList;
    }
}
