package it.unipi.lsmsd.coding_qa.dao;

import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;

import java.util.List;

public interface AggregationsDAO { // TODO ECCEZIONI
    List<ExperienceLevelDTO> getExperienceLvlPerCountry(); // TODO RISCRIVERE METODO --> GROUP BY SU PIU CAMPI VA FATTO DEFINENDO DOCUMENTO
    List<QuestionScoreDTO> getUsefulQuestions();
    List<TopicDTO> getTopicRank();
}
