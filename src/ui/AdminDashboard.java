package ui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.category.*;
import javax.swing.*;
import org.jfree.chart.renderer.category.BarRenderer;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private static final String LOG_FILE_PATH = "login_logs.txt";

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 700);
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
        clockPanel.setOpaque(false);
        clockPanel.add(clockLabel);
        mainPanel.add(clockPanel, BorderLayout.SOUTH);

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());
                clockLabel.setText(currentDateTime);
            }
        });
        timer.start();

        // Buttons
        JButton showLogsButton = new JButton("Show Logs");
        JButton accountsButton = new JButton("Accounts");
        JButton adminsButton = new JButton("Manage Admins");
        JButton adminLogsButton = new JButton("Admin Logs");
        JButton logoutButton = new JButton("Logout");

        // Apply styles to buttons
        AdminDashboardStyle.applyButtonStyle(showLogsButton);
        AdminDashboardStyle.applyButtonStyle(accountsButton);
        AdminDashboardStyle.applyButtonStyle(adminsButton);
        AdminDashboardStyle.applyButtonStyle(adminLogsButton);
        AdminDashboardStyle.applyLogoutButtonStyle(logoutButton); // Special style for logout button

        // Set button sizes
        Dimension buttonSize = new Dimension(150, 40);
        showLogsButton.setPreferredSize(buttonSize);
        accountsButton.setPreferredSize(buttonSize);
        adminsButton.setPreferredSize(buttonSize);
        adminLogsButton.setPreferredSize(buttonSize);
        logoutButton.setPreferredSize(buttonSize);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(showLogsButton);
        buttonPanel.add(accountsButton);
        buttonPanel.add(adminsButton);
        buttonPanel.add(adminLogsButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Add chart panel to center by default
        JPanel chartPanel = createLoginTimeChartPanel();
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button actions
        showLogsButton.addActionListener(e -> showLogs());
        accountsButton.addActionListener(e -> {
            new AdminUserAccounts();
            dispose();
        });
        adminsButton.addActionListener(e -> {
            new AdminManage();
            dispose();
        });
        adminLogsButton.addActionListener(e -> {
            new AdminLogs().setVisible(true);
            dispose();
        });
        logoutButton.addActionListener(e -> {
            new HomePage();
            dispose();
        });

        AdminDashboardStyle.applyFrameStyle(this);
        setVisible(true);
    }

    private JPanel createLoginTimeChartPanel() {
        // Get login data by hour
        Map<Integer, Integer> loginsByHour = getLoginsByHour();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int hour = 0; hour < 24; hour++) {
            dataset.addValue(loginsByHour.getOrDefault(hour, 0), "Logins", String.format("%02d:00", hour));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "User Login Time Distribution",
                "Time of Day",
                "Number of Logins",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customize chart appearance
        chart.setBackgroundPaint(new Color(230, 240, 255));
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(240, 248, 255));
        plot.setRangeGridlinePaint(Color.BLUE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(70, 130, 180));
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 450));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private Map<Integer, Integer> getLoginsByHour() {
        Map<Integer, Integer> hourCounts = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            hourCounts.put(i, 0);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Event: Login")) {
                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        if (part.startsWith("Time: ")) {
                            String timeStr = part.substring(6);
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = sdf.parse(timeStr);
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                int hour = cal.get(Calendar.HOUR_OF_DAY);
                                hourCounts.put(hour, hourCounts.get(hour) + 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hourCounts;
    }

    private void showLogs() {
        String[] columnNames = {"Event", "UserType", "Username", "Time"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(173, 216, 230));
        table.setBackground(new Color(224, 255, 255));

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
                            case "Event": rowData[0] = keyValue[1]; break;
                            case "UserType": rowData[1] = keyValue[1]; break;
                            case "Username": rowData[2] = keyValue[1]; break;
                            case "Time": rowData[3] = keyValue[1]; break;
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
        AdminDashboardStyle.applyButtonStyle(downloadPdfButton);

        downloadPdfButton.addActionListener(e -> {
            try {
                downloadLogsAsPdf();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to download PDF.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(downloadPdfButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, panel, "Log Contents", JOptionPane.INFORMATION_MESSAGE);
    }

    private void downloadLogsAsPdf() throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("logs.pdf"));
        document.open();

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
                            case "Event": rowData[0] = keyValue[1]; break;
                            case "UserType": rowData[1] = keyValue[1]; break;
                            case "Username": rowData[2] = keyValue[1]; break;
                            case "Time": rowData[3] = keyValue[1]; break;
                        }
                    }
                }
                for (String data : rowData) {
                    table.addCell(new PdfPCell(new Phrase(data)));
                }
            }
        }

        document.add(table);
        document.close();
        JOptionPane.showMessageDialog(this, "Logs downloaded as PDF successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}