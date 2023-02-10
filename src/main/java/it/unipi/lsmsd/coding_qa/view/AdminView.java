package it.unipi.lsmsd.coding_qa.view;

import java.util.Scanner;

public class AdminView {
    private Scanner scanner = new Scanner(System.in);

    public static void main(String [] args)
    {
        AdminView adminView = new AdminView();
        adminView.mainMenuAdmin();
    }
    public int mainMenuAdmin() {
        System.out.println("########################################## MAIN ADMIN MENU #########################################");
        int choice = -1;
        do {
            System.out.println("\t[1] Browse reported questions");
            System.out.println("\t[2] Browse reported answers");
            System.out.println("\t[3] Go to analytics menu");
            System.out.println("\t[4] Browse questions");
            System.out.println("\t[5] Search question");
            System.out.println("\t[6] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 6);
        if(choice == 6)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public int adminUserProfileMenu(){
        System.out.println("##################################### ADMIN USER PROFILE MENU ######################################");
        int choice;
        do {
            System.out.println("\t[1] Delete user");
            System.out.println("\t[2] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 2);
        if (choice == 2)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public void deleteUserMessage() {
        System.out.println("########################################### USER DELETED ###########################################");
    }

    public int adminQuestionMenu(){
        System.out.println("####################################### ADMIN QUESTION MENU ########################################");
        int choice;
        do {
            System.out.println("\t[1] Delete question");
            System.out.println("\t[2] Choose an answer to delete");
            System.out.println("\t[3] Exit");
            System.out.println("Input: ");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 3);
        if (choice == 3)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public void deleteAnswer(int maxAnswer){
        int i;
        do{
            System.out.println("\tType the number of the answer to delete: ");
            i = scanner.nextInt();
        } while (i < 0 || i > maxAnswer);
        System.out.println("########################################## ANSWER DELETED ##########################################");
    }

    public void deleteQuestionMessage() {
        System.out.println("######################################### QUESTION DELETED #########################################");
    }
}
