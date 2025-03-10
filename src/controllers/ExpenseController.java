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
                .append("month", expense.getMonth())
                .append("expenseName", expense.getExpenseName())
                .append("expenseType", expense.getExpenseType())
                .append("date", expense.getDate())
                .append("email", expense.getEmail()); // Add email field

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
                    document.getString("month"),
                    document.getString("expenseName"),
                    document.getString("expenseType"),
                    document.getDate("date"),
                    document.getString("email")
            );
            expenses.add(expense);
        }

        return expenses;
    }
}