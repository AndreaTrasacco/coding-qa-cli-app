package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.service.implementation.AggregationsServiceImpl;
import it.unipi.lsmsd.coding_qa.service.implementation.QuestionServiceImpl;
import it.unipi.lsmsd.coding_qa.service.implementation.SuggestionsServiceImpl;
import it.unipi.lsmsd.coding_qa.service.implementation.UserServiceImpl;

public class ServiceLocator {
    private static AggregationsService aggregationsService = new AggregationsServiceImpl();
    private static QuestionService questionService = new QuestionServiceImpl();
    private static SuggestionsService suggestionService = new SuggestionsServiceImpl();
    private static UserService userService = new UserServiceImpl();

    public static AggregationsService getAggregationsService() {
        return aggregationsService;
    }

    public static QuestionService getQuestionService() {
        return questionService;
    }

    public static SuggestionsService getSuggestionService() {
        return suggestionService;
    }

    public static UserService getUserService() {
        return userService;
    }
}
