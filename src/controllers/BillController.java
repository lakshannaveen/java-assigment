package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.MongoDBConnection;
import models.BillModel;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class BillController {
    private static final String COLLECTION_NAME = "bills";
    private final MongoDatabase database;

    public BillController() {
        MongoDBConnection mongoDBConnection = new MongoDBConnection();
        this.database = mongoDBConnection.getDatabase();
    }

    public String saveBill(BillModel bill) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document doc = bill.toDocument();
            collection.insertOne(doc);
            return doc.getObjectId("_id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BillModel> getBillsByUserEmail(String userEmail) {
        List<BillModel> bills = new ArrayList<>();
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            for (Document doc : collection.find(eq("userEmail", userEmail))) {
                bills.add(BillModel.fromDocument(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bills;
    }

    public boolean deleteBill(ObjectId billId) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document result = collection.findOneAndDelete(eq("_id", billId));
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public BillModel getBillById(ObjectId billId) {
        try {
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document doc = collection.find(eq("_id", billId)).first();
            return doc != null ? BillModel.fromDocument(doc) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}