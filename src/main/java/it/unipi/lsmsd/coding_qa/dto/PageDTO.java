package it.unipi.lsmsd.coding_qa.dto;

import java.util.List;

public class PageDTO <T>{

    List<T> entries;
    int counter;

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
}
