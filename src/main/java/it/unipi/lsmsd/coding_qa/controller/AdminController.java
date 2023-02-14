package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dao.mongodb.QuestionMongoDBDAO;
import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.service.*;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.*;

import java.util.Date;

public class AdminController {
    private static AdminView adminView = new AdminView();
    private static MainView mainView = new MainView();
    private static QuestionService questionService = ServiceLocator.getQuestionService();;
    private static AnswerService answerService = ServiceLocator.getAnswerService();;
    private static UserService userService = ServiceLocator.getUserService();;

    public static void start() {
        do {
            switch (adminView.mainMenuAdmin()) {
                case 1: // reported questions
                    manageReportedQuestions();
                    break;
                case 2: // reported answers
                    manageReportedAnswers();
                    break;
                case 3: // analytics
                    AnalyticsController.start();
                    break;
                case 4: // browse questions
                    QuestionController.browseQuestions();
                    break;
                case 5: // search questions
                    QuestionController.searchQuestion();
                    break;
                case 6: // exit from the application
                    System.exit(0);
            }
        } while (true);
    }

    private static void manageReportedQuestions() {
        try {
            int page = 1;
            do {
                PageDTO<QuestionDTO> pageDTO = questionService.getReportedQuestions(page);
                mainView.viewPage(pageDTO);
                if (pageDTO.getCounter() == 0){
                    return;
                }

                switch (adminView.adminReportedQuestionMenu()) {
                    case 1: // Open a question
                        if (pageDTO.getCounter() == 0)
                            mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                        else {
                            int number = mainView.inputMessageWithPaging("Specify the question number", pageDTO.getCounter());
                            QuestionPageDTO questionPageDTO = questionService.getQuestionInfo(pageDTO.getEntries().get(number - 1).getId());
                            mainView.view(questionPageDTO);
                            switch (adminView.adminQuestionMenu()) {
                                case 1: // delete question
                                    questionService.deleteQuestion(questionPageDTO.getId());
                                    break;
                                case 2: // toggle the report from the question
                                    questionService.reportQuestion(questionPageDTO.getId(), false);
                                    break;
                                case 3: // open user profile
                                    UserController.openProfileIfAdmin(userService.getInfo(questionPageDTO.getAuthor()));
                                    break;
                                case 4: // go back
                                    return;
                            }
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    private static void manageReportedAnswers() {
        try {
            int page = 1;
            do {
                PageDTO<AnswerDTO> pageDTO = answerService.getReportedAnswers(page);
                mainView.viewPage(pageDTO);
                if(page == 1 && pageDTO.getCounter() == 0)
                    return;
                switch (adminView.adminReportedAnswersMenu()) {
                    case 1: // Read answer
                        if (pageDTO.getCounter() == 0)
                            mainView.showMessage("!!!! ACTION NOT POSSIBLE !!!!");
                        else {
                            int number = mainView.inputMessageWithPaging("Specify the answer number", pageDTO.getCounter());
                            AnswerDTO answerDTO = pageDTO.getEntries().get(number - 1);
                            answerService.getCompleteAnswer(answerDTO);
                            mainView.view(answerDTO);
                            switch (adminView.adminAnswerMenu()) {
                                case 1: // delete answer
                                    answerService.deleteAnswer(answerDTO);
                                    break;
                                case 2: // toggle the report from the answer
                                    answerService.reportAnswer(answerDTO.getId(), false);
                                    break;
                                case 3: // open user profile
                                    UserController.openProfileIfAdmin(userService.getInfo(answerDTO.getAuthor()));
                                    break;
                                case 4: // go back
                                    return;
                            }
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}
