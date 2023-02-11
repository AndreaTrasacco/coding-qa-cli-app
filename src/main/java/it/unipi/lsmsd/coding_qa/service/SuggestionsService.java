package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.PageDTO;
import it.unipi.lsmsd.coding_qa.dto.QuestionDTO;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

public interface SuggestionsService {
    PageDTO<QuestionDTO> questionsToReadSuggestions(int page, String nickname) throws BusinessException;
    PageDTO<QuestionDTO> questionsToAnswerSuggestions(int page, String nickname) throws BusinessException;

}
