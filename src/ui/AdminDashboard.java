package ui;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class AdminDashboard extends JFrame {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        AdminDashboardStyle.applyStyle(welcomeLabel);
        add(welcomeLabel, BorderLayout.NORTH);

        JButton showLogsButton = new JButton("Show Logs");
        JButton downloadPdfButton = new JButton("Download PDF");
        JButton accountsButton = new JButton("Accounts");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showLogsButton);
        buttonPanel.add(downloadPdfButton);
        buttonPanel.add(accountsButton);
        add(buttonPanel, BorderLayout.CENTER);

        showLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogs();
            }
        });

        downloadPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    downloadLogsAsPdf();
                } catch (FileNotFoundException | DocumentException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AdminDashboard.this, "Failed to download PDF.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminUserAccounts();
                dispose(); // Close the current frame
            }
        });

        AdminDashboardStyle.applyStyle(this);
        setVisible(true);
    }

    private void showLogs() {
        String[] columnNames = {"Event", "UserType", "Username", "Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table read-only
            }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] logParts = line.split(", ");
                String[] rowData = new String[4];
                for (String part : logParts) {
                    String[] keyValue = part.split(": ");
                    if (keyValue.length == 2) {
                        switch (keyValue[0]) {
                            case "Event":
                                rowData[0] = keyValue[1];
                                break;
                            case "UserType":
                                rowData[1] = keyValue[1];
                                break;
                            case "Username":
                                rowData[2] = keyValue[1];
                                break;
                            case "Time":
                                rowData[3] = keyValue[1];
                                break;
                        }
                    }
                }
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load logs.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, scrollPane, "Log Contents", JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadLogsAsPdf() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("logs.pdf"));
        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Paragraph title = new Paragraph("Log Contents", font);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        String[] columnNames = {"Event", "UserType", "Username", "Time"};
        for (String columnName : columnNames) {
            PdfPCell cell = new PdfPCell(new Phrase(columnName));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] logParts = line.split(", ");
                String[] rowData = new String[4];
                for (String part : logParts) {
                    String[] keyValue = part.split(": ");
                    if (keyValue.length == 2) {
                        switch (keyValue[0]) {
                            case "Event":
                                rowData[0] = keyValue[1];
                                break;
                            case "UserType":
                                rowData[1] = keyValue[1];
                                break;
                            case "Username":
                                rowData[2] = keyValue[1];
                                break;
                            case "Time":
                                rowData[3] = keyValue[1];
                                break;
                        }
                    }
                }
                for (String data : rowData) {
                    table.addCell(new PdfPCell(new Phrase(data)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load logs.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        document.add(table);
        document.close();
        JOptionPane.showMessageDialog(this, "Logs downloaded as PDF successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminDashboard();
            }
        });
    }
}