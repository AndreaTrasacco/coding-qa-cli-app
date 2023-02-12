package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class SuggestionsView {
    private Scanner scanner = new Scanner(System.in);

    public int menuSuggestions() {
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Open a question");
            System.out.println("\t[2] Go to the next page");
            System.out.println("\t[3] Go to the previous page");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }
}
