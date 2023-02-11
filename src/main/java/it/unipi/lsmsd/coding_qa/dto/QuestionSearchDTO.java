package it.unipi.lsmsd.coding_qa.dto;

import it.unipi.lsmsd.coding_qa.utils.Constants;

public class QuestionSearchDTO {
    private String text;
    private String topic;

    public QuestionSearchDTO() {
    }

    public QuestionSearchDTO(String text, String topic){
        this.text = text;
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTopic() {
        return topic;
    }

    public boolean setTopic(String topic) {
        if(Constants.TOPICS.contains(topic)){
            this.topic = topic;
            return true;
        }
        return false;
    }
}
