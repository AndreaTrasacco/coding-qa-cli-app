package it.unipi.lsmsd.coding_qa.controller;

import it.unipi.lsmsd.coding_qa.dto.aggregations.ExperienceLevelDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.QuestionScoreDTO;
import it.unipi.lsmsd.coding_qa.dto.aggregations.TopicDTO;
import it.unipi.lsmsd.coding_qa.service.AggregationsService;
import it.unipi.lsmsd.coding_qa.service.ServiceLocator;
import it.unipi.lsmsd.coding_qa.view.AnalyticsView;

import java.util.List;

public class AnalyticsController {
    private static AnalyticsView analyticsView = new AnalyticsView();
    private static AggregationsService aggregationsService = ServiceLocator.getAggregationsService();

    public static void start() {
        try {
            List<QuestionScoreDTO> questionScoreDTOList = null;
            List<ExperienceLevelDTO> experienceLevelDTOList = null;
            List<TopicDTO> topicDTOList = null;
            do {
                switch (analyticsView.menuAnalytics()) {
                    case 1: // Show most useful question for each topic
                        if (questionScoreDTOList == null)
                            questionScoreDTOList = aggregationsService.getUsefulQuestions();
                        analyticsView.showAnalytic(questionScoreDTOList);
                        break;
                    case 2: // Show experience levels for each country
                        if (experienceLevelDTOList == null)
                            experienceLevelDTOList = aggregationsService.getExperienceLvlPerCountry();
                        analyticsView.showAnalytic(experienceLevelDTOList);
                        break;
                    case 3: // Show most discussed topics of the week
                        if (topicDTOList == null)
                            topicDTOList = aggregationsService.getTopicRank();
                        analyticsView.showAnalytic(topicDTOList);
                        break;
                    case 4: // Exit
                        return;
                }
            } while (true);
        } catch (
                Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }
}
