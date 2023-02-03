package it.unipi.lsmsd.coding_qa.service.implementation;

import it.unipi.lsmsd.coding_qa.dao.AggregationsDAO;
import it.unipi.lsmsd.coding_qa.dao.DAOLocator;
import it.unipi.lsmsd.coding_qa.dao.enums.DAORepositoryEnum;
import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
import it.unipi.lsmsd.coding_qa.service.AggregationsService;
import it.unipi.lsmsd.coding_qa.service.exception.BusinessException;

import java.util.List;

public class AggregationsServiceImpl implements AggregationsService {

    private AggregationsDAO aggregationsDAO;

    public AggregationsServiceImpl(){
        aggregationsDAO = DAOLocator.getAggregationDAO(DAORepositoryEnum.MONGODB);
    }

    @Override
    public List<ExperienceLevelDTO> getExperienceLvlPerCountry() throws BusinessException {
        try {
            return aggregationsDAO.getExperienceLvlPerCountry();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public List<QuestionScoreDTO> getUsefulQuestions() throws BusinessException {
        try {
            return aggregationsDAO.getUsefulQuestions();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }

    @Override
    public List<TopicDTO> getTopicRank() throws BusinessException {
        try {
            return aggregationsDAO.getTopicRank();
        } catch (Exception e){
            throw new BusinessException(e);
        }
    }
}
