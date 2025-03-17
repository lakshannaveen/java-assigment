package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.ExpenseController;
import models.ExpenseModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Report extends JFrame {
    private String token;
    private ExpenseController expenseController;

    public Report(String token) {
        this.token = token; // Store the token
        this.expenseController = new ExpenseController();

        // Set the JFrame properties
        setTitle("Report Page");
        setSize(800, 600); // Adjusted size for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel setup
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Report Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Fetch and display pocket names with download buttons
        JPanel pocketPanel = new JPanel();
        pocketPanel.setLayout(new BoxLayout(pocketPanel, BoxLayout.Y_AXIS));

        String email = getEmailFromToken(token);
        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        // Group expenses by pocket name
        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        for (String pocketName : expensesByPocketName.keySet()) {
            JPanel pocketRow = new JPanel(new FlowLayout(FlowLayout.LEFT));

            JLabel pocketLabel = new JLabel(pocketName);
            pocketLabel.setFont(new Font("Serif", Font.BOLD, 18));
            pocketRow.add(pocketLabel);

            JButton downloadButton = new JButton("Download");
            downloadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    downloadReport(email, pocketName, expensesByPocketName.get(pocketName));
                }
            });
            pocketRow.add(downloadButton);

            pocketPanel.add(pocketRow);
        }

        JScrollPane scrollPane = new JScrollPane(pocketPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage(token);  // Navigate back to StartPage with token
                dispose();  // Close current window
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the main panel to the frame and make it visible
        add(mainPanel);
        setVisible(true);
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

    private void downloadReport(String email, String pocketName, List<ExpenseModel> expenses) {
        String fileName = pocketName + "_report.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Month,Expense Name,Amount,Date\n");
            for (ExpenseModel expense : expenses) {
                writer.append(expense.getSelectedMonth()).append(",")
                        .append(expense.getExpenseName()).append(",")
                        .append(String.valueOf(expense.getAmount())).append(",")
                        .append(expense.getDate().toString()).append("\n");
            }
            JOptionPane.showMessageDialog(this, "Report downloaded: " + fileName);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error downloading report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new Report(exampleToken);
    }
}