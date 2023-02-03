package it.unipi.lsmsd.coding_qa.dto;

public class QuestionNodeDTO {
    private String id;
    private String title;
    private boolean type; // true: created, false: answered

    public QuestionNodeDTO(String id, String title, boolean type) {
        this.id = id;
        this.title = title;
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

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
