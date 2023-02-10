package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.service.*;

public class AdminController {
    private QuestionService questionService;
    private AnswerService answerService;
    private UserService userService;

    public AdminController(){
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
        userService = ServiceLocator.getUserService();
    }

    // menu iniziale admin e gestione operazioni admin
}
