package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.SuggestionsDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.summary.ResultSummary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class SuggestionsNeo4JDAO extends BaseNeo4JDAO implements SuggestionsDAO { // TODO AGGIUNGERE RECUPERO AUTHOR E USARE QUESTIONDTO
    // method for suggesting questions the user might be interested in
    public PageDTO<Question> questionsToRead(String nickname){
        String suggestionQuery = "MATCH (startUserQuestion:Question) <- [:CREATED]-(startUser:User{ nickname : $nickname})" +
                "WHERE startUserQuestion.closed = false" +
                "WITH DISTINCT(startUserQuestion.topic) AS topics, startUser" +
                "MATCH p = (startUser)-[*1..2]->(followed:User)-[:CREATED]->(followedQuestion:Question)" +
                "WHERE followedQuestion.closed = true AND followedQuestion.topic IN topics" +
                "RETURN DISTINCT followedQuestion, length(p) as depth" +
                "ORDER BY depth DESC" +
                "LIMIT 100 ";

        return retrieveQuestions(suggestionQuery, nickname);
    }

    // method for suggesting questions that the user might be able to answer
    public PageDTO<Question> questionsToAnswer(String nickname){
        String suggestionQuery = "MATCH (u1:User{nickname : $nickname})-[:ANSWERED]->(q1)<-[:CREATED]-(:User)-[:CREATED]->(q2:Question{closed: false})<-[a:ANSWERED]-()" +
                "RETURN q2, COUNT(a) AS ans_count" +
                "ORDER BY ans_count" +
                "LIMIT 10 ";

        return retrieveQuestions(suggestionQuery, nickname);
    }

    private PageDTO<Question> retrieveQuestions(String suggestionQuery, String nickname){
        try(Session session = getSession()){
            List<Question> suggestedQuestions = session.readTransaction(tx -> {
                Result result = tx.run(suggestionQuery, parameters("nickname", nickname));
                ArrayList<Question> questions = new ArrayList<>();
                while(result.hasNext()){
                    Record r = result.next();

                    // get the cratedDate and convert it in the right format
                    String dateString = r.get("createdDate").asString();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = dateFormat.parse(dateString);
                    } catch(ParseException e){
                        System.out.println("Parse error");
                    }

                    // get the list of answers
                    List<Answer> answers = new ArrayList<>();
                    for (Object answerValue : r.get("answers").asList()) {
                        answers.add(new Answer((Answer) answerValue));
                    }


                    // reported = false as a default value
                    Question q = new Question(r.get("id").asString(), r.get("title").asString(), null,
                            r.get("topic").asString(), r.get("author").asString(), answers, r.get("closed").asBoolean(), date, false);
                }
                return questions;
            });
            return suggestedQuestions;
        }

    }
}
