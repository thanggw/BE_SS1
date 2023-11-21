package com.example.SS2_Backend.model.StableMatching.Requirement;
import lombok.Getter;

//public class Requirement {
//    private int[] value = new int[3];
//
//    public Requirement(int[] value){
//        this.value = value;
//    }
//}
public interface Requirement {
    int getType();
    Double getBound();
    String getExpression();
    Double getLowerBound();
    Double getUpperBound();
    int getTargetValue();
    String toString();
}