package it.unipi.lsmsd.coding_qa.dto.aggregations;

import java.util.List;

public class ExperienceLevelDTO {

    class Level {
        private String expLevel;
        private float percentage;
    }

    private String country;
    private List<Level> levels;

    public ExperienceLevelDTO(String country, List<Level> levels) {
        this.country = country;
        this.levels = levels;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public void setLevels(List<Level> levels) {
        this.levels = levels;
    }
}
