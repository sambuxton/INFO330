import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;
import com.mongodb.MongoClientOptions;
import org.bson.Document;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.mongodb.client.MongoCursor;
import com.mongodb.DBObject;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Calendar;

import java.text.DateFormat;

import java.util.LinkedList;
import java.util.Queue;



import com.mongodb.*;
 import com.mongodb.client.MongoCollection;
 import com.mongodb.client.MongoDatabase;
 import com.mongodb.client.model.Projections;
 import com.mongodb.client.model.Filters;
 import static com.mongodb.client.model.Filters.*;
 import static com.mongodb.client.model.Projections.*;
 import com.mongodb.client.model.Sorts;
 import java.util.Arrays;
 import org.bson.Document;

import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;

import java.util.Arrays;


import java.util.Arrays;

import java.util.List;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.Date;

import javax.swing.JOptionPane;



public class DatabaseAccess {
   static String user = "group13";     // the user name
   static String source = "group13";   // the source where the user is defined
   static char[] password = new String("group13").toCharArray(); // the password as a character array
   // ...

	public static Order [] GetPendingOrders()
	{

		// TODO:  Query the database and retrieve the information.
		// resultset.findcolumn(string col)
      int i = 0;
      Order[] allOrders = GetOrders();
      for (Order order : allOrders) {
         if (order.Status.equals("ORDERED")) {
            i++;
         }
      }
      Order[] results = new Order[i];
      int j = 0;
      for (Order order2 : allOrders) {
         if (order2.Status.equals("ORDERED")) {
            results[j] = order2;
            j++;
         }
      }
      return results;
   }







	public static Product[] GetProducts()
	{
   String user = "group13";     // the user name
   String source = "group13";   // the source where the user is defined
   char[] password = new String("group13").toCharArray(); // the password as a character array


   MongoCredential credential = MongoCredential.createCredential(user, source, password);
   MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("info330c.ischool.uw.edu", 27017))))
                .credential(credential)
                .build());

   MongoDatabase database = mongoClient.getDatabase("group13");
   MongoCollection<Document> coll = database.getCollection("Products");
   int j = 0;


   Product[] results = new Product[Math.toIntExact(coll.countDocuments())];

   for (Document document : coll.find()) {
      Product p = new Product();

      p.ProductID = (document.getDouble("productID").intValue());
      p.InStock = document.getDouble("inStock").intValue();
      p.Description = document.getString("description");
      p.Name = document.getString("name");
      p.Price = document.getDouble("price");
      p.Relavance = document.getDouble("relevance");

      int i = 0;
      if(document.get("productReviews", List.class) != null) {
      String[] userComments = new String[document.get("productReviews", List.class).toArray().length];
      for (Object string : document.get("productReviews", List.class).toArray()) {
         String otherString = string.toString();
         userComments[i] = otherString;
         i++;
      }
      p.UserComments = userComments;
      }
      if(document.getDouble("relevance") != null) {
         p.Relavance = document.getDouble("relevance");
      }
      results[j] = p;
      j++;
   }

   return results;


		// TODO:  Retrieve all the information about the products.

	}
   public static Order[] GetOrders() {
      MongoCredential credential = MongoCredential.createCredential(user, source, password);
      MongoClient mongoClient = MongoClients.create(
           MongoClientSettings.builder()
                   .applyToClusterSettings(builder ->
                           builder.hosts(Arrays.asList(new ServerAddress("info330c.ischool.uw.edu", 27017))))
                   .credential(credential)
                   .build());

      MongoDatabase database = mongoClient.getDatabase("group13");
      MongoCollection<Document> coll = database.getCollection("Orders");
      int j = 0;


      Order[] results = new Order[Math.toIntExact(coll.countDocuments())];

      for (Document document : coll.find()) {
         Order o = new Order();

      	// Customer Customer; this will be easier once I've finished the getCustomerDetails method


         o.OrderID = (document.getDouble("orderID").intValue());

         String dateString = document.getString("orderDate");
         DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
         Date startDate = new SimpleDate();
         try{
            startDate = df.parse(dateString);
         }
         catch (ParseException e){
            System.out.println("Date error");
         }



         o.OrderDate = startDate;
         o.TotalCost = document.getDouble("totalCost");

         o.Status = document.getString("status");
         o.BillingAddress = document.getString("billingAddress");
         o.BillingInfo = document.getString("billingInfo");
         o.ShippingAddress = document.getString("shippingAddress");

         for (Customer c : GetCustomers()) {
            if(c.CustomerID == document.getDouble("customerID").intValue()) {
               o.Customer = c;
            }
         }


         Document n = coll.find(new BasicDBObject("orderID", o.OrderID)).iterator().next();
         Document q = (Document)n.get("lineItems");
         int size = q.getDouble("size").intValue();
         LineItem[] lineItems = new LineItem[size];
         int i = 0;
         for (String s : q.keySet()) {
            if (!s.equals("size")) {
               LineItem item = new LineItem();
               Document b = (Document)q.get(s);
               item.Order = o;
               Product product = GetProductDetails(b.getDouble("productID").intValue());
               item.Product = product;
               item.Quantity = b.getDouble("quantity").intValue();
               item.PricePaid = product.Price * item.Quantity;
               lineItems[i] = item;
               i++;
             }
         }
         o.LineItems = lineItems;

         results[j] = o;
         j++;
      }
      return results;


   }

	public static Order GetOrderDetails(int OrderID)
	{
		// TODO:  Query the database to get the flight information as well as all
		// the reservations.
      Order[] allOrders = GetOrders();
      for (Order order : allOrders) {
         if (order.OrderID == OrderID) {
            return order;
         }
      }
      return null;

	}

	public static Product GetProductDetails (int ProductID)
	{
      for (Product product : GetProducts()) {
         if (product.ProductID == ProductID) {
            return product;
         }
      }
      return null;
	}

	public static Customer [] GetCustomers ()
	{
		// TODO:  Query the database to retrieve a list of customers.
      MongoCredential credential = MongoCredential.createCredential(user, source, password);
      MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("info330c.ischool.uw.edu", 27017))))
                .credential(credential)
                .build());

      MongoDatabase database = mongoClient.getDatabase("group13");
      MongoCollection<Document> coll = database.getCollection("Customers");

      //The querying part
      int j = 0;


      Customer[] results = new Customer[Math.toIntExact(coll.countDocuments())];

      for (Document document : coll.find()) {
         Customer c = new Customer();


         c.CustomerID = (document.getDouble("customerID").intValue());
         c.Name = document.getString("name");
         c.Email = document.getString("email");



      results[j] = c;
      j++;
      }
      return results;
   }





	public static Order [] GetCustomerOrders (Customer c)
	{
      int size = 0;
      Order[] allOrders = GetOrders();
      for (Order order : allOrders) {
         if (order.Customer.CustomerID == c.CustomerID) {
            size++;
         }
      }
      Order[] results = new Order[size];
      int i = 0;
      for (Order order : allOrders) {
         if (order.Customer.CustomerID == c.CustomerID) {
            results[i] = order;
            i++;
         }
      }
      return results;

	}

   //This is not on the deliverables, I'm assuming it's just for fun?
	public static Product [] SearchProductReviews(String query)
	{
		// DUMMY VALUES
		Product p = new Product();
		p.Description = "A great monitor";
		p.Name = "Monitor, 19 in";
		p.InStock = 10;
		p.Price = 196;
		p.ProductID = 1;
		p.Relavance = 0.7;
		return new Product [] { p} ;
	}

	public static void MakeOrder(Customer c, LineItem [] LineItems)
	{

		/* TODO: Insert data into your database.
		// Show an error message if you can not make the reservation.
      // No partial orders!

      // save values from customer c and the check if any LineItems are out of stock
      // If they are, send message that order cannot be placed
      // Otherwise, use
      db.Products.findAndModify( {
                               query: {productID : {$in : ProductID[]} inStock : {$gte : 2.0}},

                               update: {inStock : 5.0, description : "A gizmo",
                                         "name" : "gizmo", "price" : 50.00, productID : 2.0},
                               "new": false

                           } );


      query Products to see if it is 1. If it is, then update */
      //Find product via productID and check that there are enough
      //Need for loop to query stock of each line item
      MongoCredential credential = MongoCredential.createCredential(user, source, password);
      MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("info330c.ischool.uw.edu", 27017))))
                .credential(credential)
                .build());
      MongoDatabase database = mongoClient.getDatabase("group13");
      MongoCollection<Document> coll = database.getCollection("Products");

      //Updating the stock of everything that can be updated
      Queue<Integer> notUpdated = new LinkedList<Integer>();
      Queue<Integer> updated = new LinkedList<Integer>();
      for (LineItem item : LineItems) {
         double price = item.Product.Price;
         int prodID = item.Product.ProductID;
         int numRequested = item.Quantity;

         Document query = new Document("productID", prodID)
                                       .append("inStock", new Document("$gte", numRequested));

         Document update = new Document("$inc", new Document("inStock", -1*numRequested));

         Document result = coll.findOneAndUpdate(query, update);


         if (result == null) {
            notUpdated.add(prodID);
         }
         else {
            updated.add(prodID);
         }
      }
      MongoCollection<Document> collOrders = database.getCollection("Orders");
      // Collect the variables I need (customer and lineItem details);

      //Calculating totalCost

      double totalCost = 0.0;
      for (LineItem item: LineItems) {
         totalCost = totalCost + item.Product.Price;
      }
      //Getting address and billing information
      String billingInfo;
      billingInfo = JOptionPane.showInputDialog("Billing Info");
      String billingAddress;
      billingAddress = JOptionPane.showInputDialog("Billing Address");
      String shippingAddress;
      shippingAddress = JOptionPane.showInputDialog("Shipping Address");

      //Getting date
      String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

      Double customerID = new Double(c.CustomerID);


      int orderID = 50;
      Order o = new Order();
      //getting line item doc ready
      Document itemAdd = new Document();
      int size = updated.size();
      for (int i = 0; i < size ; i++) {
         LineItem updatingItem = new LineItem();
         int productID = updated.remove();
         for(LineItem item : LineItems){

            if (item.Product.ProductID == productID) {
               updatingItem = item;
            }
         }
         Double doubleQuantity = Double.valueOf(updatingItem.Quantity);
         Double doubleProductID = Double.valueOf(updatingItem.Product.ProductID);
         itemAdd.append("lineItem" + i, new Document("quantity", doubleQuantity)
                                        .append("productID", doubleProductID));
     }
     itemAdd.append("size", Double.valueOf(size));

     //Getting fresh orderID
     int newOrderID = 0;
     for (Order order : GetOrders()) {
      if (order.OrderID >= newOrderID) {
         newOrderID = order.OrderID + 1;
      }
     }



      Document insertion = new Document("orderID", Double.valueOf(newOrderID))
         .append("orderDate", date)
         .append("status", "ORDERED")
         .append("totalCost", totalCost)
         .append("billingAddress", billingAddress)
         .append("billingInfo", billingInfo)
         .append("shippingAddress", shippingAddress)
         .append("customerID", customerID)
         .append("lineItems", itemAdd);

      //Order details uploaded
      collOrders.insertOne(insertion);

      Document query2 = new Document("orderID", orderID);

      if(notUpdated.size() == 0) {
		   JOptionPane.showMessageDialog(null, "Create order for " + c.Name + " for " + Integer.toString(LineItems.length) + " items.");
      }
      else {
         int size2 = notUpdated.size();
         int[] noUpdated = new int[size2];
         for (int i = 0; i < size2; i++) {
            noUpdated[i] = notUpdated.remove();
         }

         JOptionPane.showMessageDialog(null, "Create order for " + c.Name + " for " + Integer.toString(LineItems.length) + " items. " +
                                       "Out of stock on the items with the following ID's: " + Arrays.toString(noUpdated));
	}
}
}
