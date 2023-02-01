package it.unipi.lsmsd.coding_qa.model;

import java.util.Date;
import java.util.List;

public class Question {
    // array of answers
    private String id;
    private String title;
    private String body;
    private String topic;
    private String author;
    private List<Answer> answers; // meglio utilizzare una lista?
    private boolean closed; // mettere nel modello?
    private Date createdDate;

    private boolean reported;

    public Question(String id, String title, String body, String topic, String author,
                    List<Answer> answers, boolean closed, Date createdDate, boolean reported){
        this.id = id;
        this.title = title;
        this.body = body;
        this.topic = topic;
        this.author = author;
        this.answers = answers;
        this.closed = closed;
        this.createdDate = createdDate;
        this.reported = reported;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
