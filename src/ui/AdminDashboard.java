package ui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminDashboard extends JFrame {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Custom gradient panel
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(230, 240, 255);
                Color color2 = new Color(180, 200, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JLabel welcomeLabel = new JLabel("Welcome to Admin Dashboard", SwingConstants.CENTER);
        AdminDashboardStyle.applyHeaderStyle(welcomeLabel);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Clock Panel
        JPanel clockPanel = new JPanel();
        JLabel clockLabel = new JLabel();
        clockPanel.add(clockLabel);
        mainPanel.add(clockPanel, BorderLayout.SOUTH);

        // Set the clock to update every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());
                clockLabel.setText(currentDateTime);
            }
        });
        timer.start();

        // Buttons Panel
        JButton showLogsButton = new JButton("Show Logs");
        JButton accountsButton = new JButton("Accounts");
        JButton adminsButton = new JButton("Manage Admins"); // New button

        AdminDashboardStyle.applyButtonStyle(showLogsButton);
        AdminDashboardStyle.applyButtonStyle(accountsButton);
        AdminDashboardStyle.applyButtonStyle(adminsButton); // Style the new button

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(showLogsButton);
        buttonPanel.add(accountsButton);
        buttonPanel.add(adminsButton); // Add the new button

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action Listeners
        showLogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLogs();
            }
        });

        accountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminUserAccounts();
                dispose(); // Close the current frame
            }
        });

        adminsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminManage();
                dispose(); // Close the current frame
            }
        });

        AdminDashboardStyle.applyFrameStyle(this);
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
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.LIGHT_GRAY);

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

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton downloadPdfButton = new JButton("Download PDF");

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

        buttonPanel.add(downloadPdfButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Log Contents", JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadLogsAsPdf() throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("logs.pdf"));
        document.open();

        // Use fully qualified com.itextpdf.text.Font
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
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