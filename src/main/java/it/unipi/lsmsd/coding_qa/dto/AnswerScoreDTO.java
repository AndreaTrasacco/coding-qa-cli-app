package it.unipi.lsmsd.coding_qa.dto;

public class AnswerScoreDTO {
    private String author;
    private int score;

    public AnswerScoreDTO(String author, int score) {
        this.author = author;
        this.score = score;
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
}
