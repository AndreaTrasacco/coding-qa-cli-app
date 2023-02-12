package it.unipi.lsmsd.coding_qa.dto.aggregations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class LevelDTO {
    private String level; // beginner | intermediate | advanced
    private double value; // percentage

    public LevelDTO(String level, double value) {
        this.level = level;
        setValue(value);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN);
        this.value = bd.doubleValue();
    }

    @Override
    public String toString(){
        return " { " + level + " : " + value + "% }"; // TODO TESTARE
    }
}
