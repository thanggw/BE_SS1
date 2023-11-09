package com.example.SS2_Backend.model.StableMatching;

import java.util.LinkedList;
import java.util.List;

public class Matches {
    private List<Pair> matches = new LinkedList<>();
    public Matches(){
    }

    public void add(Pair match){
        matches.add(match);
    }
    public Pair getPair(int index){
        return matches.get(index);
    }
    public String findCompany(int target){
        String company = null;
        for(int i = 0; i < matches.size(); i++){
            if(matches.get(i).getIndividual1Index() == target){
                company = Integer.toString(matches.get(i).getIndividual2Index());
                break;
            }else if(matches.get(i).getIndividual2Index() == target){
                company = Integer.toString(matches.get(i).getIndividual1Index());
                break;
            }
        }
        return company;
    }

    public int size(){
        return matches.size();
    }

    public void disMatch(int target){
        for(int i = 0; i < matches.size(); i++){
            if(matches.get(i).getIndividual1Index() == target){
                matches.remove(i);
                break;
            }else if(matches.get(i).getIndividual2Index() == target){
                matches.remove(i);
                break;
            }
        }
    }
    public int[] parseArray(){
        int[] a = new int[matches.size()*2];
        for(int i=0; i < a.length;i++){
            a[i] = matches.get(i).getIndividual1Index();
            a[i+1] = matches.get(i).getIndividual2Index();
            i+=2;
        }
        return a;
    }

    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("{");
        for(int i = 0; i < matches.size(); i++){
            s.append("[");
            s.append(matches.get(i).toString());
            s.append("]");
        }
        s.append("}");
        return s.toString();
    }

    public static void main(String[] args){
        Matches matches = new Matches();
        matches.add(new Pair(1,2));
        matches.add(new Pair(3,4));
        matches.add(new Pair(5,6));

        System.out.println(matches.toString());
    }
}
