package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner = new Scanner(System.in);

    public int mainMenuAdmin() {
        System.out.println("############################################# MAIN MENU ############################################");
        int choice = -1;
        do {
            // TODO
            // 1) Controlla domande riportate 2) Controllare risposte riportate 3) Analytics 4) Browse 3) Search
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
}
