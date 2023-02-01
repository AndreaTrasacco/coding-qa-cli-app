package it.unipi.lsmsd.coding_qa.dto.aggregations;

import java.util.List;

public class ExperienceLevelDTO {

    //DTO needed for the analytics: SHOW % OF EXPERIENCE LEVELS FOR EACH COUNTRY

    private String country;
    private List<Float> percentages;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Float> getPercentages() {
        return percentages;
    }

    public void setPercentages(List<Float> percentages) {
        this.percentages = percentages;
    }
}
