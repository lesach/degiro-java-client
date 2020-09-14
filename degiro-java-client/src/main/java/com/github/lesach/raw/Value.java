package com.github.lesach.raw;

import java.util.List;

public class Value {

    private String name;
    private List<Value_> value = null;
    private boolean isAdded;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Value_> getValue() {
        return value;
    }

    public void setValue(List<Value_> value) {
        this.value = value;
    }

    public boolean isIsAdded() {
        return isAdded;
    }

    public void setIsAdded(boolean isAdded) {
        this.isAdded = isAdded;
    }

}