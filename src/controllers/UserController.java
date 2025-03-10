package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.User;
import org.bson.Document;
import database.MongoDBConnection; // Import the MongoDB connection class

public class UserController {
    private static final String COLLECTION_NAME = "users";
    private final MongoDatabase database; // Make it final to fix the warning

    public UserController() {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
    }

    public boolean registerUser(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document userDoc = new Document("name", user.getName())
                    .append("email", user.getEmail())
                    .append("password", user.getPassword());

            collection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace(); // You can replace this with a proper logging framework
            return false;
        }
    }
}
