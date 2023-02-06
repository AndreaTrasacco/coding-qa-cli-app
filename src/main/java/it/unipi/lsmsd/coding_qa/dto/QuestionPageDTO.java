package it.unipi.lsmsd.coding_qa.dto;

import it.unipi.lsmsd.coding_qa.model.Answer;

import java.util.Date;
import java.util.List;

public class QuestionPageDTO {
    private String id;
    private String title;
    private String body;
    private String topic;
    private String author;
    private PageDTO<Answer> answers;
    private Date createdDate;

    public QuestionPageDTO(){

    }
    public QuestionPageDTO(String id, String title, String body, String topic, String author, PageDTO<Answer> answers, Date createdDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.topic = topic;
        this.author = author;
        this.answers = answers;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public PageDTO<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(PageDTO<Answer> answers) {
        this.answers = answers;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
