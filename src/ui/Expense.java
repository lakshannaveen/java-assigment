package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Expense extends JFrame {

    public Expense() {
        setTitle("Expense Tracker");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel and apply styles
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Serif", Font.PLAIN, 16));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setOpaque(true);

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage();
                dispose(); // Close the current window
            }
        });

        // Create a panel for the back button and add the button to it
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);

        // Add the back button panel to the main panel
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        JLabel expenseLabel = new JLabel("Welcome to the Expense Tracker!", SwingConstants.CENTER);
        expenseLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Add the label to the main panel
        mainPanel.add(expenseLabel, BorderLayout.CENTER);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Expense();
            }
        });
    }
}