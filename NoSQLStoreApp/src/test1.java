
import java.net.UnknownHostException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
 
public class test1 {
 
    public static void main(String[] args) throws UnknownHostException {
        // Get a new connection to the db assuming it is running.
        MongoClient mongoClient = new MongoClient("localhost");
        
        // use test as the database. Use your database here.
        MongoDatabase db = mongoClient.getDatabase("test");
 
        MongoCollection<Document> coll = db.getCollection("car");
         
        // insert some test data to start with.
        Document obj = new Document();
        obj.append("name", "Volkswagen");
        obj.append("color", "JetBlue");
        obj.append("cno", "H672");
        obj.append("speed", 62);
        obj.append("mfdcountry", "Italy");
        coll.insertOne(obj);
         
        // findAndModify operation. Update color to blue for cars having speed > 45
        Bson query = new Document("speed",
                new Document("$gt", 45));
        Bson update = new Document("$set", 
        			new Document("color", "Blue"));
        
        System.out.println("before update");
        findAndPrint(coll);
        
        coll.findOneAndUpdate(query, update);
        
        System.out.println("after update of color field");
        findAndPrint(coll);
        
        mongoClient.close();

    }

	private static void findAndPrint(MongoCollection<Document> coll) {
		FindIterable<Document> cursor = coll.find();
        
		for (Document d : cursor)
			System.out.println(d);
	}
}