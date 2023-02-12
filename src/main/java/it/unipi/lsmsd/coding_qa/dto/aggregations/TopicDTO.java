package it.unipi.lsmsd.coding_qa.dto.aggregations;

public class TopicDTO {
    private String topic;
    private int count;

    public TopicDTO(String topic, int count) {
        this.topic = topic;
        this.count = count;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "\t* Topic: " + topic +
                "\n\t* Answers count: " + count +
                "\n****************************************************************************************************";
    }
}
