package it.unipi.lsmsd.coding_qa.dto;

public class VoteDTO {
    private String answerId;
    private boolean voteType;
    private String voterId;
    private String answerOwner; // nickname

    public VoteDTO() {

    }

    public VoteDTO(String answerId, boolean voteType, String voterId, String answerOwner) {
        this.answerId = answerId;
        this.voteType = voteType;
        this.voterId = voterId;
        this.answerOwner = answerOwner;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public boolean getVoteType() {
        return voteType;
    }

    public void setVoteType(boolean voteType) {
        this.voteType = voteType;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getAnswerOwner() {
        return answerOwner;
    }

    public void setAnswerOwner(String answerOwner) {
        this.answerOwner = answerOwner;
    }
}
