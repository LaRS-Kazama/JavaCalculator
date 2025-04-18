package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Caculator extends BaseForm {
    JTextField resultArea;
    JButton[] buttons;

    public Caculator() {
        super("Calculator");
        addComponents();
        setupButtonCalculator();
    }

    private void addComponents() {
        //This is where the numbers show when you click the buttons of the calculator
        //The text field where it shows the numbers of the calculator
        resultArea = new JTextField();
        //The position of the text field
        resultArea.setBounds(17, 20, 335, 50);
        //To set the text field not allowed the user to type it (just to display numbers)
        resultArea.setEditable(false);

        //Set text to right
        resultArea.setHorizontalAlignment(JTextField.RIGHT);
        //Larger text and clear
        resultArea.setFont(new Font("Arial", Font.BOLD, 24));
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(resultArea);
    }

    private void setupButtonCalculator() {
        String[] buttonLabels = {
                "AC", "←", "+/-", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "%", "0", "." ,"="
        };
        buttons = new JButton[buttonLabels.length];

        int startX = 20;
        int startY = 90;
        int width = 75;
        int height = 60;
        int buttonGap = 10;

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = new JButton(buttonLabels[i]);

                int row = i / 4;
                int col = i % 4;
                buttons[i].setBounds(
                        startX + col * (width + buttonGap),
                        startY + row * (height + buttonGap),
                        width,
                        height
                );
                buttons[i].setFont(new Font("Arial", Font.BOLD, 20));


            final int index = i;
            buttons[i].addActionListener(e -> {
                String buttonText = buttonLabels[index];

                if (buttonText.equals("=")){
                    try {
                        String expr = resultArea.getText();
                        double result = evaluateOperation(expr);
                        if (result == (long) result){
                            resultArea.setText(String.valueOf((long) result));
                        }else {
                            resultArea.setText(String.valueOf(result));
                        }
                    }catch (Exception ex){
                        resultArea.setText("Error");
                    }
                } else if (buttonText.equals("AC")) {
                    resultArea.setText("");
                } else if (buttonText.equals("←")) {
                    String current = resultArea.getText();
                    if (!current.isEmpty()){
                        resultArea.setText(current.substring(0, current.length() - 1));
                    }
                    
                } else if (buttonText.equals("+/-")) {
                    String integersNegative = resultArea.getText();
                    if (!integersNegative.isEmpty()){
                        if (integersNegative.startsWith("-")){
                            resultArea.setText(integersNegative.substring(1));
                        } else {
                          resultArea.setText("-" + integersNegative);
                        }
                    }
                } else if (buttonText.equals("%")) {
                    try {
                        double percentValue = Double.parseDouble(resultArea.getText());
                        resultArea.setText(String.valueOf(percentValue / 100));
                    }catch (Exception ex){
                      resultArea.setText("Error");
                    }
                } else {
                    if (resultArea.getText().equals("Error")){
                        resultArea.setText("");
                    }
                    resultArea.setText(resultArea.getText() + buttonText);
                }
            });

            this.add(buttons[i]);
        }
    }

    private double evaluateOperation(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        int j = 0;
        while (j < expression.length()) {
            char c = expression.charAt(j);

            if ((c == '-' && (j == 0 || isOperator(expression.charAt(j - 1)))) || Character.isDigit(c) || c == '.') {
                StringBuilder stringBuilder = new StringBuilder();
                if (c == '-') {
                    stringBuilder.append('-');
                    j++;
                }
                while (j < expression.length() && (Character.isDigit(expression.charAt(j)) || expression.charAt(j) == '.')) {
                    stringBuilder.append(expression.charAt(j));
                    j++;
                }
                numbers.push(Double.parseDouble(stringBuilder.toString()));
                continue;
            }
            if (isOperator(c)){
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(c)) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    numbers.push(applyOp (a, b, op));
                }
                operators.push(c);
            }
            j++;
        }
        while (!operators.isEmpty()){
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operators.pop();
            numbers.push(applyOp (a, b, op));
        }
        return numbers.pop();
    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '×' || op == '÷') return 2;
        return 0;
    }

    private double applyOp(double a, double b, char op) {
        switch (op){
            case '+': return a + b;
            case '-': return a - b;
            case '×': return a * b;
            case '÷':
                if (b == 0) throw new ArithmeticException("Cannot be divided to zero");
                return a / b;
            default: return 0;
        }
    }
}