package com.example.SS2_Backend.model.StableMatching;

import com.example.SS2_Backend.model.StableMatching.Requirement.Requirement;
import lombok.Getter;
import lombok.Setter;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.variable.EncodingUtils;

import java.util.*;

import static com.example.SS2_Backend.util.StringExpressionEvaluator.eval;
import static com.example.SS2_Backend.util.Utils.formatDouble;

/**
 * Base case of Stable Matching Problem (One to One) : Number of Individuals in two sets are Equal (n1 = n2)
 * : Every Individual inside the Population have equal number of Properties
 * : Every Individual inside the Population have the same way to evaluate Partner
 * Wish to test this Class? Run "src.main.java.com.example.SS2_Backend.util.SampleDataGenerator.java"
 * <p>
 * Problems viewing code? press: Ctrl + Alt + L (Windows)
 **/

public class StableMatchingProblem implements Problem {
	private ArrayList<Individual> Individuals; // Storing Data of the Whole population
	@Getter
	private int numberOfSets;
	@Getter
	private int numberOfIndividual;
	@Getter
	private int numberOfIndividualOfSet0;
	@Getter
	private int numberOfProperties;
	private String[] PropertiesName;
	@Getter
	@Setter
	private String evaluateFunctionForSet1;
	@Getter
	@Setter
	private String evaluateFunctionForSet2;
	@Getter
	private List<PreferenceList> preferenceLists; // Preference List of each Individual
	@Getter
	private String fitnessFunction; // Evaluate total Score of each Solution set
	@Setter
	@Getter
	private String algorithm;

	/**
	 * MOEA Problem Implementations
	 */
	//No Args Constructor & With Args Constructor
	public StableMatchingProblem() {
	}

	public StableMatchingProblem(ArrayList<Individual> Individuals, String[] PropertiesName, String fitnessFunction) {
		this.Individuals = Individuals;
		this.numberOfIndividual = Individuals.size();
		this.numberOfProperties = Individuals.get(0).getNumberOfProperties();
		this.PropertiesName = PropertiesName;
		this.fitnessFunction = fitnessFunction;
		this.preferenceLists = getPreferences(); // Construct Preference List based on the given above Individuals data
	}

	//Solution Definition
	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, 1);
		// Randomize the order (from 0 to this.NumberOfIndividual)
		solution.setVariable(0, EncodingUtils.newPermutation(this.numberOfIndividual));
		return solution;
	}

	// Evaluate
	public void evaluate(Solution solution) {
		Matches result = StableMatchingExtra(solution.getVariable(0));
		//System.out.println(solution.getVariable(1).toString());
		double fitnessScore = 0;
		Matches result1 = new Matches();
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				if (Individuals.get(i).getIndividualSet() == 0) {
					MatchSet temp = result.getSet(i);
					result1.add(temp);
				}
			}
		}
		String fnf = this.fitnessFunction.toUpperCase();
		if (!fnf.contains("S")) {
			assert result != null;
			fitnessScore = defaultEvaluation(result);
		}else{
			fitnessScore = withFunctionEvaluation(result, fnf);
		}
		solution.setAttribute("matches", result1);
		solution.setObjective(0, -fitnessScore);
	}


	@Override
	public String getName() {
		return "Two Sided Stable Matching Problem";
	}

	public int getNumberOfVariables() {
		return 1;
	}

	@Override
	public int getNumberOfObjectives() {
		return 1;
	}

	@Override
	public int getNumberOfConstraints() {
		return 1;
	}

	public void close() {
	}

	/**
	 * Extra Methods for  Stable Matching Problem
	 */

	/*
	 * Evaluate Methods
	 */
	private double defaultEvaluation(Matches matches) {
		double fitnessScore = 0.0;
		for (int i = 0; i < matches.size(); i++) {
			fitnessScore += calculateSetSatisfactory(matches.getSet(i));
		}
		return fitnessScore;
	}
	/*
	 * Fitness Function Grammar:
	 * $: i - index of MatchSet in "matches"
	 * $: set - value (1 or 2) represent set 1 (0) or set 2 (1)
	 * $: S(set) - Sum of all payoff scores of "set" evaluate by opposite set
	 */
	private double withFunctionEvaluation(Matches matches, String fitnessFunction) {
		StringBuilder tmpSB = new StringBuilder();
		for (int c = 0; c < fitnessFunction.length(); c++) {
			char ch = fitnessFunction.charAt(c);
			if (ch == 'S') {
				// Check for F(index) pattern
				if (c+ 3 < fitnessFunction.length() && fitnessFunction.charAt(c+1) ==  '(' && fitnessFunction.charAt(c + 3) == ')') {
					if(isNumericValue(fitnessFunction.charAt(c+2))) {
						int set = Character.getNumericValue(fitnessFunction.charAt(c + 2));
						//Calculate SUM
						tmpSB.append(calculateSetSatisfactoryOfASet(matches, set));
					}
				}
				c += 4;
			} else {
				//No occurrence of W/w/P/w
				tmpSB.append(ch);
			}
		}
		return eval(tmpSB.toString());
	}


	public PreferenceList getPreferenceOfIndividual(int index) {
		PreferenceList a = new PreferenceList();
		// get this Individual set belong to
		int set = Individuals.get(index).getIndividualSet();
		String evaluateFunction;
		if (set == 0) {
			evaluateFunction = this.evaluateFunctionForSet1;
		} else {
			evaluateFunction = this.evaluateFunctionForSet2;
		}
		evaluateFunction = evaluateFunction.toUpperCase();
		if (!evaluateFunction.contains("P")) {
			a = defaultPreferCalculation(index, set);
		} else {
			a = calculatePreferWithFunction(index, set, evaluateFunction);
		}
		// Sort: Individuals with higher score than others sit on the top of the List
		a.sort();
		// return Sorted list
		return a;
	}

	private PreferenceList defaultPreferCalculation(int index, int set) {
		PreferenceList a = new PreferenceList();
		for (int i = 0; i < numberOfIndividual; i++) {
			if (Individuals.get(i).getIndividualSet() != set) {
				double totalScore = 0;
				for (int j = 0; j < numberOfProperties; j++) {
					Double PropertyValue = Individuals.get(i).getPropertyValue(j);
					Requirement requirement = Individuals.get(index).getRequirement(j);
					int PropertyWeight = Individuals.get(index).getPropertyWeight(j);
					totalScore += (getScale(requirement, PropertyValue) * PropertyWeight);
				}
				// Add
				a.add(new PreferenceList.IndexValue(i, totalScore));
			}
		}
		return a;
	}

	private double getScale(Requirement requirement, double PropertyValue) {
		int type = requirement.getType();
		// Case: Scale
		if (type == 0) {
			int TargetValue = requirement.getTargetValue();
			if (PropertyValue < 0 || PropertyValue > 0) {
				return 0.0;
			} else {
				if (TargetValue != 0.0) {
					double Distance = Math.abs(PropertyValue - TargetValue);
					return (TargetValue - Distance) / TargetValue + 1;
				} else {
					return 0.0;
				}
			}
			//Case: 1 Bound
		} else if (type == 1) {
			Double Bound = requirement.getBound();
			String expression = requirement.getExpression();
			if (Objects.equals(expression, "++")) {
				if (PropertyValue < Bound) {
					return 0.0;
				} else {
					Double distance = Math.abs(PropertyValue - Bound);
					return (Bound + distance) / Bound;
				}
			} else {
				if (PropertyValue > Bound) {
					return 0.0;
				} else {
					Double distance = Math.abs(PropertyValue - Bound);
					return (Bound + distance) / Bound;
				}
			}
			//Case: 2 Bounds
		} else {
			Double lowerBound = requirement.getLowerBound();
			Double upperBound = requirement.getUpperBound();
			if (PropertyValue < lowerBound || PropertyValue > upperBound) {
				double medium = (lowerBound + upperBound) / 2;
				double distance = Math.abs(PropertyValue - medium);
				return (medium - distance) / medium + 1;
			}
		}
		return 0.0;
	}

	private PreferenceList calculatePreferWithFunction(int index, int set, String function) {
		PreferenceList a = new PreferenceList();
		for (int i = 0; i < numberOfIndividual; i++) {
			if (Individuals.get(i).getIndividualSet() != set) {
				StringBuilder tmpSB = new StringBuilder();
				for (int c = 0; c < function.length(); c++) {
					char ch = function.charAt(c);
					if (ch == 'P' || ch == 'p') {
						// read next char then parse to int (index)
						int ssLength = SubstringLength(function, c);
						int indexOfP = Integer.parseInt(function.substring(c + 1, c + 1 + ssLength)) - 1;
						double PropertyValue = Individuals.get(i).getPropertyValue(indexOfP);
						Requirement requirement = Individuals.get(index).getRequirement(indexOfP);
						double Scale = getScale(requirement, PropertyValue);
						tmpSB.append(Scale);
						c += ssLength;
					} else if (ch == 'W' || ch == 'w') {
						//read next char then parse to int (index)
						int ssLength = SubstringLength(function, c);
						int indexOfW = Integer.parseInt(function.substring(c + 1, c + 1 + ssLength)) - 1;
						int weight = Individuals.get(index).getPropertyWeight(indexOfW);
						tmpSB.append(weight);
						c += ssLength;
					} else {
						//No occurrence of W/w/P/w
						tmpSB.append(ch);
					}
				}
				double totalScore = eval(tmpSB.toString());
				// Add
				a.add(new PreferenceList.IndexValue(i, totalScore));
			}
		}
		return a;
	}

//    private static double testFunction(String function, List<Double> nums, List<Integer> weights){
//        StringBuilder tmpSB = new StringBuilder();
//        for(int c = 0; c < function.length(); c++){
//            char ch = function.charAt(c);
//            if(ch == 'P' || ch == 'p'){
//                // read next char then parse to int (index)
//                int ssLength = SubstringLength(function, c);
//                int indexOfP = Integer.parseInt(function.substring(c+1, c+1+ssLength));
//                double PropertyValue = nums.get(indexOfP-1);
//                tmpSB.append(PropertyValue);
//                c += ssLength;
//            }else if(ch == 'W' || ch == 'w'){
//                //read next char then parse to int (index)
//                int ssLength = SubstringLength(function, c);
//                int indexOfW = Integer.parseInt(function.substring(c+1, c+1+ssLength));
//                int weight = weights.get(indexOfW-1);
//                tmpSB.append(weight);
//                c+= ssLength;
//            }else{
//                //No occurrence of W/w/P/w
//                tmpSB.append(ch);
//            }
//        }
//        System.out.println(tmpSB);
//	    return eval(tmpSB.toString());
//    }

	private static int SubstringLength(String function, int startIndex) {
		int length = 0;
		for (int c = startIndex + 1; c < function.length(); c++) {
			char ch = function.charAt(c);
			if (isNumericValue(ch)) {
				length++;
			} else {
				return length;
			}
		}
		return length;
	}

	public static boolean isNumericValue(char c) {
		return c >= '0' && c <= '9';
	}

	// Add to a complete List
	public List<PreferenceList> getPreferences() {
		List<PreferenceList> fullList = new ArrayList<>();
		for (int i = 0; i < numberOfIndividual; i++) {
			//System.out.println("Adding preference for Individual " + i );
			PreferenceList a = getPreferenceOfIndividual(i);
			//System.out.println(a.toString());
			fullList.add(a);
		}
		return fullList;
	}

	private Matches StableMatchingExtra(Variable var) {
		//Parse Variable
		Matches matches = new Matches();
		Queue<Integer> UnMatchedNode = new LinkedList<>();
		List<Integer> MatchedNode = new LinkedList<>();

		String s = var.toString();

		String[] decodedSolution = s.split(",");
		for (int i = 0; i < decodedSolution.length; i++) {
			matches.add(new MatchSet(i, getCapacityOfIndividual(i)));
		}
		for (String token : decodedSolution) {
			try {
				// Convert each token to an Integer and add it to the queue
				int i = Integer.parseInt(token);
				UnMatchedNode.add(i);
			} catch (NumberFormatException e) {
				// Handle invalid tokens (non-integer values)
				System.err.println("Skipping invalid token: " + token);
				return null;
			}
		}
		while (!UnMatchedNode.isEmpty()) {
			//System.out.println(matches);
			//System.out.println(UnMatchedNode);
			int Node = UnMatchedNode.poll();
			if (MatchedNode.contains(Node)) {
				continue;
			}
			//System.out.println("working on Node:" + Node);
			//Get pref List of LeftNode
			PreferenceList NodePreference = preferenceLists.get(Node);
			//Loop through LeftNode's preference list to find a Match
			for (int i = 0; i < NodePreference.size(); i++) {
				//Next Match (RightNode) is found on the list
				int preferNode = NodePreference.getByIndex(i).getIndividualIndex();
				//System.out.println(Node + " Prefer : " + preferNode);
				if (matches.alreadyMatch(preferNode, Node)) {
					//System.out.println(Node + " is already match with " + preferNode);
					break;
				}
				//If the RightNode Capacity is not full -> create connection between LeftNode - RightNode
				if (!matches.isFull(preferNode)) {
					//System.out.println(preferNode + " is not full.");
					//AddMatch (Node, NodeToConnect)
					matches.addMatch(preferNode, Node);
					matches.addMatch(Node, preferNode);
					MatchedNode.add(preferNode);
					break;
				} else {
					//If the RightNode's Capacity is Full then Left Node will Compete with Nodes that are inside RightNode
					//Loser will be the return value
					//System.out.println(preferNode + " is full! Begin making a Compete game involve: " + Node + " ..." );
					int Loser = Compete(preferNode, Node, matches.getIndividualMatches(preferNode));
					//If RightNode is the LastChoice of Loser -> then
					// Loser will be terminated and Saved in Matches.LeftOvers Container
					//System.out.println("Found Loser: " + Loser);
					if (Loser == Node) {
						if (LastChoice(Node) == preferNode) {
							System.out.println(Node + " has no where to go. Go to LeftOvers!");
							matches.addLeftOver(Loser);
							break;
						}
						//Or else Loser go back to UnMatched Queue & Waiting for it's Matching Procedure
					} else {
						matches.disMatch(preferNode, Loser);
						matches.disMatch(Loser, preferNode);
						UnMatchedNode.add(Loser);
						MatchedNode.remove((Integer) Loser);
						//System.out.println(Loser + " lost the game, waiting for another chance.");
						matches.addMatch(preferNode, Node);
						matches.addMatch(Node, preferNode);
						MatchedNode.add(Node);
						//System.out.println(Node + " is more suitable than " + Loser + " matched with " + preferNode);
						break;
					}
				}
			}
		}
		return matches;
	}

	private int getCapacityOfIndividual(int target) {
		return Individuals.get(target).getCapacity();
	}

	// Stable Matching Algorithm Component: isPreferredOver
	private boolean isPreferredOver(int male1, int male2, int female) {
		PreferenceList preference = preferenceLists.get(female);
		for (int i = 0; i < preference.size(); i++) {
			if (preference.getByIndex(i).getIndividualIndex() == male1) {
				return true;
			} else if (preference.getByIndex(i).getIndividualIndex() == male2) {
				return false;
			}
		}
		return false;
	}

	// return true if TargetNode is the last choice of Loser
	private int LastChoice(int loser) {
		PreferenceList pref = preferenceLists.get(loser);
		return pref.getByIndex(pref.size() - 1).getIndividualIndex();
	}

	public int Compete(int Judge, int newPlayer, List<Integer> oldPlayers) {
		PreferenceList JudgeScore = preferenceLists.get(Judge);
		if (Individuals.get(Judge).getCapacity() == 1) {
			if (isPreferredOver(newPlayer, oldPlayers.get(0), Judge)) {
				return oldPlayers.get(0);
			} else {
				return newPlayer;
			}
		} else {
			PreferenceList Game = new PreferenceList();
			// The issue lies here - Index out of Bound
			Game.add(JudgeScore.getByKey(newPlayer));
			// The issue lies here - Index out of Bound
			for (int i = 0; i < oldPlayers.size(); i++) {
				Game.add(JudgeScore.getByKey(oldPlayers.get(i)));
			}
			Game.sort();
			// Return Loser
			return Game.getByIndex(Game.size() - 1).getIndividualIndex();
		}
	}

	// Calculate each pair Satisfactory of the result produced By Stable Matching Algorithm
//    private double calculatePairSatisfactory(MatchItem pair) {
//        // a = 0 - b = 11
//        int a = pair.getIndividual1Index();
//        int b = pair.getIndividual2Index();
//        // len > 6
//        PreferenceList ofA = preferenceLists.get(a);
//        // len <= 6
//        PreferenceList ofB = preferenceLists.get(b);
//        double aScore = 0.0;
//        double bScore = 0.0;
//        aScore += ofB.getByKey(a).getValue();
//        bScore += ofA.getByKey(b).getValue();
//        return aScore + bScore;
//    }
	private double calculateSetSatisfactory(MatchSet matchSet) {
		int a = matchSet.getIndividualIndex();
		PreferenceList ofInd = preferenceLists.get(a);
		List<Integer> list = matchSet.getIndividualMatches();
		double setScore = 0.0;
		for (int i = 0; i < list.size(); i++) {
			int x = list.get(i);
			setScore += ofInd.getByKey(x).getValue();
		}
		return setScore;
	}
	private  double calculateSetSatisfactoryOfASet(Matches matches, int set){
		double totalScore = 0.0;
		if(set == 0){
			for(int i = 0; i < this.numberOfIndividualOfSet0; i++){
				int a = matches.getSet(i).getIndividualIndex();
				List<Integer> list = matches.getSet(i).getIndividualMatches();
				PreferenceList ofInd = preferenceLists.get(a);
				double setScore = 0.0;
				for (int x : list) {
					setScore += ofInd.getByKey(x).getValue();
				}
				totalScore += setScore;
			}
		}else{
			for(int i = this.numberOfIndividualOfSet0; i < numberOfIndividual; i++){
				int a = matches.getSet(i).getIndividualIndex();
				List<Integer> list = matches.getSet(i).getIndividualMatches();
				PreferenceList ofInd = preferenceLists.get(a);
				double setScore = 0.0;
				for (int x : list) {
					setScore += ofInd.getByKey(x).getValue();
				}
				totalScore += setScore;
			}
		}
		return totalScore;
	}

	private String getPropertyNameOfIndex(int index) {
		return PropertiesName[index];
	}

	public Double getPropertyValueOf(int index, int jndex) {
		return Individuals.get(index).getPropertyValue(jndex);
	}

	public int getPropertyWeightOf(int index, int jndex) {
		return Individuals.get(index).getPropertyWeight(jndex);
	}

	private static String fillWithChar(char character, int width) {
		String format = "%" + width + "s";
		return String.format(format, "").replace(' ', character);
	}

	public void printIndividuals() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.numberOfProperties; i++) {
			sb.append(String.format("%-16s| ", this.getPropertyNameOfIndex(i)));
		}
		String propName = sb.toString();
		sb.delete(0, sb.length());
		//header
		System.out.println("No | Set | Name                | " + propName);
		int width = this.numberOfProperties * 18 + 32;
		String filledString = fillWithChar('-', width);
		sb.append(filledString).append("\n");
		//content
		for (int i = 0; i < this.numberOfIndividual; i++) {
			//name / set
			sb.append(String.format("%-3d| ", i));
			sb.append(String.format("%-4d| ", Individuals.get(i).getIndividualSet()));
			sb.append(String.format("%-20s| ", Individuals.get(i).getIndividualName()));
			// prop value
			StringBuilder ss = new StringBuilder();
			for (int j = 0; j < this.numberOfProperties; j++) {
				ss.append(String.format("%-16s| ", formatDouble(this.getPropertyValueOf(i, j))));
			}
			sb.append(ss).append("\n");
			ss.delete(0, sb.length());
			ss.append(String.format("%33s", "Requirement: | "));
			for (int j = 0; j < this.numberOfProperties; j++) {
				ss.append(String.format("%-16s| ", this.Individuals.get(i).getRequirement(j).toString()));
			}
			sb.append(ss).append("\n");
			ss.delete(0, sb.length());
			ss.append(String.format("%33s", "Weight: | "));
			for (int j = 0; j < this.numberOfProperties; j++) {
				ss.append(String.format("%-16s| ", this.getPropertyWeightOf(i, j)));
			}
			sb.append(ss).append("\n");
		}
		sb.append(filledString).append("\n");
		System.out.print(sb);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Individual individual : Individuals) {
			sb.append(individual.toString()).append("\n");
		}
		return numberOfProperties + "\n" + fitnessFunction + "\n" + sb;
	}

	public String printPreferenceLists() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < preferenceLists.size(); i++) {
			sb.append("Individual ").append(i).append(" : ");
			sb.append(preferenceLists.get(i).toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public void setFitnessFunction(String fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	public void setPopulation(ArrayList<Individual> individuals) {
		this.Individuals = individuals;
		this.numberOfIndividual = Individuals.size();
		this.numberOfIndividualOfSet0 = getNumberOfSet0();
		this.numberOfProperties = Individuals.get(0).getNumberOfProperties();
		this.preferenceLists = getPreferences();
	}

	public int getNumberOfSet0(){
		int c = 0;
		for(int i = 0; i < this.numberOfIndividual; i++){
			if (Individuals.get(i).getIndividualSet() == 0){
				c++;
			}else{
				break;
			}
		}
		return c;
	}

	public void setAllPropertyNames(String[] allPropertyNames) {
		this.PropertiesName = allPropertyNames;
	}

//    public static void main(String[] args){
//        List<Double> nums = new ArrayList<>();
//        List<Integer> weights = new ArrayList<>();
//        nums.add(1.1);
//        nums.add(9.4);
//        nums.add(13.9);
//        nums.add(2.8);
//        nums.add(4.9);
//        weights.add(9);
//        weights.add(3);
//        weights.add(8);
//        weights.add(4);
//        weights.add(7);
//        String f = "P3*W3+P5*W5+P1*W1+P4*W4+P2^W2";
//        System.out.println(testFunction(f, nums, weights));
////        System.out.println(SubstringLength("P3*W3+P5*W5+P1*W1+P4*W4+P2*W2", 0));
////        System.out.println(f.substring(1, 1+1));
//    }
}
