package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import database.MongoDBConnection;
import models.ExpenseModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
    private MongoDatabase database;

    public ExpenseController() {
        // Create an instance of MongoDBConnection to get the database connection
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
    }

    public void addExpense(ExpenseModel expense) {
        // Get the "expenses" collection
        MongoCollection<Document> collection = database.getCollection("expenses");

        // Create a document representing the expense data
        Document expenseDocument = new Document()
                .append("pocketName", expense.getPocketName())
                .append("selectedMonth", expense.getSelectedMonth()) // Update field name
                .append("expenseName", expense.getExpenseName())
                .append("amount", expense.getAmount()) // Add amount field
                .append("date", expense.getDate())
                .append("email", expense.getEmail());

        // Insert the document into the collection
        collection.insertOne(expenseDocument);
        System.out.println("Expense added successfully!");
    }

    public List<ExpenseModel> getExpensesByEmail(String email) {
        // Get the "expenses" collection
        MongoCollection<Document> collection = database.getCollection("expenses");

        // Query the collection for documents with the specified email
        List<Document> documents = collection.find(Filters.eq("email", email)).into(new ArrayList<>());

        // Convert documents to ExpenseModel objects
        List<ExpenseModel> expenses = new ArrayList<>();
        for (Document document : documents) {
            ExpenseModel expense = new ExpenseModel(
                    document.getString("pocketName"),
                    document.getString("selectedMonth"),
                    document.getString("expenseName"),
                    document.getDouble("amount"), // Update field name
                    document.getDate("date"),
                    document.getString("email")
            );
            expenses.add(expense);
        }

        return expenses;
    }
    public void deleteExpense(String email, String expenseName, String selectedMonth) {
        MongoCollection<Document> collection = database.getCollection("expenses");
        collection.deleteOne(Filters.and(
                Filters.eq("email", email),
                Filters.eq("expenseName", expenseName),
                Filters.eq("selectedMonth", selectedMonth)
        ));
        System.out.println("Expense deleted successfully!");
    }

    public void deletePocket(String email, String pocketName) {
        MongoCollection<Document> collection = database.getCollection("expenses");
        collection.deleteMany(Filters.and(
                Filters.eq("email", email),
                Filters.eq("pocketName", pocketName)
        ));
        System.out.println("Pocket deleted successfully!");
    }
}