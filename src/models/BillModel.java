package models;

import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Date;

public class BillModel {
    private ObjectId id;
    private String userEmail;
    private String fileName;
    private String fileType;
    private Date uploadDate;
    private String filePath;

    public BillModel() {}

    public BillModel(String userEmail, String fileName, String fileType, String filePath) {
        this.userEmail = userEmail;
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.uploadDate = new Date();
    }

    // Getters and Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Document toDocument() {
        return new Document("userEmail", userEmail)
                .append("fileName", fileName)
                .append("fileType", fileType)
                .append("filePath", filePath)
                .append("uploadDate", uploadDate);
    }

    public static BillModel fromDocument(Document doc) {
        BillModel bill = new BillModel();
        bill.setId(doc.getObjectId("_id"));
        bill.setUserEmail(doc.getString("userEmail"));
        bill.setFileName(doc.getString("fileName"));
        bill.setFileType(doc.getString("fileType"));
        bill.setFilePath(doc.getString("filePath"));
        bill.setUploadDate(doc.getDate("uploadDate"));
        return bill;
    }
}