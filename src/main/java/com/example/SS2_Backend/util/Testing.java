package com.example.SS2_Backend.util;

import com.example.SS2_Backend.model.StableMatching.Matches;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Testing {
	private final Matches matches;
	private int[] capacities;
	public Testing(Matches matches){
		this.matches = matches;
	}
	public void setCapacities(int[] capacities){
		this.capacities = capacities;
	}
	public boolean hasDuplicate(){
		int sz = matches.size();
		for (int i = 0; i < sz; i++) {
			Map<Integer, Integer> freq = new HashMap<>();
			Set<Integer> matchSet = matches.getSet(i);
			Iterator<Integer> IT = matchSet.iterator();
			if(IT.hasNext()){
				int elm = IT.next();
				freq.compute(elm, (K, V) -> V == null ? 1 : V+1);
				if(freq.containsValue(2)) return false;
			}
			System.out.println(freq);
		}
		return true;
	}

}
