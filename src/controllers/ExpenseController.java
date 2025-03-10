package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import models.ExpenseModel;
import org.bson.Document;

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
                .append("date", expense.getDate());

        // Insert the document into the collection
        collection.insertOne(expenseDocument);
        System.out.println("Expense added successfully!");
    }
}
