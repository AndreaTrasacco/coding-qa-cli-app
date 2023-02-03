package it.unipi.lsmsd.coding_qa.service.exception;

public class BusinessException extends Exception{
    public BusinessException(Exception ex){
        super(ex);
    }
    public BusinessException(String message){
        super(message);
    }
}
