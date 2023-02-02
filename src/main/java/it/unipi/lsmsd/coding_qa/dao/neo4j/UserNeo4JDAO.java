package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class UserNeo4JDAO extends BaseNeo4JDAO implements UserNodeDAO {

    //this method adds a new User node
    @Override
    public void create(final String nickname) {
        //TODO il controllo sull'unicità del nickname va messo qui???
        final String insertUser = "CREATE (u: User{nickname: $nickname})";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(insertUser, parameters("nickname", nickname)).consume();
                return 1;
            });
        }
    }

    //this method deletes a User node
    @Override
    public void delete(String nickname) {
        final String deleteUser = "MATCH (u: User{nickname: $nickname})" +
                "DELETE u";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(deleteUser, parameters("nickname", nickname)).consume();
                return 1;
            });
        }
    }

    //this method updates a User node
    @Override
    public void update(String nickname) {
        final String updateUser = "MATCH (u: User{nickname: $nickname})" +
                "SET u.nickname = $nickname";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(updateUser, parameters("nickname", nickname)).consume();
                return 1;
            });
        }
    }

    //this method returns the list of follower
    @Override
    public List<String> getFollowerList(String nickname) {
        List<String> followerList;
        final String listOfUser = "MATCH (u: User)-[:FOLLOW]->(u1: User)" +
                "WHERE u.nickname = $nickname" +
                "RETURN u1.nickname as Nickname";
        try(Session session = getSession()){
            followerList = session.readTransaction( (TransactionWork<List<String>>) tx -> {
                Result result = tx.run(listOfUser, parameters("nickname", nickname));
                ArrayList<String> users = new ArrayList<>();
                while(result.hasNext()){
                    Record user = result.next();
                    users.add(user.get("Nickname").asString());
                }
                return users;
            });
        }
        return followerList;
    }

    //this method create the follow relationship
    @Override
    public void followUser(String myNickname, String userToFollow) {
        final String followString = "MATCH (u1: User), (u2: User)" +
                "WHERE u1.nickname = $myNickname AND u2.nickname = $userToFollow" +
                "CREATE (u1)-[r:FOLLOW]->(u2)" +
                "RETURN type(r)";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(followString, parameters("myNickname", myNickname, "userToFollow", userToFollow)).consume();
                return 1;
            });
        }
    }

    @Override
    public void deleteQuestion(String nickname, String id) {
        final String deleteQuestion = "MATCH (u: User{nickname: $nickname})-[:CREATED]->(q: Question{id: $id})" +
                "DELETE q";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
               tx.run(deleteQuestion, parameters("nickname", nickname, "id", id));
               return 1;
            });
        }
    }

    @Override
    public void deleteAnswer(String nickname, String id) {
        final String deleteQuestion = "MATCH (u: User{nickname: $nickname})-[:ANSWERED]->(q: Question{id: $id})" +
                "DELETE q";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(deleteQuestion, parameters("nickname", nickname, "id", id));
                return 1;
            });
        }
    }
}
