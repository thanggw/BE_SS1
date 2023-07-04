package com.example.SS2_Backend.util;


import com.example.SS2_Backend.model.NormalPlayer;
import com.example.SS2_Backend.model.Strategy;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringExpressionEvaluator {


    public enum DefaultFunction {
        SUM,
        AVERAGE,
        MIN,
        MAX,
        PRODUCT,
        MEDIAN,
        RANGE
    }
    static DecimalFormat decimalFormat = new DecimalFormat("#.##############");

    public static double evaluatePayoffFunctionWithRelativeToOtherPlayers(Strategy strategy,
                                                                          String payoffFunction,
                                                                          List<NormalPlayer> normalPlayers,
                                                                          List<Integer> chosenStrategyIndices) {

        String expression = payoffFunction;

        if (payoffFunction.isBlank()) {
            // the payoff function is the the sum function of all properties by default
            return calculateByDefault(strategy.getProperties(), null);
        } else {

            // if there is no relationship in the payoff function, then just evaluate it normally, no need to replace any P placeholder
            if (!payoffFunction.contains("P")) {
                return evaluatePayoffFunctionNoRelative(strategy, expression);
            }

            // replace the placeholder for THIS current player's strategy with the actual value
            // example: payoffFunction is a string formula, e.g: p1 + p2 / p3 - P2p3 with p1, p2, p3 are the properties 1, 2, 3 of the strategy chosen by this player
            for (int i = 0; i < strategy.getProperties().size(); ++i) {
                double propertyValue = strategy.getProperties().get(i);
                String placeholder = String.format("\\bp%d\\b", i + 1);

                expression = expression.replaceAll(placeholder, formatDouble(propertyValue));
            }

            // replace the placeholder for OTHER players' strategies with the actual values
            // example: payoffFunction is a string formula, e.g: p1 + p2 / p3 - P2p3 with P2p3 is the property p of the strategy chosen by the player 2
            for (int i = 0; i < normalPlayers.size(); i++) {
                // example: P1
                NormalPlayer otherPlayer = normalPlayers.get(i);
                Strategy otherPlayerStrategy = otherPlayer.getStrategyAt(chosenStrategyIndices.get(i));

                for (int j = 0; j < otherPlayerStrategy.getProperties().size(); j++) {
                    // example: P1p1
                    String placeholder = String.format("\\bP%dp%d\\b", i + 1, j + 1);
                    Double propertyValue = otherPlayerStrategy.getProperties().get(j);
                    expression = expression.replaceAll(placeholder, formatDouble(propertyValue));
                }
            }



            // evaluate this string expression to get the result
            return eval(expression);

        }


    }



    public static double evaluatePayoffFunctionNoRelative(Strategy strategy,
                                                          String payoffFunction) {

        String expression = payoffFunction;

        if (payoffFunction.isBlank()) {
            // the payoff function is the the sum function of all properties by default
            return calculateByDefault(strategy.getProperties(), null);
        } else {

            if (checkIfIsDefaultFunction(payoffFunction)) {
                return calculateByDefault(strategy.getProperties(), payoffFunction);
            }

            // replace the placeholder for THIS current player's strategy with the actual value
            // example: payoffFunction is a string formula, e.g: p1 + p2 / p3 - P2p3 with p1, p2, p3 are the properties 1, 2, 3 of the strategy chosen by this player
            for (int i = 0; i < strategy.getProperties().size(); ++i) {
                double propertyValue = strategy.getProperties().get(i);

                String placeholder = String.format("\\bp%d\\b", i + 1);

                expression = expression.replaceAll(placeholder, formatDouble(propertyValue));
            }


            // evaluate this string expression to get the result
            return eval(expression);

        }


    }

    public static double evaluateFitnessValue(Double[] payoffs, String fitnessFunction) {
        String expression = fitnessFunction;
        List<Double> payoffList =  new ArrayList<>(Arrays.asList(payoffs));

        if (fitnessFunction.isBlank()) {
            // if the fitnessFunction is absent,qweqweqwe
            // the fitness value is the average of all payoffs of all chosen strategies by default
            return calculateByDefault(payoffList, null);
        } else {
            // replace placeholders for players' payoffs with the actual values

            if (checkIfIsDefaultFunction(fitnessFunction)) {
                return calculateByDefault(payoffList, fitnessFunction);
            }
            for (int i = 0; i < payoffs.length; i++) {
                double playerPayoff = payoffs[i];

                String placeholder = String.format("\\bu%d\\b", i + 1);
                expression = expression.replaceAll(placeholder, formatDouble(playerPayoff));
            }

            return eval(expression);
        }
    }


    private static String formatDouble(double propertyValue) {
        // if the property value is too small it can be written as for example 1.0E-4, so we need to format it to 0.0001
        return decimalFormat.format(propertyValue);
    }


    private static boolean checkIfIsDefaultFunction(String function) {
        return Arrays.stream(DefaultFunction.values()).anyMatch(f -> f.name().equalsIgnoreCase(function));
    }

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
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .max().getAsDouble();
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
        DefaultFunction function = DefaultFunction.valueOf(defaultFunction.toUpperCase());

        switch (function) {
            case SUM:
                return calSum(values);
            case PRODUCT:
                return calProduct(values);
            case MAX:
                return calMax(values);
            case MIN:
                return calMin(values);
            case AVERAGE:
                return calAverage(values);
            case MEDIAN:
                return calMedian(values);
            case RANGE:
                return calRange(values);
            default:
                return calSum(values);
        }

    }


    public static double eval(String strExpression) {
        System.out.println("Evaluating: ");
        System.out.println(strExpression);

        String formattedExpression = strExpression.replaceAll("NaN", "0")// replace NaN with 0, so that the expression can be evaluated
                .replaceAll("\u00A0", ""); // Removes all NBSP characters from the string

        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < formattedExpression.length()) ? formattedExpression.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();

                if (pos < formattedExpression.length()) {
                    System.out.println("wrong expression: " + formattedExpression);
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) {
                        System.out.println("Missing ')");
                        System.out.println(formattedExpression);
                        throw new RuntimeException("Missing ')'");
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(formattedExpression.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = formattedExpression.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')' after argument to " + func);
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    System.out.println("wrong expression: " + formattedExpression);
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }



}




