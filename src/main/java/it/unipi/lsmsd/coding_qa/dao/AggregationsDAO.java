package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;

import java.util.List;

public interface AggregationsDAO {

    public List<ExperienceLevelDTO> getExperienceLvlPerCountry();

    public List<QuestionScoreDTO> getUsefulQuestions();

    public List<TopicDTO> getTopicRank();
}
