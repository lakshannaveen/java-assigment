package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StartPage extends JFrame {
    private ExpenseController expenseController;

    public StartPage(String token) {
        // Decode the token to get the email
        String email = getEmailFromToken(token);

        // Extract the username part of the email (before '@')
        String username = email != null ? email.split("@")[0] : "Guest";

        // Set the JFrame properties
        setTitle("Welcome - Expense Tracker");
        setSize(800, 600); // Adjusted size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the expense controller
        expenseController = new ExpenseController();

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome label with the username or "Guest" if email is null
        JLabel welcomeLabel = new JLabel("Welcome to My Expense, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Tabbed pane to display expenses by pocket name
        JTabbedPane tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Fetch and display expenses
        loadExpenses(email, tabbedPane);

        // Start button to navigate to Expense page
        JButton startButton = new JButton("Start My Expense");
        StartPageStyle.styleButton(startButton);  // Apply styling from your custom method

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Expense(token);  // Pass the token to Expense
                dispose();  // Close current window
            }
        });

        // Button panel for layout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(startButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
    }

    private void loadExpenses(String email, JTabbedPane tabbedPane) {
        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        // Group expenses by pocket name
        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        // Create a table for each pocket name and add it to the tabbed pane
        for (Map.Entry<String, List<ExpenseModel>> entry : expensesByPocketName.entrySet()) {
            String pocketName = entry.getKey();
            List<ExpenseModel> pocketExpenses = entry.getValue();

            String[] columnNames = {"Month", "Expense Name", "Amount", "Date"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            for (ExpenseModel expense : pocketExpenses) {
                Object[] rowData = {
                        expense.getSelectedMonth(),
                        expense.getExpenseName(),
                        expense.getAmount(),
                        expense.getDate()
                };
                tableModel.addRow(rowData);
            }

            JTable expenseTable = new JTable(tableModel);
            StartPageStyle.styleTable(expenseTable);  // Apply custom styles to the table

            JScrollPane scrollPane = new JScrollPane(expenseTable);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(scrollPane, BorderLayout.CENTER);

            tabbedPane.addTab(pocketName, panel);
        }
    }

    private String getEmailFromToken(String token) {
        try {
            // Decode JWT token to get the email claim
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if there is any error decoding the token
        }
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new StartPage(exampleToken);
    }
}