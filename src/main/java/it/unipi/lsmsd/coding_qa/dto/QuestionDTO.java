package it.unipi.lsmsd.coding_qa.dto;

import java.util.Date;

public class QuestionDTO {

    private String title;
    private Date createdDate;
    private String topic;
    private String author;


    public QuestionDTO(String title, Date createdDate, String topic, String author) {
        this.title = title;
        this.createdDate = createdDate;
        this.topic = topic;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
