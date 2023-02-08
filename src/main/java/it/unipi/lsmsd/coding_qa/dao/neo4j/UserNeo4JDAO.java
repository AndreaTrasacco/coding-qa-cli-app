package it.unipi.lsmsd.coding_qa.dao.neo4j;

import it.unipi.lsmsd.coding_qa.dao.UserNodeDAO;
import it.unipi.lsmsd.coding_qa.dao.base.BaseNeo4JDAO;
import it.unipi.lsmsd.coding_qa.dao.exception.DAONodeException;
import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import org.neo4j.driver.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class UserNeo4JDAO extends BaseNeo4JDAO implements UserNodeDAO {

    //this method adds a new User node
    @Override
    public void create(final String nickname) throws DAONodeException {
        final String insertUser = "CREATE (u: User{nickname: $nickname})";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(insertUser, parameters("nickname", nickname)).consume();
                return 1;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }
    }

    //this method deletes a User node
    @Override
    public void delete(String nickname) throws DAONodeException {
        final String deleteUser = "MATCH (u: User{nickname: $nickname}) " +
                "DETACH DELETE u";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(deleteUser, parameters("nickname", nickname)).consume();
                return 1;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }
    }

    //this method returns the list of follower
    @Override
    public PageDTO<String> getFollowingList(String nickname) throws DAONodeException {
        List<String> followerList;
        PageDTO<String> pageDTO = new PageDTO<>();
        final String listOfUser = "MATCH (u:User{nickname : $nickname})-[:FOLLOW]->(u1:User) " +
                "RETURN u1.nickname as nickname";
        try(Session session = getSession()){
            followerList = session.readTransaction( (TransactionWork<List<String>>) tx -> {
                Result result = tx.run(listOfUser, parameters("nickname", nickname));
                ArrayList<String> users = new ArrayList<>();
                while(result.hasNext()){
                    Record user = result.next();
                    users.add(user.get("nickname").asString());
                }
                return users;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }
        pageDTO.setCounter(followerList.size());
        pageDTO.setEntries(followerList);
        return pageDTO;
    }

    /*@Override
    public List<String> getFollowers(String nickname) {
        List<String> followerList;
        final String listOfUser = "MATCH (u: User)<-[:FOLLOW]-(u1: User)" +
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
    }*/

    //this method create the follow relationship
    @Override
    public void followUser(String myNickname, String userToFollow) throws DAONodeException {
        final String followString = "MATCH (u1:User{nickname : $myNickname}), (u2: User{nickname : $userToFollow}) " +
                "CREATE (u1)-[r:FOLLOW]->(u2)";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(followString, parameters("myNickname", myNickname, "userToFollow", userToFollow)).consume();
                return null;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }
    }

    // delete the relationship CREATED
    /*@Override
    public void deleteCreated(String nickname, String id) {
        final String deleteQuestion = "MATCH (u: User{nickname: $nickname})-[:CREATED]->(q: Question{id: $id})" +
                "DELETE q";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
               tx.run(deleteQuestion, parameters("nickname", nickname, "id", id));
               return 1;
            });
        }
    }*/

    // delete the relationship ANSWERED
    /*@Override
    public void deleteAnswered(String nickname, String id) {
        final String deleteQuestion = "MATCH (u: User{nickname: $nickname})-[:ANSWERED]->(q: Question{id: $id})" +
                "DELETE q";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(deleteQuestion, parameters("nickname", nickname, "id", id));
                return 1;
            });
        }
    }*/

    @Override
    public void deleteFollowed(String myNickname, String userToUnfollow) throws DAONodeException {
        final String unfollow = "MATCH (u1:User{nickname : $myNickname})-[r:FOLLOW]->(u2:User{nickname : $userToUnfollow}) " +
                "DETACH DELETE r";
        try(Session session = getSession()){
            session.writeTransaction(tx -> {
                tx.run(unfollow, parameters("myNickname", myNickname, "userToUnfollow", userToUnfollow)).consume();
                return null;
            });
        } catch (Exception e){
            throw new DAONodeException(e);
        }
    }
}
