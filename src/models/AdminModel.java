package models;

import org.bson.Document;

public class AdminModel {
    private String username;
    private String password;

    public AdminModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Document toDocument() {
        return new Document("username", username)
                .append("password", password); // Ideally, hash the password before storing
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
