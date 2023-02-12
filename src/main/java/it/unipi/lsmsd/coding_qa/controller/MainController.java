package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.view.*;

public class MainController { // This class manages logic behind main functionalities of users
    private static MainView mainView = new MainView();

    public static void startApplication() {
        int choice = AuthenticationController.start();
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
            case 0: AdminController.start(); break;
            case 1: mainMenuLoggedUser(); break;
            case 2: mainMenuNotLoggedUser(); break;
        }
    }

    public static void mainMenuLoggedUser() {
        do {
            switch (mainView.mainMenuLoggedIn()) {
                case 1: // browse questions
                    QuestionController.browseQuestions(1);
                    break;
                case 2: // search question
                    QuestionController.searchQuestion(1);
                    break;
                case 3: // publish new question
                    QuestionController.createQuestion();
                    break;
                case 4: // Browse suggested questions to answer
                    SuggestionsController.browseSuggestedQuestions(false);
                    break;
                case 5: // Browse suggested questions to read
                    SuggestionsController.browseSuggestedQuestions(true);
                    break;
                case 6: // Open your profile
                    UserController.openSelfProfile();
                    break;
                case 7: // Analytics
                    AnalyticsController.start();
                    break;
                case 8: // Exit
                    System.exit(0);
            }
        } while (true);
    }

    private static void mainMenuNotLoggedUser() {
        do {
            switch (mainView.mainMenuNotLoggedIn()) {
                case 1: // browse questions
                    QuestionController.browseQuestions(2);
                    break;
                case 2: // search question
                    QuestionController.searchQuestion(2);
                    break;
                case 3: // Exit
                    System.exit(0);
            }
        } while (true);
    }

}
