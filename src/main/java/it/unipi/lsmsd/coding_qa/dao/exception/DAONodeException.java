package it.unipi.lsmsd.coding_qa.dao.exception;

public class DAONodeException extends Exception{
    public DAONodeException(Exception ex){
        super(ex);
    }
    public DAONodeException(String message){
        super(message);
    }
}

