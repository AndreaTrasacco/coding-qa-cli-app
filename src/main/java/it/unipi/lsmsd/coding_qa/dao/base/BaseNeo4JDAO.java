package it.unipi.lsmsd.coding_qa.dao.base;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public abstract class BaseNeo4JDAO {
    private static final String NEO4J_URI = "neo4j://localhost:7687"; //TODO mettere l'uri giusto
    private static final String NEO4J_USER = "neo4j"; //TODO mettere username giusto
    private static final String NEO4J_PASSWORD = "nicolo"; //TODO mettere password giusta

    private static Driver Neo4jDriver;

    //method that returns the driver instance
    private static Driver getConnection(){
        return Neo4jDriver;
    }

    //method that returns the session
    public static Session getSession(){
        return getConnection().session();
    }

    //method that initializes the Neo4j driver
    public static void initPool(){
        Neo4jDriver = GraphDatabase.driver(NEO4J_URI, AuthTokens.basic(NEO4J_USER, NEO4J_PASSWORD));
    }

    //method that closes the Neo4j connection
    public static void close(){
        Neo4jDriver.close();
    }

}
