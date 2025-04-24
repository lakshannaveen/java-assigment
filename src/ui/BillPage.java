package ui;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import controllers.BillController;
import models.BillModel;
import java.awt.Dialog.ModalityType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
        this.userEmail = getEmailFromToken(token); // Properly decode JWT token
        this.billController = new BillController();

        if (this.userEmail == null) {
            JOptionPane.showMessageDialog(this, "Invalid session. Please login again.", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            new HomePage();
            return;
        }

        initializeUI();
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

    private void initializeUI() {
        setTitle("My Storage Bill - " + userEmail);
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        backButton.addActionListener(e -> {
            new StartPage(token);
            dispose();
        });
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("My Bill Storage", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // Add email label
        JLabel emailLabel = new JLabel(userEmail, SwingConstants.RIGHT);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(Color.WHITE);
        topPanel.add(emailLabel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Upload section
        JPanel uploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        uploadPanel.setBorder(BorderFactory.createTitledBorder("Upload New Bill"));
        uploadPanel.setBackground(Color.WHITE);
        uploadPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton selectButton = createStyledButton("Select Bill from Device", "#388e3c", "#2e7d32", "#4c8c4a");
        selectButton.setPreferredSize(new Dimension(220, 40));
        selectButton.addActionListener(this::selectFile);

        fileInfoLabel = new JLabel("No file selected");
        fileInfoLabel.setPreferredSize(new Dimension(300, 40));
        fileInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton uploadButton = createStyledButton("Upload Selected Bill", "#388e3c", "#2e7d32", "#4c8c4a");
        uploadButton.setPreferredSize(new Dimension(220, 40));
        uploadButton.addActionListener(this::uploadFile);

        uploadPanel.add(new JLabel("Select an image file (JPG, PNG):"));
        uploadPanel.add(selectButton);
        uploadPanel.add(fileInfoLabel);
        uploadPanel.add(uploadButton);

        contentPanel.add(uploadPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Bills list section
        JPanel billsListPanel = new JPanel(new BorderLayout());
        billsListPanel.setBorder(BorderFactory.createTitledBorder("Your Bills"));
        billsListPanel.setBackground(Color.WHITE);

        billsPanel = new JPanel();
        billsPanel.setLayout(new BoxLayout(billsPanel, BoxLayout.Y_AXIS));
        billsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(billsPanel);
        scrollPane.setPreferredSize(new Dimension(1100, 450));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        billsListPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.setBackground(Color.WHITE);
        JButton refreshButton = createStyledButton("Refresh Bills List", "#388e3c", "#2e7d32", "#4c8c4a");
        refreshButton.setPreferredSize(new Dimension(220, 40));
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
                JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                cardPanel.setBackground(Color.WHITE);
                cardPanel.setMaximumSize(new Dimension(1100, 200));

                // Image preview
                JLabel imageLabel = new JLabel();
                try {
                    ImageIcon imageIcon = new ImageIcon(bill.getFilePath());
                    Image scaledImg = imageIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImg));
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);

                    imageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            showZoomedImage(bill.getFilePath());
                        }
                    });
                    imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    imageLabel.setToolTipText("Click to zoom");
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
                JButton downloadButton = createStyledButton("Download", "#1976d2", "#1565c0", "#42a5f5");
                downloadButton.setPreferredSize(new Dimension(140, 35));
                downloadButton.addActionListener(e -> downloadBill(bill));
                buttonsPanel.add(downloadButton);

                // Delete button
                JButton deleteButton = createStyledButton("Delete", "#d32f2f", "#b71c1c", "#f44336");
                deleteButton.setPreferredSize(new Dimension(140, 35));
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
                billsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        billsPanel.revalidate();
        billsPanel.repaint();
    }

    private void showZoomedImage(String imagePath) {
        JDialog zoomDialog = new JDialog(this, "Zoomed Bill", ModalityType.APPLICATION_MODAL);
        zoomDialog.setSize(800, 600);
        zoomDialog.setLocationRelativeTo(this);

        try {
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image image = originalIcon.getImage();

            int dialogWidth = 780;
            int dialogHeight = 560;
            double widthRatio = (double) dialogWidth / image.getWidth(null);
            double heightRatio = (double) dialogHeight / image.getHeight(null);
            double scale = Math.min(widthRatio, heightRatio);

            int scaledWidth = (int) (image.getWidth(null) * scale);
            int scaledHeight = (int) (image.getHeight(null) * scale);

            Image scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            JLabel zoomLabel = new JLabel(new ImageIcon(scaledImage));

            JScrollPane scrollPane = new JScrollPane(zoomLabel);
            scrollPane.setPreferredSize(new Dimension(dialogWidth, dialogHeight));
            zoomDialog.add(scrollPane);

            zoomLabel.setComponentPopupMenu(createImagePopupMenu(imagePath));

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("Could not load image", JLabel.CENTER);
            zoomDialog.add(errorLabel);
        }

        zoomDialog.setVisible(true);
    }

    private JPopupMenu createImagePopupMenu(String imagePath) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem saveItem = new JMenuItem("Save Image As...");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Image As");
            String fileName = new File(imagePath).getName();
            fileChooser.setSelectedFile(new File(fileName));

            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();
                try {
                    Files.copy(Paths.get(imagePath), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(this, "Image saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        popupMenu.add(saveItem);
        return popupMenu;
    }

    private JButton createStyledButton(String text, String bgColor, String hoverColor, String pressedColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(Color.decode(pressedColor));
                } else if (getModel().isRollover()) {
                    g.setColor(Color.decode(hoverColor));
                } else {
                    g.setColor(Color.decode(bgColor));
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };

        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

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
                    userEmail, // Using the email from the decoded token
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