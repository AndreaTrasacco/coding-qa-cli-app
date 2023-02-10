package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class MainView {
    private Scanner scanner = new Scanner(System.in);


    /*
    * 1) Browse questions
    * 2) Search question
    * 3) Browse followed users (LOGGED)
    * 4) Publish new question (LOGGED)
    * 5) Browse suggested questions to answer (LOGGED)
    * 6) Browse suggested questions to read (LOGGED)
    * 7) Open your profile (LOGGED)
    * 8) Analytics
    * 9) ...
    * 10) Logout (LOGGED) | Exit (NOT LOGGED)
    * */

    public int mainMenuLoggedIn() {
        System.out.println("############################################# MAIN MENU ############################################");
        int choice;
        do {
            // TODO
            System.out.println("\t[1] Sign up");
            System.out.println("\t[2] Log in");
            System.out.println("\t[3] Go to main menu without logging in");
            System.out.println("\t[4] Exit");
            System.out.print("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 && choice > 4);
        if(choice == 4) // TODO
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int mainMenuNotLoggedIn() {
        System.out.println("##################################### MAIN MENU (NOT LOGGED IN) ####################################");
        int choice;
        do {
            // TODO
            System.out.println("\t[1] Sign up");
            System.out.println("\t[2] Log in");
            System.out.println("\t[3] Go to main menu without logging in");
            System.out.println("\t[4] Exit");
            System.out.print("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 && choice > 4);
        if(choice == 4) // TODO
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int menuAnalytics(){
        // 1) 2) 3)
        return 0;
    }
}
