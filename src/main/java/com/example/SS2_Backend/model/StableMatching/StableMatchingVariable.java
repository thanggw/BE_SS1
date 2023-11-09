package com.example.SS2_Backend.model.StableMatching;

import org.moeaframework.core.Variable;

public class StableMatchingVariable implements Variable {
    private final Matches matches;

    public StableMatchingVariable(Matches matches){
        this.matches = matches;
    }
    public Matches toMatches(){
        return matches;
    }
    public int size(){
        return matches.size();
    }
    public String toString(){
        return matches.toString();
    }

    @Override
    public Variable copy() {
        return new StableMatchingVariable(this.matches);
    }

    @Override
    public void randomize() {

    }

    @Override
    public String encode() {
        return null;
    }

    @Override
    public void decode(String s) {

    }
}
