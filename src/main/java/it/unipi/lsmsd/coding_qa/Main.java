package it.unipi.lsmsd.coding_qa;

import it.unipi.lsmsd.coding_qa.controller.MainController;
import it.unipi.lsmsd.coding_qa.dao.base.BaseMongoDBDAO;

public class Main {
    public static void main(String [] args){
        try {
            BaseMongoDBDAO.init();
            MainController.startApplication();
        } finally {
            BaseMongoDBDAO.close();
        }
    }
}
