package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import models.AdminModel;
import org.bson.Document;

public class AdminController {
    private static final String COLLECTION_NAME = "admins";
    private final MongoDatabase database;

    public AdminController() {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
    }

    public boolean registerAdmin(AdminModel admin) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Check if username already exists
            Document existingAdmin = collection.find(new Document("username", admin.getUsername())).first();
            if (existingAdmin != null) {
                System.out.println("Username already taken.");
                return false;
            }

            // Insert new admin
            collection.insertOne(admin.toDocument());
            System.out.println("Admin registered successfully!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
