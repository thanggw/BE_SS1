package com.example.SS2_Backend.model.StableMatching;

import java.util.ArrayList;
import java.util.List;

public class PreferenceLists {
    private final List<List<Pair>> preferenceLists = new ArrayList<>();

    public PreferenceLists(){
    }

    public List<List<Pair>> getPreferenceList(){
        return this.preferenceLists;
    }

    public int size(){
        return this.preferenceLists.size();
    }

    public boolean isEmpty(){
        return this.preferenceLists.isEmpty();
    }

    public List<Pair> getIndividualPreferenceList(int index){
        return this.preferenceLists.get(index);
    }

    public void add(List<Pair> individualList){
        this.preferenceLists.add(individualList);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.size();i++){
            List<Pair> list = this.getIndividualPreferenceList(i);
            sb.append("Individual ").append(i);
            sb.append(" [");
            for(int j=0;j<list.size();j++) {
                if(j == list.size() - 1){
                    sb.append("C").append(j+1).append(": ");
                    sb.append(list.get(j).getIndividual1Index()).append("\t");
                    sb.append("Tt: ");
                    sb.append(list.get(j).getIndividual2Index()).append(" ");
                    break;
                }
                sb.append("C").append(j+1).append(": ");
                sb.append(list.get(j).getIndividual1Index()).append("\t");
                sb.append("Tt: ");
                sb.append(list.get(j).getIndividual2Index()).append("\t").append("|");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
