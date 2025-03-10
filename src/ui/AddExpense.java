package ui;

import javax.swing.*;
import java.awt.*;

public class AddExpense extends JFrame {

    public AddExpense() {
        // Set the JFrame properties
        setTitle("Welcome to Add Expense");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Add some welcome label or instructions
        JLabel welcomeLabel = new JLabel("Welcome to Add Expense", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AddExpense();
    }
}