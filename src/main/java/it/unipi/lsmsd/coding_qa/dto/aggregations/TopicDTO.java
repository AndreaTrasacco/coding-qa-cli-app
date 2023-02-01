package it.unipi.lsmsd.coding_qa.dto.aggregations;

public class TopicDTO {

    //DTO needed for the analytics: RANKING OF THE MOST DISCUSSED TOPIC OF THE WEEK

    private String topicName;
    private int questionCount;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
}
