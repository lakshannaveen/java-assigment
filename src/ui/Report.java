package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import controllers.ExpenseController;
import models.ExpenseModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        // Main panel setup with padding and alignment adjustments
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Welcome label with improved styling
        JLabel welcomeLabel = new JLabel("Welcome to the Report Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.DARK_GRAY);  // Set color to dark gray for contrast
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Fetch and display pocket names with download buttons
        JPanel pocketPanel = new JPanel();
        pocketPanel.setLayout(new BoxLayout(pocketPanel, BoxLayout.Y_AXIS));
        pocketPanel.setBackground(new Color(245, 245, 245));  // Light gray background for pocket panel

        String email = getEmailFromToken(token);
        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        // Group expenses by pocket name
        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        // Adjusted spacing and alignment for each pocket name and its button
        for (String pocketName : expensesByPocketName.keySet()) {
            JPanel pocketRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));  // Spaced components for clarity
            pocketRow.setBackground(new Color(245, 245, 245));  // Ensure consistency in background

            JLabel pocketLabel = new JLabel(pocketName);
            pocketLabel.setFont(new Font("Serif", Font.BOLD, 18));
            pocketLabel.setPreferredSize(new Dimension(300, 30));  // Control the width for better alignment
            pocketRow.add(pocketLabel);

            JButton downloadButton = new JButton("Download");
            ReportStyle.styleBlueButton(downloadButton);
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
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the scrollPane
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button with improved styling
        JButton backButton = new JButton("Back");
        ReportStyle.styleGreenButton(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage(token);  // Navigate back to StartPage with token
                dispose();  // Close current window
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);  // Ensure the button panel has a clean background
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
        String fileName = pocketName + "_report.pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            document.add(new Paragraph("Report for Pocket: " + pocketName));
            document.add(new Paragraph("Email: " + email));
            document.add(new Paragraph(" ")); // Add a blank line

            PdfPTable table = new PdfPTable(4); // 4 columns
            table.addCell("Month");
            table.addCell("Expense Name");
            table.addCell("Amount");
            table.addCell("Date");

            for (ExpenseModel expense : expenses) {
                table.addCell(expense.getSelectedMonth());
                table.addCell(expense.getExpenseName());
                table.addCell(String.valueOf(expense.getAmount()));
                table.addCell(expense.getDate().toString());
            }

            document.add(table);
            document.close();
            JOptionPane.showMessageDialog(this, "Report downloaded: " + fileName);
        } catch (DocumentException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error downloading report: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Example JWT (replace with actual JWT generated during registration)
        String exampleToken = "token";  // Pass your generated token here
        new Report(exampleToken);
    }
}
