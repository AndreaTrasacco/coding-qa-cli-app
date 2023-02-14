package it.unipi.lsmsd.coding_qa.view;

import java.util.List;
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

    public <T> void showAnalytic(List<T> list){
        if(list.size() == 0)
            System.out.println("############################################### EMPTY ##############################################");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("################################################ " + (i + 1) + " ################################################");
            System.out.println(list.get(i));
        }
    }

}
