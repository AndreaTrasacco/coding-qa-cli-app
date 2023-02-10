package it.unipi.lsmsd.coding_qa.view;

import it.unipi.lsmsd.coding_qa.dto.QuestionSearchDTO;

import java.util.Scanner;

public class QuestionView {
    private Scanner scanner = new Scanner(System.in);

    public QuestionSearchDTO search(){ // TODO
        // Input topic e testo da ricercare
        return null;
    }

    public int menuInQuestionPage(){ // TODO
        // 2 menu (uno solo domanda e uno sulle risposte nella pagina) IN METODI DIVERSI
        // primo menu : rispodi | report | browse answers | ..
        // secondo menu dopo aver aperto riposte : vote | report | next page | previous page ATTENZIONE


        // ATTENZIONE : ALCUNI NON VANNO BENE SE NON LOGGED --> METTERE IN UNA CLASSE DIVERSA?? O IN METODI DIVERSI
        // Report
        // Vota una risposta
        // Pagina successiva/Precedente
        // RISPONDI ALLA DOMANDA
        // If question is mine --> Accept an answer
        // If question is mine --> Delete question
        return 0;
    }

    public int menuInVoteAnswer(){
        // 1) Vote up
        // 2) Vote down
        return 0;
    }
}
