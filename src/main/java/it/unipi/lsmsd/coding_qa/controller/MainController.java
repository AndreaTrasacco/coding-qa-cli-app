package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.view.*;

public class MainController { // This class manages logic behind main functionalities of users
    private AuthenticationController authController = new AuthenticationController();
    private AdminController adminController = new AdminController();
    private AnalyticsController analyticsController = new AnalyticsController();
    private UserController userController = new UserController();
    private QuestionController questionController = new QuestionController();
    private SuggestionsController suggestionsController = new SuggestionsController();
    private MainView mainView = new MainView();

    public void startApplication() {
        int choice = authController.start();
        if (choice == -1 || choice == 4) return;
        int type = 2;
        if (AuthenticationController.getLoggedUser() != null) {
            if (AuthenticationController.getLoggedUserNickname().equals("admin")) {
                type = 0;
            } else {
                type = 1;
            }
        }
        switch (type){
            case 0: adminController.start(); break;
            case 1: mainMenuLoggedUser(); break;
            case 2: mainMenuNotLoggedUser(); break;
        }
    }

    public void mainMenuLoggedUser() {
        do {
            switch (mainView.mainMenuLoggedIn()) {
                case 1: // browse questions
                    questionController.browseQuestions(1);
                    break;
                case 2: // search question
                    questionController.searchQuestion(1);
                    break;
                case 3: // publish new question
                    questionController.createQuestion();
                    break;
                case 4: // Browse suggested questions to answer
                    suggestionsController.browseSuggestedQuestions(false);
                    break;
                case 5: // Browse suggested questions to read
                    suggestionsController.browseSuggestedQuestions(true);
                    break;
                case 6: // Open your profile
                    userController.openSelfProfile();
                    break;
                case 7: // Analytics
                    analyticsController.start();
                    break;
                case 8: // Exit
                    System.exit(0);
            }
        } while (true);
    }

    private void mainMenuNotLoggedUser() {
        do {
            switch (mainView.mainMenuNotLoggedIn()) {
                case 1: // browse questions
                    questionController.browseQuestions(2);
                    break;
                case 2: // search question
                    questionController.searchQuestion(2);
                    break;
                case 3: // Exit
                    System.exit(0);
            }
        } while (true);
    }

}
