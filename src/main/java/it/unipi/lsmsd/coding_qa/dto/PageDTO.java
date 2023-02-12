package it.unipi.lsmsd.coding_qa.dto;

import java.util.*;

public class PageDTO<T> {

    List<T> entries = new ArrayList<>();
    int counter = 0;

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(List<T> entries) {
        this.entries = entries;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        String output = "";
        if (counter == 0)
            output = "*********************************************** EMPTY **********************************************";
        else {
            for (int i = 1; i <= counter; i++)
                output += "\n************************************************ " + i + " ***********************************************\n" + entries.get(i - 1);
        }
        return output;
    }
}
