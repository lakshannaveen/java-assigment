package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class AdminLogs extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public AdminLogs() {
        setTitle("Admin Login Logs");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Apply light blue theme
        getContentPane().setBackground(new Color(240, 245, 255));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Back button
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setForeground(Color.WHITE);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        // Title Label Centered Properly
        JLabel titleLabel = new JLabel("ADMIN LOGIN LOGS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // A center panel just for title to make it stay centered
        JPanel centerTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerTitlePanel.setOpaque(false); // transparent
        centerTitlePanel.add(titleLabel);
        headerPanel.add(centerTitlePanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // Content Panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(240, 245, 255));

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"Event", "User Type", "Username", "Date & Time"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.BLACK);
        header.setForeground(Color.WHITE);
        header.setOpaque(true);

        // Fix white slash: override header renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(Color.BLACK);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createLineBorder(new Color(200, 220, 255), 5)
        ));
        contentPanel.add(tableScrollPane);

        // Chart Panel
        JFreeChart chart = createScatterChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createLineBorder(new Color(200, 220, 255), 5)
        ));
        chartPanel.setPreferredSize(new Dimension(500, 600));
        contentPanel.add(chartPanel);

        add(contentPanel, BorderLayout.CENTER);

        // Load data from file
        loadAdminLogs();
    }

    private void loadAdminLogs() {
        try (BufferedReader br = new BufferedReader(new FileReader("login_logs.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 4) {
                    String userType = parts[1].split(": ")[1];
                    if (userType.equalsIgnoreCase("admin")) {
                        String event = parts[0].split(": ")[1];
                        String username = parts[2].split(": ")[1];
                        String time = parts[3].split(": ", 2)[1];
                        tableModel.addRow(new Object[]{event, userType, username, time});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JFreeChart createScatterChart() {
        XYSeries series = new XYSeries("Admin Logins Per Day");
        Map<String, Integer> loginCounts = new TreeMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("login_logs.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length == 4 && parts[1].split(": ")[1].equalsIgnoreCase("admin")) {
                    if (line.contains("Event: Login")) {
                        String dateTime = parts[3].split(": ", 2)[1];
                        String dateOnly = dateTime.split(" ")[0];
                        loginCounts.put(dateOnly, loginCounts.getOrDefault(dateOnly, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = 1;
        for (String date : loginCounts.keySet()) {
            series.add(index++, loginCounts.get(date));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Admin Daily Login Activity",
                "Day Sequence",
                "Number of Logins",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        chart.setBackgroundPaint(new Color(240, 245, 255));
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(200, 220, 255));
        plot.setRangeGridlinePaint(new Color(200, 220, 255));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(30, 144, 255));
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-3, -3, 6, 6));
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);

        return chart;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminLogs adminLogs = new AdminLogs();
            adminLogs.setVisible(true);
        });
    }
}
