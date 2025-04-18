package gui;

import javax.swing.*;

public class BaseForm extends JFrame {
    public BaseForm(String title){
        super(title);
        setSize(385, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}
