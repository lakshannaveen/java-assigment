package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import models.User;
import org.bson.Document;
import database.MongoDBConnection;
import services.EmailService;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import com.auth0.jwt.interfaces.DecodedJWT;

public class UserController {
    private static final String COLLECTION_NAME = "users";
    private final MongoDatabase database;
    private final EmailService emailService;

    // Secret key for signing the JWT token (hardcoded)
    private static final String SECRET_KEY = "1020"; // anytime change

    // Email configuration (should be moved to config file in production)
    private static final String EMAIL_USERNAME = "trackerexpense3@gmail.com";
    private static final String EMAIL_PASSWORD = "cjys lyfz tqva jiqd";
    private static final String EMAIL_SUBJECT = "Account Created Successfully";
    private static final String EMAIL_BODY = "Dear %s,\n\nYour account has been created successfully!\n\nThank you for registering with us.";

    public UserController() {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
        this.emailService = new EmailService(EMAIL_USERNAME, EMAIL_PASSWORD);
    }

    public boolean registerUser(User user) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);

            // Generate JWT token for the user with email and password
            String token = generateJwtToken(user);

            // Store the user along with the token in MongoDB, including the name
            Document userDoc = new Document("name", user.getName())
                    .append("email", user.getEmail())
                    .append("password", user.getPassword())
                    .append("token", token);

            collection.insertOne(userDoc);

            // Send welcome email in background thread
            new Thread(() -> {
                try {
                    emailService.sendEmail(
                            user.getEmail(),
                            EMAIL_SUBJECT,
                            String.format(EMAIL_BODY, user.getName())
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send email: " + e.getMessage());
                    // Log this error in production
                }
            }).start();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to generate JWT token
    private String generateJwtToken(User user) {
        // Set the expiration time to 1 year (365 days)
        long expirationTime = 365 * 24 * 60 * 60 * 1000L;
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        // Create an algorithm with the secret key
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        // Generate the token
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("email", user.getEmail())
                .withExpiresAt(expirationDate)
                .sign(algorithm);
    }

    // Method to extract email from the JWT token
    public String getEmailFromToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("email").asString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            FindIterable<Document> documents = collection.find();
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                User user = new User(
                        doc.getString("name"),
                        doc.getString("email"),
                        doc.getString("password")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}