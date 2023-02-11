package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.view.*;

public class MainController {
    private AuthenticationController authController = new AuthenticationController();
    private AdminController adminController = new AdminController();
    private MainView mainView = new MainView();

    public void startApplication() {
        int choice = authController.start();
        if (choice == -1 || choice == 4) return;
        if(authController.getLoggedUser() != null){
            if(authController.getLoggedUser().getNickname().equals("admin")){
                adminController.start(); // TODO PRENDERE VALORE DI RITORNO
            } else {

            }
        } else {

        }
        // Exit : logged, exit
        // Se logged: showMenu
        // In base alla scelta del menu
        // chiamo i vari controller
    }

    public void mainManu() {
        int choice = mainView.mainMenuLoggedIn();
    }

}
