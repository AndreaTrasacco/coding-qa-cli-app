package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class MainView {
    private Scanner scanner = new Scanner(System.in);

    public static void main(String [] args)
    {
        MainView mainView = new MainView();
        mainView.mainMenuLoggedIn();
        mainView.mainMenuNotLoggedIn();
        mainView.menuAnalytics();
    }

    public int mainMenuNotLoggedIn() {
        System.out.println("#################################### MAIN MENU (NOT LOGGED IN) #####################################");
        int choice;
        do {
            System.out.println("\t[1] Browse questions");
            System.out.println("\t[2] Search question");
            System.out.println("\t[3] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 3);
        if(choice == 3)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int mainMenuLoggedIn() {
        System.out.println("############################################# MAIN MENU ############################################");
        int choice;
        do {
            System.out.println("\t[1] Browse questions");
            System.out.println("\t[2] Search question");
            System.out.println("\t[4] Publish new question");
            System.out.println("\t[5] Browse suggested questions to answer");
            System.out.println("\t[6] Browse suggested questions to read");
            System.out.println("\t[7] Open your profile menu");
            System.out.println("\t[8] Go to analytics menu");
            System.out.println("\t[9] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 9);
        if(choice == 9)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int menuAnalytics(){
        System.out.println("########################################## ANALYTICS MENU ##########################################");
        int choice;
        do {
            System.out.println("\t[1] Show most useful question for each topic");
            System.out.println("\t[2] Show experience levels for each country");
            System.out.println("\t[3] Show most discussed topics of the week");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 4);
        if(choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }
}
