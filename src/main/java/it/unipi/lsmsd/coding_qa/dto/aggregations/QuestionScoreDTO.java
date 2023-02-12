package it.unipi.lsmsd.coding_qa.dto.aggregations;

public class QuestionScoreDTO {

    //DTO needed for the analytics: MOST USEFUL QUESTION FOR EACH TOPIC

    private String title;
    private int score;
    private String topic;

    public QuestionScoreDTO(String title, int score, String topic) {
        this.title = title;
        this.score = score;
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "\t* Topic: " + topic +
                "\n\t* Title: " + title +
                "\n\t* Total score: " + score +
                "\n****************************************************************************************************";
    }
}
