package it.unipi.lsmsd.coding_qa.service;

import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public interface AggregationsService {
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() throws BusinessException;

    public List<QuestionScoreDTO> getUsefulQuestions() throws BusinessException;

    public List<TopicDTO> getTopicRank() throws BusinessException;
}
