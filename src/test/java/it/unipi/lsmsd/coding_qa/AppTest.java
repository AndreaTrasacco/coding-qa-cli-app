package it.unipi.lsmsd.coding_qa;

import it.unipi.lsmsd.coding_qa.dao.AnswerDAO;
import it.unipi.lsmsd.coding_qa.dao.mongodb.AnswerMongoDBDAO;
import it.unipi.lsmsd.coding_qa.model.Answer;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{

    public static void main(String[] args){
        AnswerDAO answerDAO = new AnswerMongoDBDAO();
        try {
            answerDAO.create("1", new Answer("1", "a", new Date(), "a", 1, new ArrayList<>(), false, false));
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
