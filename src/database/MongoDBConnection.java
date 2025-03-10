package database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static final String CONNECTION_STRING =
            "mongodb+srv://naveen:uD4DxPM4lBhZ4gOH@cluster0.lbyqk.mongodb.net/test?retryWrites=true&w=majority&appName=Cluster0";
    private static final String DATABASE_NAME = "test";

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection() {
        try {
            ConnectionString connectionString = new ConnectionString(CONNECTION_STRING);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to MongoDB successfully!");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    public static void main(String[] args) {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        mongoDBConnection.closeConnection();
    }
}
