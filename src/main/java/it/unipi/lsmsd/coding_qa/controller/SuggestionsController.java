package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionPageDTO;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.service.SuggestionsService;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.MainView;
import it.unipi.lsmsd.coding_qa.view.SuggestionsView;

public class SuggestionsController {
    private static SuggestionsView suggestionsView = new SuggestionsView();
    private static MainView mainView = new MainView();
    private static SuggestionsService suggestionsService = ServiceLocator.getSuggestionService();
    private static QuestionService questionService = ServiceLocator.getQuestionService();

    public static void browseSuggestedQuestions(boolean type) { // type: true for suggested Q to read ; false for suggested Q to answer
        try {
            int page = 1;
            do {
                PageDTO<QuestionDTO> pageDTO;
                if(type)
                    pageDTO = suggestionsService.questionsToReadSuggestions(page, AuthenticationController.getLoggedUserNickname());
                else
                    pageDTO = suggestionsService.questionsToAnswerSuggestions(page, AuthenticationController.getLoggedUserNickname());
                switch (suggestionsView.menuSuggestions()) {
                    case 1: // Open a question
                        if (pageDTO.getCounter() == 0)
                            mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                        else {
                            int number = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter());
                            QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(pageDTO.getEntries().get(number - 1).getId());
                            QuestionController.questionPageLogged(questionPageDTO);
                        }
                        break;
                    case 2: // Go to the next page
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3: // Go to the previous page
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4: // Exit
                        return;
                }
            } while (true);
        } catch (
                Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}
