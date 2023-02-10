package it.unipi.lsmsd.coding_qa.dto;

public class QuestionSearchDTO {
    private String text;
    private String topic;

    public QuestionSearchDTO() {
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

    public void setTopic(String topic) { // TODO FAR RITORNARE BOOLEAN IN MODO DA CONTROLLARE (NEL METODO) SE IL TOPIC E' VALIDO
        this.topic = topic;
    }
}
