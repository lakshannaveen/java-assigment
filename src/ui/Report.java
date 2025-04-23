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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });

        topPanel.add(backButton, BorderLayout.WEST);

        JLabel headingLabel = new JLabel("My Pocket Reports", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Serif", Font.BOLD, 26));
        headingLabel.setForeground(Color.WHITE);
        topPanel.add(headingLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        String email = getEmailFromToken(token);
        List<ExpenseModel> expenses = expenseController.getExpensesByEmail(email);
        Map<String, List<ExpenseModel>> expensesByPocketName = expenses.stream()
                .collect(Collectors.groupingBy(ExpenseModel::getPocketName));

        // Pocket Card Scroll Panel
        JPanel pocketCardContainer = new JPanel();
        pocketCardContainer.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pocketCardContainer.setBackground(new Color(245, 245, 245));

        for (String pocketName : expensesByPocketName.keySet()) {
            JPanel pocketCard = new JPanel();
            pocketCard.setLayout(new BoxLayout(pocketCard, BoxLayout.Y_AXIS));
            pocketCard.setPreferredSize(new Dimension(250, 130));
            pocketCard.setBackground(Color.WHITE);
            pocketCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel pocketLabel = new JLabel(pocketName, SwingConstants.CENTER);
            pocketLabel.setFont(new Font("Serif", Font.BOLD, 18));
            pocketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pocketCard.add(Box.createRigidArea(new Dimension(0, 10)));
            pocketCard.add(pocketLabel);

            JButton downloadButton = new JButton("Download");
            ReportStyle.styleBlueButton(downloadButton);
            downloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            downloadButton.addActionListener(e -> downloadReport(email, pocketName, expensesByPocketName.get(pocketName)));
            pocketCard.add(Box.createRigidArea(new Dimension(0, 10)));
            pocketCard.add(downloadButton);

            pocketCardContainer.add(pocketCard);
        }

        JScrollPane scrollPane = new JScrollPane(pocketCardContainer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Pockets"));

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
        mainPanel.add(chartPanel, BorderLayout.SOUTH);

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
