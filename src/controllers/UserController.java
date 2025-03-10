package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import models.User;
import org.bson.Document;
import database.MongoDBConnection;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UserController {
    private static final String COLLECTION_NAME = "users";
    private final MongoDatabase database;

    // Secret key for signing the JWT token (hardcoded)
    private static final String SECRET_KEY = "1020"; // Change this to a strong secret key

    public UserController() {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
    }

    public boolean registerUser(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Generate JWT token for the user with email and password only
            String token = generateJwtToken(user);

            // Store the user along with the token in MongoDB
            Document userDoc = new Document("email", user.getEmail())
                    .append("password", user.getPassword())
                    .append("token", token);  // Store the generated token

            collection.insertOne(userDoc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();  // You can replace this with a proper logging framework
            return false;
        }
    }

    // Method to generate JWT token
    private String generateJwtToken(User user) {
        // Set the expiration time to 1 year (365 days)
        long expirationTime = 365 * 24 * 60 * 60 * 1000L; // 1 year in milliseconds
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        // Create an algorithm with the secret key
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        // Generate the token
        return JWT.create()
                .withSubject(user.getEmail())  // Use email as the subject
                .withClaim("email", user.getEmail())  // Add email as a claim
                .withExpiresAt(expirationDate)  // Set the expiration date
                .sign(algorithm);  // Sign the token with the secret key
    }
    // Method to extract email from the JWT token
    public String getEmailFromToken(String token) {
        try {
            // Decode the JWT token
            DecodedJWT decodedJWT = JWT.decode(token);
            // Return the email claim from the token
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // Return null if token decoding fails
        }
    }
}
