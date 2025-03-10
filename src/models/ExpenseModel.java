package models;

import java.util.Date;

public class ExpenseModel {
    private String pocketName;
    private String month;
    private String expenseName;
    private String expenseType;
    private Date date;
    private String email; // Add email field

    public ExpenseModel(String pocketName, String month, String expenseName, String expenseType, Date date, String email) {
        this.pocketName = pocketName;
        this.month = month;
        this.expenseName = expenseName;
        this.expenseType = expenseType;
        this.date = date;
        this.email = email;
    }

    public String getPocketName() {
        return pocketName;
    }

    public void setPocketName(String pocketName) {
        this.pocketName = pocketName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
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