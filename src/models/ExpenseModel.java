package models;

import java.util.Date;

public class ExpenseModel {
    private String pocketName;
    private String selectedMonth;
    private String expenseName;
    private double amount; // Add amount field
    private Date date;
    private String email;

    public ExpenseModel(String pocketName, String selectedMonth, String expenseName, double amount, Date date, String email) {
        this.pocketName = pocketName;
        this.selectedMonth = selectedMonth;
        this.expenseName = expenseName;
        this.amount = amount;
        this.date = date;
        this.email = email;
    }

    // Getters and Setters
    public String getPocketName() {
        return pocketName;
    }

    public void setPocketName(String pocketName) {
        this.pocketName = pocketName;
    }

    public String getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}