package com.example.SS2_Backend.model.StableMatching;

import java.util.List;

public interface MatchItem {
    public int getIndividual1Index();
    public int getIndividual2Index();
    public List<Integer> getIndividualMatches();
    public int getCapacity();
    public void addMatch(int target);
    public void unMatch(int target);
    public String toString();
}
