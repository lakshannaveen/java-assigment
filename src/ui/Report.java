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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Report extends JFrame {
    private String token;
    private ExpenseController expenseController;

    public Report(String token) {
        this.token = token;
        this.expenseController = new ExpenseController();

        setTitle("Report Page");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome to the Report Page!", SwingConstants.CENTER);
        welcomeLabel.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 24));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel pocketPanel = new JPanel();
        pocketPanel.setLayout(new BoxLayout(pocketPanel, BoxLayout.Y_AXIS));
        pocketPanel.setBackground(new Color(245, 245, 245));

        String email = getEmailFromToken(token);
        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);

        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        for (String pocketName : expensesByPocketName.keySet()) {
            JPanel pocketRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            pocketRow.setBackground(new Color(245, 245, 245));

            JLabel pocketLabel = new JLabel(pocketName);
            pocketLabel.setFont(new java.awt.Font("Serif", java.awt.Font.BOLD, 18));
            pocketLabel.setPreferredSize(new Dimension(300, 30));
            pocketRow.add(pocketLabel);

            JButton downloadButton = new JButton("Download");
            ReportStyle.styleBlueButton(downloadButton);
            downloadButton.addActionListener(e -> downloadReport(email, pocketName, expensesByPocketName.get(pocketName)));
            pocketRow.add(downloadButton);

            pocketPanel.add(pocketRow);
        }

        JScrollPane scrollPane = new JScrollPane(pocketPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Chart Panel
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setPreferredSize(new Dimension(1000, 300));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Expenses Over Time"));

        TimeSeries series = new TimeSeries("All Expenses");

        for (ExpenseModel expense : expenses) {
            series.addOrUpdate(new Day(expense.getDate()), expense.getAmount());
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "All User Expenses",
                "Date",
                "Amount",
                dataset,
                false, true, false
        );

        ChartPanel jChartPanel = new ChartPanel(chart);
        chartPanel.add(jChartPanel, BorderLayout.CENTER);
        mainPanel.add(chartPanel, BorderLayout.NORTH);

        JButton backButton = new JButton("Back");
        ReportStyle.styleGreenButton(backButton);
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private String getEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
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
        String exampleToken = "token"; // Replace with actual token
        new Report(exampleToken);
    }
}
