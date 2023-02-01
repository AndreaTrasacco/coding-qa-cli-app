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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.first;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class AggregationsMongoDBDAO extends BaseMongoDBDAO implements AggregationsDAO {
    @Override
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() {
        //TODO prima di scrivere le query ricontrollare le aggregazioni

        return null;
    }

    @Override
    public List<QuestionScoreDTO> getUsefulQuestions() {
        List<QuestionScoreDTO> questionScoreDTOList = new ArrayList<>();
        MongoDatabase mongoDatabase = getDB();
        MongoCollection<Document> collectionQuestions = mongoDatabase.getCollection("questions");

        Bson match = match(exists("answers", true));
        Bson project1 = project(fields(include("title", "topic"), computed("score", sum("$answers.score", 1)))); //TODO capire cosa va messo come secondo argomento
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

        return null;
    }

    //methods that retrieve the % of users of different experience level per country
}
