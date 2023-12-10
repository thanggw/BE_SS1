package com.example.SS2_Backend.util;

import com.fathzer.soft.javaluator.*;

import java.util.List;
import java.util.NoSuchElementException;

/*
Create a new Evaluator class because who the hell is going to read
a self-built class without sufficient/proper explanation comment for further developing?
 */
public class ExpressionEvaluatorForStableMatching {
	private static double calSum(List<Double> values) {
		return values.stream()
		    .mapToDouble(Double::doubleValue)
		    .sum();
	}

	private static double calProduct(List<Double> values) {
		return values.stream()
		    .reduce(1.0, (a, b) -> a * b);
	}

	private static double calMax(List<Double> values) {
		try {
			return values.stream()
			    .mapToDouble(Double::doubleValue)
			    .max().getAsDouble();
		}catch (NoSuchElementException e){
			throw e;
		}
	}

	private static double calMin(List<Double> values) {
		return values.stream()
		    .mapToDouble(Double::doubleValue)
		    .min().getAsDouble();
	}

	private static double calAverage(List<Double> values) {
		return values.stream()
		    .mapToDouble(Double::doubleValue)
		    .average().getAsDouble();
	}

	private static double calMedian(List<Double> values) {
		double[] arr = values.stream()
		    .mapToDouble(Double::doubleValue)
		    .sorted()
		    .toArray();
		int n = arr.length;
		if (n % 2 == 0) {
			return (arr[n / 2] + arr[n / 2 - 1]) / 2;
		} else {
			return arr[n / 2];
		}
	}

	private static double calRange(List<Double> values) {
		double[] arr = values.stream()
		    .mapToDouble(Double::doubleValue)
		    .sorted()
		    .toArray();
		return arr[arr.length - 1] - arr[0];
	}


	private static double calculateByDefault(List<Double> values, String defaultFunction) {
		StringExpressionEvaluator.DefaultFunction function = StringExpressionEvaluator
		    .DefaultFunction
		    .valueOf(defaultFunction.toUpperCase());
		double val;
		switch (function) {
			case SUM:
				val = calSum(values);
				break;
			case PRODUCT:
				val = calProduct(values);
				break;
			case MAX:
				val = calMax(values);
				break;
			case MIN:
				val = calMin(values);
				break;
			case AVERAGE:
				val = calAverage(values);
				break;
			case MEDIAN:
				val = calMedian(values);
				break;
			case RANGE:
				val = calRange(values);
				break;
			default:
				val = calSum(values);
				break;
		}
		return val;
	}
	public static void main(String[] args) {
		// Example expression: 2^3
		String expression = "2*(3+3)";
		System.out.println(evaluateExpression(expression));
	}

	public static Double evaluateExpression(String expression) {
		return new DoubleEvaluator().evaluate(expression);
	}
}
