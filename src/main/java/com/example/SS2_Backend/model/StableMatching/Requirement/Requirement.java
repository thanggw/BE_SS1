package com.example.SS2_Backend.model.StableMatching.Requirement;
import lombok.Getter;
@Getter
public abstract class Requirement {
    private final int Type;
    protected Requirement(int type) {
        this.Type = type;
    }
    public int getTargetValue(){
        return 0;
    }
    public double getBound() {
        return 0;
    }
    public String getExpression() {
        return null;
    }
    public double getUpperBound() {
        return 0;
    }
    public double getLowerBound() {
        return 0;
    }
    public abstract String toString();
}