package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class Expense extends JFrame {

    private JComboBox<String> monthComboBox;
    private JSpinner dateSpinner;

    public Expense() {
        setTitle("Expense Tracker");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel and apply styles
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create the back button
        JButton backButton = new JButton("Back");
        ExpenseStyle.styleButton(backButton);

        // Add action listener to the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StartPage();
                dispose(); // Close the current window
            }
        });

        // Create a panel for the back button and add the button to it
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.add(backButton);

        // Add the back button panel to the main panel
        mainPanel.add(backButtonPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Pocket name
        JLabel pocketNameLabel = new JLabel("Pocket Name:");
        JTextField pocketNameField = new JTextField();
        ExpenseStyle.addPlaceholder(pocketNameField, "Enter pocket name");
        formPanel.add(pocketNameLabel);
        formPanel.add(pocketNameField);

        // Select a month (only current month)
        JLabel monthLabel = new JLabel("Select Month:");
        monthComboBox = new JComboBox<>();
        populateMonthComboBox();
        formPanel.add(monthLabel);
        formPanel.add(monthComboBox);

        // Expense name (optional)
        JLabel expenseNameLabel = new JLabel("Expense Name (optional):");
        JTextField expenseNameField = new JTextField();
        ExpenseStyle.addPlaceholder(expenseNameField, "Enter expense name");
        formPanel.add(expenseNameLabel);
        formPanel.add(expenseNameField);

        // Expense type (optional)
        JLabel expenseTypeLabel = new JLabel("Expense Type (optional):");
        JTextField expenseTypeField = new JTextField();
        ExpenseStyle.addPlaceholder(expenseTypeField, "Enter expense type");
        formPanel.add(expenseTypeLabel);
        formPanel.add(expenseTypeField);

        // Select date within the month (optional)
        JLabel dateLabel = new JLabel("Select Date (optional):");
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy");
        dateSpinner.setEditor(dateEditor);
        setDateRange();
        formPanel.add(dateLabel);
        formPanel.add(dateSpinner);

        // Add form panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit");
        ExpenseStyle.styleButton(submitButton);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle form submission
                String pocketName = pocketNameField.getText();
                String selectedMonth = (String) monthComboBox.getSelectedItem();
                String expenseName = expenseNameField.getText();
                String expenseType = expenseTypeField.getText();
                Date selectedDate = (Date) dateSpinner.getValue();

                // Validate fields
                if (pocketName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Pocket Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (selectedMonth == null) {
                    JOptionPane.showMessageDialog(null, "Month selection is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // For now, just print the values
                System.out.println("Pocket Name: " + pocketName);
                System.out.println("Month: " + selectedMonth);
                System.out.println("Expense Name: " + expenseName);
                System.out.println("Expense Type: " + expenseType);
                System.out.println("Date: " + selectedDate);
            }
        });

        // Add submit button to the main panel
        JPanel submitButtonPanel = new JPanel();
        submitButtonPanel.add(submitButton);
        mainPanel.add(submitButtonPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);
        setVisible(true);
    }

    private void populateMonthComboBox() {
        LocalDate now = LocalDate.now();
        YearMonth currentYearMonth = YearMonth.of(now.getYear(), now.getMonth());
        monthComboBox.addItem(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());
        monthComboBox.setSelectedIndex(0);
        monthComboBox.setEnabled(false); // Disable to make sure only current month can be selected
    }

    private void setDateRange() {
        LocalDate now = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, now.getYear());
        calendar.set(Calendar.MONTH, now.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();
        ((SpinnerDateModel) dateSpinner.getModel()).setStart(startDate);
        ((SpinnerDateModel) dateSpinner.getModel()).setEnd(endDate);
        dateSpinner.setValue(startDate);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Expense();
            }
        });
    }
}