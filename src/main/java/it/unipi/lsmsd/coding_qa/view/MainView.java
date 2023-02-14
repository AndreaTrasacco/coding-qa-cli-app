package it.unipi.lsmsd.coding_qa.view;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;

import java.util.Scanner;

public class MainView {
    private Scanner scanner = new Scanner(System.in);

    public int mainMenuNotLoggedIn() {
        System.out.println("#################################### MAIN MENU (NOT LOGGED IN) #####################################");
        int choice;
        do {
            System.out.println("\t[1] Browse questions");
            System.out.println("\t[2] Search question");
            System.out.println("\t[3] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
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
            System.out.println("\t[3] Publish new question");
            System.out.println("\t[4] Browse suggested questions to answer");
            System.out.println("\t[5] Browse suggested questions to read");
            System.out.println("\t[6] Open your profile");
            System.out.println("\t[7] Go to analytics menu");
            System.out.println("\t[8] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 8);
        if(choice == 8)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int inputMessageWithPaging(String message, int size){
        int number;
        do {
            System.out.println("\t* " + message + ": ");
            System.out.println("Input: ");
            number = Integer.parseInt(scanner.nextLine());;
        } while (number < 1 || number > size);
        return number;
    }

    public void showMessage(String message){
        System.out.println(message);
    }

    public <T> void viewPage(PageDTO<T> pageDTO){
        System.out.println(pageDTO);
    }

    public void view(Object o){
        System.out.println(o);
    }

}
