package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner = new Scanner(System.in);

    public int mainMenuAdmin() {
        System.out.println("\n########################################## ADMIN MAIN MENU #########################################");
        int choice;
        do {
            System.out.println("\t[1] Browse reported questions");
            System.out.println("\t[2] Browse reported answers");
            System.out.println("\t[3] Go to analytics menu");
            System.out.println("\t[4] Browse questions");
            System.out.println("\t[5] Search question");
            System.out.println("\t[6] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
            ;
        } while (choice < 1 || choice > 6);
        if (choice == 6)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public void deleteUserMessage() {
        System.out.println("########################################### USER DELETED ###########################################");
    }

    public int adminQuestionMenu() {
        System.out.println("####################################### ADMIN QUESTION MENU ########################################");
        int choice;
        do {
            System.out.println("\t[1] Delete question");
            System.out.println("\t[2] Toggle report");
            System.out.println("\t[3] Open author's profile");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
            ;
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int adminReportedQuestionMenu() {
        System.out.println("################################### ADMIN REPORTED QUESTION MENU ###################################");
        int choice;
        do {
            System.out.println("\t[1] Open a question");
            System.out.println("\t[2] Go to the next page");
            System.out.println("\t[3] Go to the previous page");
            System.out.println("\t[4] Exit from reported questions");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
            ;
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int adminReportedAnswersMenu() {
        System.out.println("################################### ADMIN REPORTED ANSWERS MENU ###################################");
        int choice;
        do {
            System.out.println("\t[1] Read an answer");
            System.out.println("\t[2] Go to the next page");
            System.out.println("\t[3] Go to the previous page");
            System.out.println("\t[4] Exit from reported answers");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
            ;
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int adminAnswerMenu() {
        System.out.println("####################################### ADMIN ANSWER MENU ########################################");
        int choice;
        do {
            System.out.println("\t[1] Delete answer");
            System.out.println("\t[2] Toggle report");
            System.out.println("\t[3] Open author's profile");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
            ;
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public void deleteQuestionMessage() {
        System.out.println("######################################### QUESTION DELETED #########################################");
    }
}
