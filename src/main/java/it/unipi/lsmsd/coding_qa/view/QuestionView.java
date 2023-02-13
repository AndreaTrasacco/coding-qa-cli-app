package it.unipi.lsmsd.coding_qa.view;

import it.unipi.lsmsd.coding_qa.dto.*;
import it.unipi.lsmsd.coding_qa.utils.Constants;

import java.util.Arrays;
import java.util.Scanner;

public class QuestionView {
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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

    public void search(QuestionSearchDTO questionSearchDTO) {
        // Input topic and text to be searched
        System.out.println("########################################## SEARCH QUESTION #########################################");
        String topic;
        System.out.println("Possible Topics: " + Arrays.toString(Constants.TOPICS.toArray()));
        do {
            System.out.println("* Topic: ");
            topic = scanner.nextLine();
        } while (!Constants.TOPICS.contains(topic));
        questionSearchDTO.setTopic(topic);
        System.out.println("* Text: ");
        questionSearchDTO.setText(scanner.nextLine());
    }

    public int menuInQuestionPageNotLoggedOrAdmin() {
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Browse answers");
            System.out.println("\t[2] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 2);
        if (choice == 2)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInQuestionPageLoggedOrOwner() {
        int choice;
        System.out.println("########################################### QUESTION MENU ##########################################");
        do {
            System.out.println("\t[1] Answer");
            System.out.println("\t[2] Browse answers");
            System.out.println("\t[3] Report");
            System.out.println("\t[4] Delete");
            System.out.println("\t[5] Update");
            System.out.println("\t[6] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 6);
        if (choice == 6)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int searchQuestionsMenu() {
        int choice;
        System.out.println("######################################## BROWSE QUESTION MENU ######################################");
        do {
            System.out.println("\t[1] Open a Question");
            System.out.println("\t[2] Next page");
            System.out.println("\t[3] Previous page");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int browseQuestionsMenu() {
        int choice;
        System.out.println("######################################## BROWSE QUESTION MENU ######################################");
        do {
            System.out.println("\t[1] Open a Question");
            System.out.println("\t[2] Next page");
            System.out.println("\t[3] Previous page");
            System.out.println("\t[4] Search question by topic");
            System.out.println("\t[5] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 5);
        if (choice == 5)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInAnswersPage() {
        int choice;
        System.out.println("####################################### BROWSE ANSWER MENU #########################################");
        do {
            System.out.println("\t[1] Select an answer");
            System.out.println("\t[2] Next Page");
            System.out.println("\t[3] Previous Page");
            System.out.println("\t[4] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 4);
        if (choice == 4)
            System.out.println("############################################### EXIT ###############################################");

        return choice;
    }

    public int menuInCompleteAnswer() {
        int choice;
        System.out.println("########################################### ANSWER MENU ##########################################");
        do {
            System.out.println("\t[1] Upvote answer");
            System.out.println("\t[2] Downvote answer");
            System.out.println("\t[3] Report answer");
            System.out.println("\t[4] Modify answer");
            System.out.println("\t[5] Accept answer");
            System.out.println("\t[6] Delete answer");
            System.out.println("\t[7] View user profile");
            System.out.println("\t[8] Exit");
            System.out.println("Input: ");
            choice = Integer.parseInt(scanner.nextLine());
        } while (choice < 1 || choice > 8);
        if (choice == 8)
            System.out.println("############################################### EXIT ###############################################");
        return choice;
    }

    public void modifyAnswer(AnswerModifyDTO answerDTO) { // TODO TESTARE
        System.out.println("########################################### MODIFY ANSWER ##########################################");
        System.out.println("(Press ENTER if you don't want to modify the body)");
        System.out.println("* Body (Press ENTER 2 times to end writing the body): ");
        String body = "";
        String text = "";
        do {
            text = scanner.nextLine();
            body += (!text.equals("")) ? (text + '\n') : "";
        } while(!text.equals(""));
        if (!body.equals("")) {
            answerDTO.setBody(body);
        }
        System.out.println("####################################################################################################");
    }

    public void modifyQuestion(QuestionModifyDTO questionDTO) {
        System.out.println("########################################## MODIFY QUESTION #########################################");
        System.out.println("(Press ENTER if you don't want to modify the field)");
        System.out.println("* Title: ");
        String title = scanner.nextLine();
        if (!title.equals("")) {
            questionDTO.setTitle(title);
        }
        System.out.println("* Body (Press ENTER 2 times to end writing the body): ");
        String body = "";
        String text = "";
        do {
            text = scanner.nextLine();
            body += (!text.equals("")) ? (text + '\n') : "";
        } while(!text.equals(""));
        if (!body.equals("")) {
            questionDTO.setBody(body);
        }
        String topic;
        System.out.println("Possible Topics: " + Arrays.toString(Constants.TOPICS.toArray()));
        do {
            System.out.println("* Topic: ");
            topic = scanner.nextLine();
        } while (!Constants.TOPICS.contains(topic) && !topic.equals(""));
        if (!topic.equals("")) {
            questionDTO.setTopic(topic);
        }
    }

    public void createAnswer(AnswerDTO answerDTO) {
        System.out.println("############################################# ADD ANSWER ###########################################");
        System.out.println("* Body (Press ENTER 2 times to end writing the body): ");
        String body = "";
        String text = "";
        do {
            text = scanner.nextLine();
            body += (!text.equals("")) ? (text + '\n') : "";
        } while(!text.equals(""));
        answerDTO.setBody(body);
    }

    public void createQuestion(QuestionPageDTO questionPageDTO){ // TODO TESTARE
        System.out.println("########################################## ADD QUESTION #########################################");
        System.out.println("* Title: ");
        questionPageDTO.setTitle(scanner.nextLine());
        System.out.println("* Body (Press ENTER 2 times to end writing the body): ");
        String body = "";
        String text = "";
        do {
            text = scanner.nextLine();
            body += (!text.equals("")) ? (text + '\n') : "";
        } while(!text.equals(""));
        questionPageDTO.setBody(body);
        System.out.println("Possible topics: " + Arrays.toString(Constants.TOPICS.toArray()));
        String topic;
        do {
            System.out.println("* Topic: ");
            topic = scanner.nextLine();
        } while (!Constants.TOPICS.contains(topic));
        questionPageDTO.setTopic(topic);
    }
}
