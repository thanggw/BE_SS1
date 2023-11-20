package com.example.SS2_Backend.model.StableMatching;
import static com.example.SS2_Backend.util.Utils.formatDouble;

import java.util.ArrayList;
import java.util.List;

public class PreferenceLists {
    public static class IndexValue{
        private final int IndividualIndex;
        private final double Value;

        public IndexValue(int IndividualIndex, double Value){
            this.IndividualIndex = IndividualIndex;
            this.Value = Value;
        }
        public int getIndividualIndex() {
            return IndividualIndex;
        }
        public double getValue() {
            return Value;
        }
        public String toString(){
            return "Index: " + IndividualIndex + " Score: " + Value;
        }
    }
    private final List<List<IndexValue>> preferenceLists = new ArrayList<>();

    public PreferenceLists(){
    }

    public List<List<IndexValue>> getPreferenceList(){
        return this.preferenceLists;
    }

    public int size(){
        return this.preferenceLists.size();
    }

    public boolean isEmpty(){
        return this.preferenceLists.isEmpty();
    }

    public List<IndexValue> getIndividualPreferenceList(int index){
        return this.preferenceLists.get(index);
    }

    public void add(List<IndexValue> individualList){
        this.preferenceLists.add(individualList);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<this.size();i++){
            List<IndexValue> list = this.getIndividualPreferenceList(i);
            sb.append("Individual ").append(i);
            sb.append(" [");
            for(int j=0;j<list.size();j++) {
                if(j == list.size() - 1){
                    sb.append("C").append(j+1).append(": ");
                    sb.append(list.get(j).getIndividualIndex()).append("\t");
                    sb.append("Tt: ");
                    sb.append(formatDouble(list.get(j).getValue())).append(" ");
                    break;
                }
                sb.append("C").append(j+1).append(": ");
                sb.append(list.get(j).getIndividualIndex()).append("\t");
                sb.append("Tt: ");
                sb.append(formatDouble(list.get(j).getValue())).append("\t").append("|");
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
