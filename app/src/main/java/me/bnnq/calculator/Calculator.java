package me.bnnq.calculator;

import static me.bnnq.calculator.Calculator.Priority.*;

import static me.bnnq.calculator.Calculator.Priority.CLOSE_BRACKET;
import static me.bnnq.calculator.Calculator.Priority.DIGIT_DOT;
import static me.bnnq.calculator.Calculator.Priority.OPEN_BRACKET;
import static me.bnnq.calculator.Calculator.Priority.OPERATOR_MD;
import static me.bnnq.calculator.Calculator.Priority.OPERATOR_PM;
import static me.bnnq.calculator.Calculator.Priority.WHITESPACE;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Calculator {

     enum Priority {
        OPERATOR_MD(3), //operator multiply and divide
        OPERATOR_PM(2), //operator plus and minus
        OPEN_BRACKET(1),
        DIGIT_DOT(0), //digit and dot
        CLOSE_BRACKET(-1),
        WHITESPACE(-2);

        private final int val;

        Priority(final int val) {
            this.val = val;
        }

        public int toInt() {
            return val;
        }
    }

    private Calculator() { throw new IllegalStateException("Utility class"); }

    public static double calculateExpression(String expression)  {
        return calculateRPN(expressionToRPN(modifyExpression(expression)));
    }

    private static int calculateNumOfOpenBrackets(@NonNull String str) {
        int numOfOpenBrackets = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == '(')
                numOfOpenBrackets++;

            return numOfOpenBrackets;
    }

    private static int calculateNumOfCloseBrackets(@NonNull String str) {
        int numOfCloseBrackets = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == ')')
                numOfCloseBrackets++;

            return numOfCloseBrackets;
    }

    private static String modifyExpression(@NonNull String expression) {

         //First modification: delete single brackets if there are, until any character other than a parenthesis is encountered
         //(it's necessary in order for the second modification to be correctly completed)
        if (expression.contains("(")) {
            int numOfCloseBrackets = 0;
            for (int i = expression.length() - 1; i >= 0 && (expression.charAt(i) == '(' || expression.charAt(i) == ')'); i--) {
                if (expression.charAt(i) == ')')
                    numOfCloseBrackets++;
                else {
                    if (numOfCloseBrackets-- <= 0) {
                        StringBuilder sb = new StringBuilder(expression);
                        sb.deleteCharAt(i);
                        expression = sb.toString();
                    }
                }
            }
        }

         //Second modification: delete operator at end if present (i.e. there is no operand for it)
        if (getPriority(expression.charAt(expression.length() - 1)).toInt() > OPEN_BRACKET.toInt()) {
            StringBuilder sb = new StringBuilder(expression);
            sb.deleteCharAt(sb.length() - 1);
            expression = sb.toString();
        }

        //Third modification: adding missing closing brackets
        int openBracketsCounter = calculateNumOfOpenBrackets(expression);
        int closeBracketsCounter = calculateNumOfCloseBrackets(expression);

        StringBuilder sb = new StringBuilder(expression);
        while (openBracketsCounter > closeBracketsCounter) {
            sb.append(')');
            closeBracketsCounter++;
        }
        expression = sb.toString();

        return expression;
    }

    private static String expressionToRPN(@NonNull String expression) {
        String current = "";
        List<Character> operatorsBuf = new ArrayList<>();

        for (int i = 0; i < expression.length(); i++) {
            switch (getPriority(expression.charAt(i))) {
                case WHITESPACE:
                    continue;
                case CLOSE_BRACKET: {
                    StringBuilder sb = new StringBuilder(current);
                    while (getPriority(operatorsBuf.get(operatorsBuf.size() - 1)) != OPEN_BRACKET)
                        sb.append(operatorsBuf.remove(operatorsBuf.size() - 1));
                    operatorsBuf.remove(operatorsBuf.size() - 1);
                    current = sb.toString();
                    break;
                }
                case DIGIT_DOT: {
                    StringBuilder sb = new StringBuilder(current);
                    while (i < expression.length() && getPriority(expression.charAt(i)) == DIGIT_DOT)
                        sb.append(expression.charAt(i++));
                    sb.append(' ');
                    current = sb.toString();
                    i--;
                    break;
                }
                case OPEN_BRACKET: {
                    operatorsBuf.add('(');
                    break;
                }
                default: {
                    StringBuilder sb = new StringBuilder(current);
                    while (!operatorsBuf.isEmpty() && getPriority(expression.charAt(i)).toInt() <= getPriority(operatorsBuf.get(operatorsBuf.size() - 1)).toInt())
                        sb.append(operatorsBuf.remove(operatorsBuf.size() - 1));
                    operatorsBuf.add(expression.charAt(i));
                    current = sb.toString();
                }
            }
        }

        StringBuilder sb = new StringBuilder(current);
        while (!operatorsBuf.isEmpty())
            sb.append(operatorsBuf.remove(operatorsBuf.size() - 1));

        return sb.toString();
    }

    private static double calculateRPN(@NonNull String rpn) {

        List<Double> calculationBuf = new LinkedList<>();
        for (int i = 0; i < rpn.length(); i++) {
            //If char is digit - sew it with subsequent digits into a single number and add to the calculation buffer
            if (getPriority(rpn.charAt(i)) == DIGIT_DOT) {
                StringBuilder sb = new StringBuilder();
                while (i < rpn.length() && getPriority(rpn.charAt(i)) == DIGIT_DOT)
                    sb.append(rpn.charAt(i++));
                calculationBuf.add(Double.parseDouble(sb.toString()));
            }
            else {
                //If calculation buffer contains only one number - add "invisible" number so that it doesn't affect the calculations
                if (calculationBuf.size() == 1 && getPriority(rpn.charAt(i)) == OPERATOR_MD)
                    calculationBuf.add((double) 1);
                else if (calculationBuf.size() == 1 && getPriority(rpn.charAt(i)) == OPERATOR_PM)
                    calculationBuf.add((double) 0);

                switch (rpn.charAt(i)) {
                    case ' ':
                        continue;
                    case '+': {
                        calculationBuf.add(calculationBuf.size() - 2, (calculationBuf.remove(calculationBuf.size() - 2) + calculationBuf.remove(calculationBuf.size() - 1)));
                        break;
                    }
                    case '-': {
                        calculationBuf.add(calculationBuf.size() - 2, (calculationBuf.remove(calculationBuf.size() - 2) - calculationBuf.remove(calculationBuf.size() - 1)));
                        break;
                    }
                    case '*': {
                        calculationBuf.add(calculationBuf.size() - 2, (calculationBuf.remove(calculationBuf.size() - 2) * calculationBuf.remove(calculationBuf.size() - 1)));
                        break;
                    }
                    case '/': {
                        calculationBuf.add(calculationBuf.size() - 2, (calculationBuf.remove(calculationBuf.size() - 2) / calculationBuf.remove(calculationBuf.size() - 1)));
                        break;
                    }
                    default: {
                        try {
                            throw new UnexpectedBehaviorException("Unexpected symbol at RPN expression");
                        }
                        catch (UnexpectedBehaviorException e) {
                            e.printStackTrace();
                            System.exit(-10);
                        }
                    }
                }
            }
        }

        try {
            if (calculationBuf.size() != 1)
                throw new UnexpectedBehaviorException("As a result of calculations, only one number should remain in the buffer");
        }
        catch (UnexpectedBehaviorException e) {
            e.printStackTrace();
            System.exit(-10);
        }
        return calculationBuf.remove(calculationBuf.size() - 1);
    }

    private static Priority getPriority(final char symbol) {
        if (symbol == '*' || symbol == '/')
            return OPERATOR_MD;
        if (symbol == '+' || symbol == '-')
            return OPERATOR_PM;
        if (symbol == '(')
            return OPEN_BRACKET;
        if (symbol == ')')
            return CLOSE_BRACKET;
        if ((symbol >= '0' && symbol <= '9') || symbol == '.')
            return DIGIT_DOT;
        if (symbol == ' ')
            return WHITESPACE;

        try {
            throw new IncorrectUseException("Using the getPriority() inappropriately");
        }
        catch (IncorrectUseException e) {
            e.printStackTrace();
            System.exit(-10);
        }
        return null;
    }

}