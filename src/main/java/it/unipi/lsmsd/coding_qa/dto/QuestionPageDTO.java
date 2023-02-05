package it.unipi.lsmsd.coding_qa.dto;

import it.unipi.lsmsd.coding_qa.model.Answer;

import java.util.Date;
import java.util.List;

public class QuestionPageDTO { // TODO COMPLETARE
    private String id;
    private String title;
    private String body;
    private String topic;
    private String author;
    private PageDTO<Answer> answers;
    private Date createdDate;
}
