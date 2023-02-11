package it.unipi.lsmsd.coding_qa.utils;

public class UtilsPage { // TODO CANCELLARE
    public static int getFirstNumber(int page){
        return (page - 1) * Constants.PAGE_SIZE + 1;
    }

    public static int getLastNumber(int page, int size){
        return (page - 1) * Constants.PAGE_SIZE + size;
    }
}
