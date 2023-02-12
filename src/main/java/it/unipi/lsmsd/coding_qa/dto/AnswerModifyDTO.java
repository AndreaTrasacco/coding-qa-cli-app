package it.unipi.lsmsd.coding_qa.dto;

public class AnswerModifyDTO {
    String body;

    public AnswerModifyDTO(String body){
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
