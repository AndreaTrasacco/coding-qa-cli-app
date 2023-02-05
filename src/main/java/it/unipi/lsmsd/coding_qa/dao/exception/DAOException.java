package it.unipi.lsmsd.coding_qa.dao.exception;

public class DAOException extends Exception{
    public DAOException(Exception ex){
        super(ex);
    }
    public DAOException(String message){
        super(message);
    }
}

