package it.unipi.lsmsd.coding_qa.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnswerDTO {
    private String id;
    private String body = "";
    private Date createdDate;
    private String author;
    private int score;
    private List<String> voters;
    private boolean accepted;

    public AnswerDTO(String id, String body, Date createdDate, String author, int score, List<String> voters, boolean accepted) {
        this.id = id;
        this.body = body;
        this.createdDate = createdDate;
        this.author = author;
        this.score = score;
        this.voters = voters;
        this.accepted = accepted;
    }

    public AnswerDTO(String id, String body, Date createdDate, String author, int score, boolean accepted) {
        this(id, body, createdDate, author, score, new ArrayList<>(), accepted);
    }

    public AnswerDTO(String id, String body, Date createdDate, String author, int score) {
        this(id, body, createdDate, author, score, new ArrayList<>(), false);
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public List<String> getVoters() {
        return voters;
    }

    public void setVoters(List<String> voters) {
        this.voters = voters;
    }

    @Override
    public String toString() {
        String acc = "";
        if(accepted)
            acc = "********************************************* ACCEPTED *********************************************\n";
        return acc + "\t* Author: " + author +
                "\n\t* Score: " + score +
                ((body.equals("")) ? "" : "\n\t* Body:\n" + body) +
                "\n\t[" + createdDate + "]\n" +
                "****************************************************************************************************\n";
    }
}
