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
        setTitle("My Storage Bill");
        setSize(1200, 800); // Increased width and height
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // Added padding

        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.decode("#e0f7e9"));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton backButton = new JButton("⬅ Back");
        backButton.setBackground(Color.decode("#81c784"));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("My Storage Bill", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Larger font
        titleLabel.setForeground(Color.decode("#2e7d32"));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Upload section - now in a horizontal layout
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        uploadPanel.setBorder(BorderFactory.createTitledBorder("Upload New Bill"));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JButton selectButton = new JButton("Select Bill from Device");
        selectButton.setBackground(Color.decode("#42a5f5"));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);
        selectButton.setPreferredSize(new Dimension(200, 30));
        selectButton.addActionListener(this::selectFile);

        fileInfoLabel = new JLabel("No file selected");
        fileInfoLabel.setPreferredSize(new Dimension(250, 30));

        JButton uploadButton = new JButton("Upload Selected Bill");
        uploadButton.setBackground(Color.decode("#66bb6a"));
        uploadButton.setForeground(Color.WHITE);
        uploadButton.setFocusPainted(false);
        uploadButton.setPreferredSize(new Dimension(200, 30));
        uploadButton.addActionListener(this::uploadFile);

        uploadPanel.add(new JLabel("Select an image file (JPG, PNG):"));
        uploadPanel.add(selectButton);
        uploadPanel.add(fileInfoLabel);
        uploadPanel.add(uploadButton);

        contentPanel.add(uploadPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // More space between sections

        // Bills list section
        JPanel billsListPanel = new JPanel(new BorderLayout());
        billsListPanel.setBorder(BorderFactory.createTitledBorder("Your Bills"));
        billsListPanel.setBackground(Color.WHITE);

        billsPanel = new JPanel();
        billsPanel.setLayout(new BoxLayout(billsPanel, BoxLayout.Y_AXIS));
        billsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(billsPanel);
        scrollPane.setPreferredSize(new Dimension(1100, 450)); // Wider scroll pane
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        billsListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.setBackground(Color.WHITE);
        JButton refreshButton = new JButton("Refresh Bills List");
        refreshButton.setBackground(Color.decode("#a5d6a7"));
        refreshButton.setFocusPainted(false);
        refreshButton.setPreferredSize(new Dimension(200, 30));
        refreshButton.addActionListener(e -> refreshBillsList());
        refreshPanel.add(refreshButton);
        billsListPanel.add(refreshPanel, BorderLayout.SOUTH);

        contentPanel.add(billsListPanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        refreshBillsList();
        setVisible(true);
    }

    private void refreshBillsList() {
        billsPanel.removeAll();
        List<BillModel> bills = billController.getBillsByUserEmail(userEmail);

        if (bills.isEmpty()) {
            JLabel noBillsLabel = new JLabel("No bills uploaded yet");
            noBillsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            noBillsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            billsPanel.add(noBillsLabel);
        } else {
            for (BillModel bill : bills) {
                // Create card panel
                JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setMaximumSize(new Dimension(1100, 200)); // Wider cards

                // Image preview
                JLabel imageLabel = new JLabel();
                try {
                    ImageIcon imageIcon = new ImageIcon(bill.getFilePath());
                    Image scaledImg = imageIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH); // Larger preview
                    imageLabel.setIcon(new ImageIcon(scaledImg));
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                } catch (Exception e) {
                    imageLabel.setText("Image not available");
                    imageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                }
                cardPanel.add(imageLabel, BorderLayout.WEST);

                // Bill info panel
                JPanel infoPanel = new JPanel(new BorderLayout());
                infoPanel.setBackground(Color.WHITE);

                JLabel billLabel = new JLabel("<html><div style='width:600px;'><b>" + bill.getFileName() + "</b><br>"
                        + "<small>Uploaded: " + bill.getUploadDate() + "</small></div></html>");
                billLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                infoPanel.add(billLabel, BorderLayout.NORTH);

                // Buttons panel
                JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
                buttonsPanel.setBackground(Color.WHITE);

                // Download button
                JButton downloadButton = createStyledButton("Download", "#42a5f5", "#1e88e5");
                downloadButton.setPreferredSize(new Dimension(120, 30));
                downloadButton.addActionListener(e -> downloadBill(bill));
                buttonsPanel.add(downloadButton);

                // Delete button
                JButton deleteButton = createStyledButton("Delete", "#ef5350", "#e53935");
                deleteButton.setPreferredSize(new Dimension(120, 30));
                deleteButton.addActionListener(e -> {
                    if (JOptionPane.showConfirmDialog(this,
                            "Are you sure you want to delete this bill?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        if (billController.deleteBill(bill.getId())) {
                            refreshBillsList();
                            JOptionPane.showMessageDialog(this, "Bill deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete bill", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                buttonsPanel.add(deleteButton);

                infoPanel.add(buttonsPanel, BorderLayout.SOUTH);
                cardPanel.add(infoPanel, BorderLayout.CENTER);

                billsPanel.add(cardPanel);
                billsPanel.add(Box.createRigidArea(new Dimension(0, 15))); // More space between cards
            }
        }

        billsPanel.revalidate();
        billsPanel.repaint();
    }

    private JButton createStyledButton(String text, String bgColor, String hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode(bgColor));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode(hoverColor));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.decode(bgColor));
            }
        });
        return button;
    }

    private void downloadBill(BillModel bill) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Bill As");
        fileChooser.setSelectedFile(new File(bill.getFileName()));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                Files.copy(Paths.get(bill.getFilePath()), fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this, "Bill downloaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error downloading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void selectFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Bill Image");
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