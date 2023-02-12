package it.unipi.lsmsd.coding_qa.dto.aggregations;

import javafx.util.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ExperienceLevelDTO {

    private String country;
    private List<LevelDTO> levels;

    public ExperienceLevelDTO(String country, List<LevelDTO> levels) {
        this.country = country;
        setLevels(levels);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<LevelDTO> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelDTO> levels) {
        Collections.sort(levels, new Comparator<LevelDTO>() {
            @Override
            public int compare(LevelDTO lhs, LevelDTO rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return (lhs.getValue() > rhs.getValue()) ? -1 : (lhs.getValue() < rhs.getValue()) ? 1 : 0;
            }
        });
        this.levels = levels;
    }

    @Override
    public String toString() {
        return "\t* Country: " + country +
                "\n\t* Experience levels: \t" + levels +
                "\n****************************************************************************************************";
    }
}
