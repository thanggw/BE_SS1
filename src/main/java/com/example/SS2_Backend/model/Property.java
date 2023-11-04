package com.example.SS2_Backend.model;

public class Property {
    private String name;
    private String value;
    private int weight;

    public Property(String name, String value, int weight) {
        this.name = name;
        this.value = value;
        this.weight = weight;
    }
    public String getName(){
        return name;
    }
    public String getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return name + " Value: " + value + ", Weight: " + weight;
    }
}
