package it.unipi.lsmsd.coding_qa.model;

import java.util.Date;
import java.util.List;

public class Answer {
    private String id;
    private String body;
    private Date createdDate;
    private String author;
    private int score;
    private List<String> voters;
    private boolean accepted;
    private boolean reported;

    public Answer(Answer answer){
        this.id = answer.id;
        this.body = answer.body;
        this.createdDate = answer.createdDate;
        this.author = answer.author;
        this.score = answer.score;
        this.voters = answer.voters;
        this.accepted = answer.accepted;
        this.reported = answer.reported;
    }
    public Answer(String id, String body, Date createdDate, String author, int score, List<String> voters, boolean accepted, boolean reported) {
        this.id = id;
        this.body = body;
        this.createdDate = createdDate;
        this.author = author;
        this.score = score;
        this.voters = voters;
        this.accepted = accepted;
        this.reported = reported;
    }

    public String getId() {
        return id;
    }

    /*public String getParentQuestionId(){ // TODO ELIMINARE, NEL MODELLO QUESTO NON VA BENE (E' UNA COSA DI MONGODB)
        return this.id.substring(0, this.id.indexOf('_'));
    }*/

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<String> getVoters() {
        return voters;
    }

    public void setVoters(List<String> voters) {
        this.voters = voters;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }
}
