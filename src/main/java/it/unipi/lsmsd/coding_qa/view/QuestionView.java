package it.unipi.lsmsd.coding_qa.view;

import it.unipi.lsmsd.coding_qa.dto.*;

import java.util.Scanner;

public class QuestionView {
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        QuestionView questionView = new QuestionView();
        //questionView.search(new QuestionSearchDTO());
        //questionView.menuInQuestionPageLogged();
        //questionView.menuInQuestionPageNonLogged();
        //questionView.menuInAnswerPageLogged();
        //questionView.menuInAnswerPageNonLogged();
        //questionView.menuInVoteAnswer();
        //questionView.menuInQuestionPageOwner();
        //questionView.menuInAnswerPageOwner();
        //System.out.println(questionView.specifyQuestionIndex(new PageDTO<QuestionDTO>()));
        //System.out.println(questionView.specifyAnswerIndex(new PageDTO<AnswerDTO>()));
        //questionView.browseQuestionsMenu();
    }

    public void search(QuestionSearchDTO questionSearchDTO){
        // Input topic e testo da ricercare
        System.out.println("########################################## SEARCH QUESTION #########################################");
        System.out.println("* Topic: ");
        questionSearchDTO.setTopic(scanner.nextLine());
        System.out.println("* Text: ");
        questionSearchDTO.setText(scanner.nextLine());
    }

    public int menuInQuestionPageLogged(){
        // 2 menu (uno solo domanda e uno sulle risposte nella pagina) IN METODI DIVERSI
        // primo menu : rispodi | report | browse answers | ..
        // secondo menu dopo aver aperto riposte : vote | report | next page | previous page ATTENZIONE

        System.out.println("########################################### QUESTION MENU ##########################################");
        int choice;
        do {
            System.out.println("\t[1] Answer");
            System.out.println("\t[2] Report");
            System.out.println("\t[3] Browse answers");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 4);
        if(choice == 4)
            System.out.println("############################################### EXIT ###############################################");
        return choice;

        // ATTENZIONE : ALCUNI NON VANNO BENE SE NON LOGGED --> METTERE IN UNA CLASSE DIVERSA?? O IN METODI DIVERSI
        // Report
        // Vota una risposta
        // Pagina successiva/Precedente
        // RISPONDI ALLA DOMANDA
        // If question is mine --> Accept an answer
        // If question is mine --> Delete question
    }

    public int menuInQuestionPageNonLogged(){
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Browse answers");
            System.out.println("\t[2] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 2);
        if(choice == 2)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInQuestionPageOwner(){
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Answer");
            System.out.println("\t[2] Browse answers");
            System.out.println("\t[3] Delete");
            System.out.println("\t[4] Update");
            System.out.println("\t[5] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 5);
        if(choice == 5)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int browseQuestionsMenu(){
        int choice;
        System.out.println("######################################## BROWSE QUESTION MENU ######################################");
        do {
            System.out.println("\t[1] View Question");
            System.out.println("\t[2] Next page");
            System.out.println("\t[3] Previous page");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 4);
        if(choice == 4)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int specifyQuestionIndex(PageDTO<QuestionDTO> pageDTO){
        int index;
        do{
            System.out.println("Digit the question index: ");
            index = Integer.parseInt(scanner.nextLine());;
        } while(index < 1 || index > pageDTO.getCounter());
        return index;
    }

    public int menuInAnswerPageLogged(){
        int choice;
        System.out.println("############################################ ANSWER MENU ###########################################");
        do {
            System.out.println("\t[1] Vote");
            System.out.println("\t[2] Report");
            System.out.println("\t[3] Next Page");
            System.out.println("\t[4] Previous Page");
            System.out.println("\t[5] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 5);
        if(choice == 5)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInAnswerPageNotLogged(){
        int choice;
        System.out.println("############################################ ANSWER MENU ###########################################");
        do {
            System.out.println("\t[1] Next Page");
            System.out.println("\t[2] Previous Page");
            System.out.println("\t[3] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 3);
        if(choice == 3)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInAnswerPageOwner(){
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Vote");
            System.out.println("\t[2] Report");
            System.out.println("\t[3] Accept");
            System.out.println("\t[4] Delete");
            System.out.println("\t[5] Next Page");
            System.out.println("\t[6] Previous Page");
            System.out.println("\t[7] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 7);
        if(choice == 6)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInVoteAnswer(){
        // 1) Vote up
        // 2) Vote down

        int choice;
        System.out.println("Press [1] for upvote\nPress[2] for downvote");
        do {
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());;
        } while (choice < 1 || choice > 2);

        return choice;
    }

    public void modifyAnswer(AnswerModifyDTO answerDTO){
        System.out.println("########################################### MODIFY ANSWER ##########################################");
        System.out.println("(Press Enter if you don't to modify the field): ");
        System.out.println("* Body: ");
        String body = scanner.nextLine();
        if(body != ""){
            answerDTO.setBody(body);
        }
        System.out.println("########################################## ANSWER MODIFIED #########################################");
    }

    public void modifyQuestion(QuestionModifyDTO questionDTO){
        System.out.println("########################################## MODIFY QUESTION #########################################");
        System.out.println("(Press Enter if you don't to modify the field): ");
        System.out.println("* Title: ");
        String title = scanner.nextLine();
        if(title != ""){
            questionDTO.setBody(title);
        }
        System.out.println("* Body: ");
        String body = scanner.nextLine();
        if(body != ""){
            questionDTO.setBody(body);
        }
        System.out.println("* Topic: ");
        String topic = scanner.nextLine();
        if(topic != ""){
            questionDTO.setTopic(topic);
        }

        System.out.println("######################################### QUESTION MODIFIED ########################################");
    }
    public void addAnswer(AnswerDTO answerDTO){
        System.out.println("############################################# ADD ANSWER ###########################################");
        System.out.println("* Body: ");
        answerDTO.setBody(scanner.nextLine());
        System.out.println("############################################ ANSWER ADDED ##########################################");

    }

    public void reportQuestion(){
        System.out.println("######################################### QUESTION REPORTED ########################################");
    }

    public void deleteQuestion(){
        System.out.println("########################################## QUESTION DELETED ########################################");
    }

    public void viewAnswer(AnswerDTO answerDTO){
        System.out.println(answerDTO);
    }

    public void reportAnswer(){
        System.out.println("########################################## ANSWER REPORTED #########################################");
    }

    public void acceptAnswer(){
        System.out.println("########################################## ANSWER ACCEPTED #########################################");
    }

    public void deleteAnswer(){
        System.out.println("########################################### ANSWER DELETED #########################################");
    }

    public void voteAnswer(){
        System.out.println("############################################ ANSWER VOTED ##########################################");
    }
}
