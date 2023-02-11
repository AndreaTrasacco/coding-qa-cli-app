package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.view.*;

public class MainController { // This class manages logic behind "main functionalities" of users
    private AuthenticationController authController = new AuthenticationController();
    private AdminController adminController = new AdminController();
    private AnalyticsController analyticsController = new AnalyticsController();
    private UserController userController = new UserController();
    private QuestionController questionController = new QuestionController();
    private MainView mainView = new MainView();

    public void startApplication() {
        int choice = authController.start();
        if (choice == -1 || choice == 4) return;
        if (authController.getLoggedUser() != null) {
            if (authController.getLoggedUser().getNickname().equals("admin")) {
                adminController.start();
            } else {
                mainMenuLoggedUser();
            }
        } else {

        }
        // Exit : logged, exit
        // Se logged: showMenu
        // In base alla scelta del menu
        // chiamo i vari controller
    }

    public void mainMenuLoggedUser() {
        do {
            switch (mainView.mainMenuLoggedIn()) {
                case 1: // browse questions
                    questionController.browse(1);
                    break;
                case 2: // search question
                    questionController.search(1);
                    break;
                case 3: // publish new question
                    // questionController
                    break;
                case 4: // Browse suggested questions to answer
                    break;
                case 5: // Browse suggested questions to read
                    break;
                case 6: // Open your profile
                    userController.openSelfProfile();
                    break;
                case 7: // Analytics
                    analyticsController.start();
                    break;
                case 9: // Exit
                    return;
            }
        } while (true);
    }

}
