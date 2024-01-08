package com.example.SS2_Backend.model.StableMatching.Requirement;

import lombok.Getter;

@Getter
public class ScaleTargetRequirement extends Requirement{
    private final int TargetValue;
    public ScaleTargetRequirement(int targetValue) {
        super(0);
        TargetValue = targetValue;
    }
    @Override
    public String toString() {
        return "[" + this.TargetValue + "]";
    }
}
