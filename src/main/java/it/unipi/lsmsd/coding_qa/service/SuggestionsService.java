package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.model.Question;
import it.unipi.lsmsd.coding_qa.model.User;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface SuggestionsService {
    PageDTO<QuestionDTO> questionToReadSuggestions(int page, String nickname) throws BusinessException;
    PageDTO<QuestionDTO> questionToAnswerSuggestions(int page, String nickname) throws BusinessException;

}
