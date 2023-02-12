package it.unipi.lsmsd.coding_qa.dao.base;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public abstract class BaseNeo4JDAO {
    //private static final String NEO4J_URI = "neo4j://10.1.1.15:7687";
    private static final String NEO4J_URI = "neo4j://localhost:7687";
    private static final String NEO4J_USER = "neo4j";
    // private static final String NEO4J_PASSWORD = "studenti";
    private static final String NEO4J_PASSWORD = "neo4j";
    private static Driver neo4jDriver;

    //method that returns the driver instance
    private static Driver getDriver(){
        if(neo4jDriver == null)
            initPool();
        return neo4jDriver;
    }

    //method that returns the session
    public static Session getSession(){
        return getDriver().session();
    }

    //method that initializes the Neo4j driver
    public static void initPool(){
        neo4jDriver = GraphDatabase.driver(NEO4J_URI, AuthTokens.basic(NEO4J_USER, NEO4J_PASSWORD));
    }

    //method that closes the Neo4j connection
    public static void close(){
        neo4jDriver.close();
    }

}
