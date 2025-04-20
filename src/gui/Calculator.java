package gui;

import javax.swing.*;
import java.awt.*;
import java.lang.classfile.instruction.CharacterRange;
import java.util.Stack;

public class Calculator extends BaseForm {
    JTextField resultArea;
    JButton[] buttons;

    public Calculator() {
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
                "%", "0", ".", "="
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

                switch (buttonText) {
                    case "=" -> {
                        try {
                            String expr = resultArea.getText();
                            double result = CalculatorEngine.evaluateExpression(expr);
                            if (result == (long) result) {
                                resultArea.setText(String.valueOf((long) result));
                            } else {
                                resultArea.setText(String.valueOf(result));
                            }
                        } catch (Exception ex) {
                            resultArea.setText("Error");
                        }
                    }
                    case "AC" -> resultArea.setText("");
                    case "←" -> {
                        String current = resultArea.getText();
                        if (!current.isEmpty()) {
                            resultArea.setText(current.substring(0, current.length() - 1));
                        }
                    }
                    case "+/-" -> {
                        String integersNegative = resultArea.getText();
                        if (integersNegative.isEmpty()) return;
                        int lastIndex = integersNegative.length() - 1;
                        int i2 = lastIndex;

                        while (i2 >= 0 && (Character.isDigit(integersNegative.charAt(i2)) || integersNegative.charAt(i2) == '.'
                                || integersNegative.charAt(i2) == ')')) {
                            if (integersNegative.charAt(i2) == ')') {
                                int parentCount = 1;
                                i2--;
                                while (i2 >= 0 && parentCount >= 0) {
                                    if (integersNegative.charAt(i2) == ')') parentCount++;
                                    else if (integersNegative.charAt(i2) == '(') parentCount--;
                                    i2--;
                                }
                                break;
                            }
                            i2--;
                        }
                        int start = i2 + 1;
                        String before = integersNegative.substring(0, start);
                        String target = integersNegative.substring(start);

                        if (target.startsWith("(-") && target.endsWith(")")) {
                            target = target.substring(2, target.length() - 1);
                        } else {
                            target = "(-" + target + ")";
                        }
                        resultArea.setText(before + target);
                    }
                    case "%" -> {
                        try {
                            double percentValue = Double.parseDouble(resultArea.getText());
                            resultArea.setText(String.valueOf(percentValue / 100));
                        } catch (Exception ex) {
                            resultArea.setText("Error");
                        }
                    }
                    default -> {
                        if (resultArea.getText().equals("Error")) {
                            resultArea.setText("");
                        }
                        resultArea.setText(resultArea.getText() + buttonText);
                    }
                }
            });

            this.add(buttons[i]);
        }
    }
}