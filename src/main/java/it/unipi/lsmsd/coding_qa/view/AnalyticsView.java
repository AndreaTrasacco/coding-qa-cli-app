package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class AnalyticsView {
    private Scanner scanner = new Scanner(System.in);

    public int menuAnalytics() {
        System.out.println("########################################## ANALYTICS MENU ##########################################");
        int choice;
        do {
            System.out.println("\t[1] Show most useful question for each topic");
            System.out.println("\t[2] Show experience levels for each country");
            System.out.println("\t[3] Show most discussed topics of the week");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }
}
