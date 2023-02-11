package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionSearchDTO;
import it.unipi.lsmsd.coding_qa.service.AnswerService;
import it.unipi.lsmsd.coding_qa.service.QuestionService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.utils.Constants;
import it.unipi.lsmsd.coding_qa.view.AdminView;
import it.unipi.lsmsd.coding_qa.view.MainView;
import it.unipi.lsmsd.coding_qa.view.QuestionView;

public class QuestionController {
    private QuestionService questionService;
    private AnswerService answerService;
    private QuestionView questionView;
    private MainView mainView;
    private AdminView adminView;

    public QuestionController(){
        questionService = ServiceLocator.getQuestionService();
        answerService = ServiceLocator.getAnswerService();
        questionView =  new QuestionView();
        mainView = new MainView();
        adminView = new AdminView();
    }

    // Browse e search for admin, logged, notlogged (VALUTARE SE TROPPO DUPICATO)

    public void browse(int userType) { // 0: Admin, 1: Logged, 2: NonLogged, 3: Owner
        try{
            int page = 1;
            do{
                PageDTO<QuestionDTO> pageDTO = questionService.browseQuestions(page);
                mainView.viewPage(pageDTO);
                switch(questionView.browseQuestionsMenu()){
                    case 1:
                        int index = questionView.specifyQuestionIndex(pageDTO);
                        questionService.getQuestionInfo(pageDTO.getEntries().get(index).getId());
                        switch(userType){
                            case 0: adminView.adminQuestionMenu(); break;
                            case 1: questionView.menuInQuestionPageLogged(); break;
                            case 2: questionView.menuInQuestionPageNonLogged(); break;
                            case 3: questionView.menuInQuestionPageOwner();
                        }

                        break;
                    case 2:
                        if (pageDTO.getCounter() == Constants.PAGE_SIZE)
                            page++;
                        else
                            mainView.showMessage("!!!! THIS IS THE LAST PAGE !!!!");
                        break;
                    case 3:
                        if (page > 1)
                            page--;
                        else
                            mainView.showMessage("!!!! THIS IS THE FIRST PAGE !!!!");
                        break;
                    case 4:
                        return;
                }
            } while(true);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    public void search() {
        try{ // TODO next/previous page (manca un menù in QuestionView?)
            int page = 1;
            QuestionSearchDTO questionSearchDTO = new QuestionSearchDTO();
            questionView.search(questionSearchDTO);
            PageDTO<QuestionDTO> pageDTO = questionService.searchQuestions(1,questionSearchDTO.getText(), questionSearchDTO.getTopic() );
            mainView.viewPage(pageDTO);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }


    // menu se logged oppure no
    // operazioni varie anche tutto in un solo metodo se basta
}
