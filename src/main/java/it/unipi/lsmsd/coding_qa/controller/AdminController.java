package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.service.*;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.*;

public class AdminController {
    private AdminView adminView = new AdminView();
    private MainView mainView = new MainView();
    private QuestionService questionService;
    private AnswerService answerService;
    private UserService userService;
    private UserController userController = new UserController();
    private AnalyticsController analyticsController = new AnalyticsController();
    private QuestionController questionController = new QuestionController();

    public AdminController() {
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
        userService = ServiceLocator.getUserService();
    }

    public void start() {
        do {
            switch (adminView.mainMenuAdmin()) {
                case 1: // reported questions
                    manageReportedQuestions();
                    break;
                case 2: // reported answers
                    manageReportedAnswers();
                    break;
                case 3: // analytics
                    analyticsController.start();
                    break;
                case 4: // browse questions
                    questionController.browseQuestions(0);
                    break;
                case 5: // search questions
                    questionController.searchQuestion(0);
                    break;
                case 6: // exit from the application
                    System.exit(0);
            }
        } while (true);
    }

    private void manageReportedQuestions() {
        try {
            int page = 1;
            do {
                PageDTO<QuestionDTO> pageDTO = questionService.getReportedQuestions(page);
                mainView.viewPage(pageDTO);
                if (pageDTO.getCounter() == 0) return;
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
                                    userController.openProfileIfAdmin(userService.getInfo(questionPageDTO.getAuthor()));
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

    private void manageReportedAnswers() {
        try {
            int page = 1;
            do {
                PageDTO<AnswerDTO> pageDTO = answerService.getReportedAnswers(page);
                mainView.viewPage(pageDTO);
                if (pageDTO.getCounter() == 0) return;
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
                                    userController.openProfileIfAdmin(userService.getInfo(answerDTO.getAuthor()));
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
