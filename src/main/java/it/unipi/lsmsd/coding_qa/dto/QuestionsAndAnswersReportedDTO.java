package it.unipi.lsmsd.coding_qa.dto;

import java.util.Date;

public class QuestionsAndAnswersReportedDTO { // TODO ELIMINARE
    private String id;
    private String title;
    private String body;
    private String author;
    private Date createdDate;
    private int type; // 0: Question, 1: Answer

    public QuestionsAndAnswersReportedDTO(String id, String title, String body, String author, Date createdDate, int type) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.createdDate = createdDate;
        this.type = type;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
