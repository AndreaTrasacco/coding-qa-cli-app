package it.unipi.lsmsd.coding_qa.dto.aggregations;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public class ExperienceLevelDTO {

    private String country;
    private List<Pair<String, Double>> levels;

    public ExperienceLevelDTO(String country, List<Pair<String, Double>> levels) {
        this.country = country;
        this.levels = levels;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Pair<String, Double>> getLevels() {
        return levels;
    }

    public void setLevels(List<Pair<String, Double>> levels) {
        this.levels = levels;
    }
}
