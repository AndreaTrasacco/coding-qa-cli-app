package it.unipi.lsmsd.coding_qa.model;

import java.util.Date;

public class Question {
    // array of answers
    private String id;
    private String title;
    private String body;
    private int viewsCount;
    private String topic;
    private String author;
    private Answer[] answers;
    private boolean closed; // mettere nel modello?
    private Date createdDate;

    public Question(String id, String title, String body, int viewsCount, String topic, String author,
                    Answer[] answers, boolean closed, Date createdDate){
        this.id = id;
        this.title = id;
        this.body = body;
        this.viewsCount = viewsCount;
        this.topic = topic;
        this.author = author;
        this.answers = answers;
        this.closed = closed;
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isClosed() {
        return closed;
    }

    public boolean getClosed(){
        return closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
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

    public Answer[] getAnswers() {
        return answers;
    }

    public void setAnswers(Answer[] answers) {
        this.answers = answers;
    }
}
