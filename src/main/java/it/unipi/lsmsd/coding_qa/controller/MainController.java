package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.view.*;

public class MainController {
    private static AuthenticationController authController = new AuthenticationController();
    private MainView mainView = new MainView();

    public void startApplication() {
        int choice = authController.start();
        if (choice == -1 || choice == 4) return; // CORREGGERE SE NECESSARIO
        // Exit : logged, exit
        // Se logged: showMenu
        // In base alla scelta del menu
        // chiamo i vari controller
    }

    public void mainManu() {
        int choice = mainView.mainMenuLoggedIn();
    }
}
