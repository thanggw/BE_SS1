package com.example.SS2_Backend.model;

public class Pair {
    /*
    kieu du lieu Pair(gia tri 1, gia tri 2)
    Dung cho nhung tinh huong Match (i1, i2)
    / Property(Gia tri, Weight)
    / Male(index, totalCalculatedScore)
    */
    private int individual1Index;
    private int individual2Index;

    public Pair(int individual1Index, int individual2Index){
        this.individual1Index = individual1Index;
        this.individual2Index = individual2Index;
    }

    public int getIndividual1Index() {
        return individual1Index;
    }

    public int getIndividual2Index() {
        return individual2Index;
    }

    public String toString(){
        return "Individual1: " + individual1Index + " Individual2: " + individual2Index;
    }
}
