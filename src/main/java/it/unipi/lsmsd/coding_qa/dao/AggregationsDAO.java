package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dao.exception.DAOException;
import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;

import java.util.List;

public interface AggregationsDAO {
    List<ExperienceLevelDTO> getExperienceLvlPerCountry() throws DAOException;
    List<QuestionScoreDTO> getUsefulQuestions() throws DAOException;
    List<TopicDTO> getTopicRank() throws DAOException;
}
