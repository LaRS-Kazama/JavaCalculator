package gui;

import java.util.Stack;

public class CalculatorEngine {
    public static double evaluateExpression(String expression){
        expression = expression.replace("×", "*").replace("÷", "/").replaceAll("\\s+", "");
        return evaluateOperation(expression);
    }

    public static double evaluateOperation(String expr) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int j = 0;
        while (j < expr.length()) {
            char c = expr.charAt(j);
            if (c == '('){
                operators.push(c);
                j++;
            } else if (Character.isDigit(c) || c == '.') {
                StringBuilder stringBuilder = new StringBuilder();
                while (j < expr.length() && (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')){
                    stringBuilder.append(expr.charAt(j++));
                }
                numbers.push(Double.parseDouble(stringBuilder.toString()));
            } else if (c == '-' && (j == 0 || expr.charAt(j - 1) == '(' || isOperator(expr.charAt(j - 1)))) {
                StringBuilder sB = new StringBuilder("-");
                j++;

                while (j < expr.length() && (Character.isDigit(expr.charAt(j)) || expr.charAt(j) == '.')) {
                    sB.append(expr.charAt(j++));
                }
                numbers.push(Double.parseDouble(sB.toString()));
            } else if (c == ')'){
                while (operators.peek() != '('){
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOp (a, b, op));
                }
                operators.pop();
                j++;
            } else if (isOperator(c)){
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOp (a, b, op));
                }
                operators.push(c);
                j++;
            }else {
                j++;
            }
        }
        while (!operators.isEmpty()){
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operators.pop();
            numbers.push(applyOp (a, b, op));
        }
        return numbers.pop();
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedence(char op) {
        return (op == '+' || op == '-') ? 1 : 2;
    }

    private static double applyOp(double a, double b, char op) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
                yield a / b;
            }
            default -> 0;
        };
    }
}

