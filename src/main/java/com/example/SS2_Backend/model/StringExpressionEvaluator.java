package com.example.SS2_Backend.model;


import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class StringExpressionEvaluator {

    public static double evaluatePayoffFunctionWithRelativeToOtherPlayers(Strategy strategy,
                                                                          String payoffFunction,
                                                                          List<NormalPlayer> normalPlayers,
                                                                          List<Integer> chosenStrategyIndices) {

        String expression = payoffFunction;

        if (payoffFunction.isBlank()) {
            // the payoff function is the the sum function of all properties by default
            return calculatePayoffByDefault(strategy);
        } else {
            // if there is no relationship in the payoff function, then just evaluate it normally, no need to replace any P placeholder
            if (!payoffFunction.contains("P")) {
                return evaluatePayoffFunctionNoRelative(strategy, expression);
            }

            // replace the placeholder for THIS current player's strategy with the actual value
            // example: payoffFunction is a string formula, e.g: p1 + p2 / p3 - P2p3 with p1, p2, p3 are the properties 1, 2, 3 of the strategy chosen by this player
            for (int i = 0; i < strategy.getProperties().size(); ++i) {
                double propertyValue = strategy.getProperties().get(i);

                DecimalFormat df = new DecimalFormat("#.##############");
                String formattedNum = df.format(propertyValue);
                String placeholder = String.format("\\bp%d\\b", i + 1);

                expression = expression.replaceAll(placeholder, formattedNum);
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
                    expression = expression.replaceAll(placeholder, Double.toString(propertyValue));
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
            return calculatePayoffByDefault(strategy);
        } else {

            // replace the placeholder for THIS current player's strategy with the actual value
            // example: payoffFunction is a string formula, e.g: p1 + p2 / p3 - P2p3 with p1, p2, p3 are the properties 1, 2, 3 of the strategy chosen by this player
            for (int i = 0; i < strategy.getProperties().size(); ++i) {
                double propertyValue = strategy.getProperties().get(i);

                DecimalFormat df = new DecimalFormat("#.##############");
                String formattedNum = df.format(propertyValue);
                String placeholder = String.format("\\bp%d\\b", i + 1);

                expression = expression.replaceAll(placeholder, formattedNum);
            }


            // evaluate this string expression to get the result
            return eval(expression);

        }


    }
    private static double calculatePayoffByDefault(Strategy strategy) {
        // sum of all property values of the strategy
        return strategy.getProperties().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }


    public static double eval(String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
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
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
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
                        System.out.println(str);
                        throw new RuntimeException("Missing ')'");
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
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
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
//
//    public static double evaluateExpression(String str) {
//        Expression e = new Expression(str);
//        return e.calculate();
//
//    }

    public static double evaluateFitnessValue(double[] payoffs, String fitnessFunction) {
        String expression = fitnessFunction;

        if (fitnessFunction.isBlank()) {
            // if the fitnessFunction is absent,
            // the fitness value is the average of all payoffs of all chosen strategies by default
            return calculateFitnessValueByDefault(payoffs);
        } else {
            // replace placeholders for players' payoffs with the actual values
            for (int i = 0; i < payoffs.length; i++) {
                double playerPayoff = payoffs[i];

                String placeholder = String.format("\\bu%d\\b", i + 1);
                expression = expression.replaceAll(placeholder, Double.toString(playerPayoff));
            }

            return eval(expression);
        }
    }

    private static double calculateFitnessValueByDefault(double[] payoffs) {
        // calculate the average of the payoffs
        return Arrays.stream(payoffs)
                .average()
                .orElse(0D);
    }
}




