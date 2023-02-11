package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;

public class QuestionController {
    private QuestionService questionService;
    private AnswerService answerService;

    public QuestionController(){
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
    }

    // Browse e search for admin, logged, notlogged (VALUTARE SE TROPPO DUPICATO)

    public void browseAdmin() { // TODO
    }

    public void searchAdmin() { // TODO
    }

    // menu se logged oppure no
    // operazioni varie anche tutto in un solo metodo se basta
}
