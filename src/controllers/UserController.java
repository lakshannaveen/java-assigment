package controllers;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import models.User;
import org.bson.Document;

public class UserController {
    private static final String CONNECTION_STRING =      "mongodb+srv://naveen:uD4DxPM4lBhZ4gOH@cluster0.lbyqk.mongodb.net/test?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "test";
    private static final String COLLECTION_NAME = "users";

    public boolean registerUser(User user) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            Document userDoc = new Document("name", user.getName())
                    .append("email", user.getEmail())
                    .append("password", user.getPassword());

            collection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}