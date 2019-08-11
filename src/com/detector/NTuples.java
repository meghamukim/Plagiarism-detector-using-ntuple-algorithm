package com.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NTuples {
    List<String> tuples;

    public NTuples () {
        tuples = new ArrayList<String>();
    }
    public NTuples(List<String> data) {
        tuples = new ArrayList<String>(data);
    }

    public List<String> getTuples() {
        return this.tuples;
    }

    public void add (String string) {
        this.tuples.add(string);
    }

    public void addAll (NTuples tuples) {
        this.tuples.addAll(tuples.tuples);
    }
    public int getTupleSize() {
        return this.tuples.get(0).split("\\W+").length;
    }

}
