package it.unipi.lsmsd.coding_qa.dto;

public class QuestionNodeDTO {
    private String title;
    private boolean type; // true: created, false: answered

    public QuestionNodeDTO(String title, boolean type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
