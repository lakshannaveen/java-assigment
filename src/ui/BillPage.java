package ui;

import controllers.BillController;
import models.BillModel;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class BillPage extends JFrame {
    private final String token;
    private final String userEmail;
    private final BillController billController;
    private JLabel fileInfoLabel;
    private File selectedFile;
    private JPanel billsPanel;

    public BillPage(String token) {
        this.token = token;
        this.userEmail = extractEmailFromToken(token);
        this.billController = new BillController();

        initializeUI();
    }

    private String extractEmailFromToken(String token) {
        int emailIndex = token.indexOf("email:") + 6;
        return (emailIndex >= 6) ? token.substring(emailIndex).trim() : "unknown@example.com";
    }

    private void initializeUI() {
        setTitle("Bill Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#e0f7e9")); // light green
        JButton backButton = new JButton("⬅ Back");
        backButton.setBackground(Color.decode("#81c784"));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Bill Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.decode("#2e7d32"));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton uploadButton = new JButton("Select Bill File");
        fileInfoLabel = new JLabel("No file selected");
        fileInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::selectFile);

        JButton saveButton = new JButton("Upload Bill");
        saveButton.setBackground(Color.decode("#66bb6a"));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(this::uploadFile);

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setBackground(Color.decode("#a5d6a7"));
        refreshButton.addActionListener(e -> refreshBillsList());

        contentPanel.add(new JLabel("Upload your bill (JPG, PNG only):"));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(uploadButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        contentPanel.add(fileInfoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(saveButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(refreshButton);

        billsPanel = new JPanel();
        billsPanel.setLayout(new BoxLayout(billsPanel, BoxLayout.Y_AXIS));
        billsPanel.setBorder(BorderFactory.createTitledBorder("Your Bills"));

        JScrollPane scrollPane = new JScrollPane(billsPanel);
        scrollPane.setPreferredSize(new Dimension(760, 300));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        refreshBillsList();
        setVisible(true);
    }

    private void refreshBillsList() {
        billsPanel.removeAll();
        List<BillModel> bills = billController.getBillsByUserEmail(userEmail);

        if (bills.isEmpty()) {
            billsPanel.add(new JLabel("No bills uploaded yet"));
        } else {
            for (BillModel bill : bills) {
                JPanel billPanel = new JPanel(new BorderLayout());
                billPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                // Image Preview
                JLabel imageLabel = new JLabel();
                ImageIcon imageIcon = new ImageIcon(bill.getFilePath());
                Image scaledImg = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImg));

                billPanel.add(imageLabel, BorderLayout.WEST);

                JLabel billLabel = new JLabel("<html><b>" + bill.getFileName() + "</b><br>" + bill.getUploadDate() + "</html>");
                billPanel.add(billLabel, BorderLayout.CENTER);

                JButton deleteButton = new JButton("Delete");
                deleteButton.setBackground(Color.decode("#ef5350"));
                deleteButton.setForeground(Color.WHITE);
                deleteButton.setFocusPainted(false);
                deleteButton.setOpaque(true);
                deleteButton.setBorderPainted(false);
                deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        deleteButton.setBackground(Color.decode("#e53935"));
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        deleteButton.setBackground(Color.decode("#ef5350"));
                    }
                });
                deleteButton.addActionListener(e -> {
                    if (billController.deleteBill(bill.getId())) {
                        refreshBillsList();
                        JOptionPane.showMessageDialog(this, "Bill deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete bill", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                billPanel.add(deleteButton, BorderLayout.EAST);
                billsPanel.add(billPanel);
                billsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        billsPanel.revalidate();
        billsPanel.repaint();
    }

    private void selectFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return f.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
            }

            @Override
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png)";
            }
        });

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileInfoLabel.setText("Selected: " + selectedFile.getName() + " (" + (selectedFile.length() / 1024) + " KB)");
        }
    }

    private void uploadFile(ActionEvent e) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File uploadDir = new File("uploads");
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String fileExt = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));
            String newFileName = "bill_" + System.currentTimeMillis() + fileExt;
            Path targetPath = Paths.get("uploads", newFileName);
            Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            BillModel bill = new BillModel(
                    userEmail,
                    selectedFile.getName(),
                    Files.probeContentType(selectedFile.toPath()),
                    targetPath.toString()
            );

            String billId = billController.saveBill(bill);
            if (billId != null) {
                JOptionPane.showMessageDialog(this, "Bill uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                selectedFile = null;
                fileInfoLabel.setText("No file selected");
                refreshBillsList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save bill to database", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error uploading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
