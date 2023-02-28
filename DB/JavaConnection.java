package Main;
import java.sql.*;

import javax.swing.JOptionPane;  

public class JavaConnection {
	private Connection connection;
	private Statement statement;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	public void openConnection() {
		try {
			
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","system","Hamza59272"); 
		}
		catch(Exception e) 
		{
			System.out.println("Error opening Connection");
		}
	}
	
	// This one is vulnerable to sql injection and therefore is replaced with the prepared Statements
	// Should only be used when executed by the programmer
	public ResultSet executeQuery(String query) {
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		}
		catch(Exception e)
		{
			System.out.println("Error Executing the Query");
		}
		
		return resultSet;
	}
	public ResultSet executePreparedQuery(String query) {
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = statement.executeQuery(query);
		}
		catch(Exception e)
		{
			System.out.println("Error Executing the Prepared Query");
		}
		
		return resultSet;
	}
	
	// Register Customer Linked With: registerCustomer.java
	public void registerCustomerQuery(String first_name, String last_name, String email_address, String password, String contact_no, int age, String gender, String street, String city, String appartment_address, String house_number){
		try {
			//--------------------- INSERTING INTO USERS --------------------//
			String query = "INSERT INTO Users(user_id, first_name, last_name, email_address, password, contact_no, age, gender, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			ResultSet rs_PersonID = executeQuery("SELECT COUNT(*) FROM Users");
			
			int person_id = 0;
			
			while (rs_PersonID.next()) {
		        person_id = rs_PersonID.getInt("count(*)") + 1;
		    }
			
			System.out.println(person_id);

			preparedStatement.setInt(1, person_id);
			preparedStatement.setString(2, first_name);
			preparedStatement.setString(3, last_name);
			preparedStatement.setString(4, email_address);
			preparedStatement.setString(5, password);
			preparedStatement.setString(6, contact_no);
			preparedStatement.setInt(7, age);			
			preparedStatement.setString(8, gender);
			preparedStatement.setString(9, String.valueOf("Customer"));
			
			System.out.println("Before INSERTING INTO LIVES");
			resultSet = preparedStatement.executeQuery();
			System.out.println("After INSERTING INTO LIVES");
			
			//--------------------- INSERTING INTO CUSTOMER --------------------//
			String insertCustomer = "INSERT INTO Customer(user_id, balance, account_status) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertCustomer);
					
			preparedStatement.setInt(1, person_id);
			preparedStatement.setInt(2, 0);
			preparedStatement.setString(3, "Active");
			
			System.out.println("Before INSERTING INTO CUSTOMER");
			resultSet = preparedStatement.executeQuery();
			System.out.println("After INSERTING INTO CUSTOMER");
			
			//--------------------- INSERTING INTO LOCATION --------------------//
			String insertLocation = "INSERT INTO Location(LOCATION_ID, STREET, CITY, APARTMENT_ADDRESS, HOUSE_NUMBER) VALUES (?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertLocation);
			
			ResultSet rs_LocationID = executeQuery("SELECT COUNT(*) FROM Location");
			
			int location_id = 0;
			
			while (rs_LocationID.next()) {
		        location_id = rs_LocationID.getInt("count(*)") + 1;
		    }
					
			preparedStatement.setInt(1, location_id);
			preparedStatement.setString(2, street);
			preparedStatement.setString(3, city);
			preparedStatement.setString(4, appartment_address);
			preparedStatement.setString(5, house_number);
			
			System.out.println("Before INSERTING INTO LOCATION");
			resultSet = preparedStatement.executeQuery();
			System.out.println("After INSERTING INTO LOCATION");
			
			//--------------------- INSERTING INTO LIVES --------------------//
			String insertLives = "INSERT INTO Lives(CUSTOMER_USER_ID, LOCATION_LOCATION_ID) VALUES (?, ?)";
			preparedStatement = connection.prepareStatement(insertLives);
					
			preparedStatement.setInt(1, person_id);
			preparedStatement.setInt(2, location_id);
			
			System.out.println("Before INSERTING INTO LIVES");
			resultSet = preparedStatement.executeQuery();
			System.out.println("After INSERTING INTO LIVES");
			
		}
		catch(Exception e)
		{
			System.out.println("Error registering the user");
		}
	}
	
	// Register Rider Linked With: registerRider.java
	public void registerRiderQuery(String first_name, String last_name, String email_address, String password, String contact_no, int age, String gender) {
		try {
			//--------------------- INSERTING INTO USERS --------------------//
			String query = "INSERT INTO Users(user_id, first_name, last_name, email_address, password, contact_no, age, gender, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);
			ResultSet rs_PersonID = executeQuery("SELECT COUNT(*) FROM Users");
			
			int person_id = 0;
			
			while (rs_PersonID.next()) {
		        person_id = rs_PersonID.getInt("count(*)") + 1;
		    }
			
			System.out.println(person_id);

			preparedStatement.setInt(1, person_id);
			preparedStatement.setString(2, first_name);
			preparedStatement.setString(3, last_name);
			preparedStatement.setString(4, email_address);
			preparedStatement.setString(5, password);
			preparedStatement.setString(6, contact_no);
			preparedStatement.setInt(7, age);			
			preparedStatement.setString(8, gender);
			preparedStatement.setString(9, String.valueOf("DeliveryBoy"));
			resultSet = preparedStatement.executeQuery();
			
			//--------------------- INSERTING INTO DELIVERYBOY --------------------//
			String insertCustomer = "INSERT INTO DeliveryBoy(user_id, balance, status) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(insertCustomer);
					
			preparedStatement.setInt(1, person_id);
			preparedStatement.setInt(2, 0);
			preparedStatement.setString(3, "Active");
			
			resultSet = preparedStatement.executeQuery();
			
			
		}catch(Exception ex) {
			System.out.println("Error registering the rider");
		}
	}
	
	// Login Customer Linked With: LoginCustomer.java
	public boolean loginCustomer(String email, String password) {
		try {
			String query = "SELECT user_id, email_address, password FROM Users WHERE email_address = ? AND password = ?";
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next() == true)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("Error is generated when the user logged in");
		}
		
		return false;
	}
	//Fetching Menu Details
	public Menu[] FoodDetails() {
		String query = "SELECT * FROM food" ; 
		Menu[] food = new Menu[48];
		int i = 0;
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				food[i] = new Menu(resultSet.getInt("food_id"), resultSet.getString("name"), 
						resultSet.getDouble("price"), resultSet.getString("description"), 
						resultSet.getString("image"), resultSet.getInt("category_category_id"));
				i++;
			}
		} catch (SQLException e) {
			
			System.out.println("Error in fetching data");
		}
		return food;
	}
	// Login Rider Linked With: LoginRider.java
	public boolean loginRider(String email, String password) {
		try {
			String query = "SELECT user_id, email_address, password FROM Users WHERE email_address = ? AND password = ?";
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next() == true)
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("Error is generated when the rider logged in");
		}
		
		return false;
	}
	
	// Login Admin Linked With: LoginAdmin.java
	public boolean loginAdmin(String email, String password) {
		try {
			String query = "SELECT user_id, email_address, password FROM Users WHERE email_address = ? AND password = ?";
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next() == true)
				return true;
			else
				return false;
		}
		catch(Exception loginAdminAccountError)
		{
			System.out.println("Error is generated when the admin logged in");
		}
		
		return false;
	}
	
	
	// AdminPanelUI Linked With: AdminPanelUI.java
	public boolean deleteUser(String email_address, String type) {
		try {
			// ------------------- Remove Data From User, Customer, Lives --------------- //
			String locationExtractQuery = "SELECT location_id FROM users JOIN customer ON customer.user_id = users.user_id JOIN lives ON lives.customer_user_id = customer.user_id JOIN location ON location.location_id = lives.location_location_id";
			ResultSet rs_LocationID = executeQuery(locationExtractQuery);
			
			int location_id = 0;
			
			while (rs_LocationID.next()) {
				location_id = rs_LocationID.getInt("location_id");
		    }
			
			System.out.println(location_id);
			
			String deleteUser = "DELETE FROM Users WHERE email_address = ? and type= ?";
			preparedStatement = connection.prepareStatement(deleteUser);
					
			preparedStatement.setString(1, email_address);
			preparedStatement.setString(2, type);
			resultSet = preparedStatement.executeQuery();
			
			// ------------------- Remove Data From Location --------------- //
			String removeLocation = "DELETE FROM Location WHERE location_id = ?";
			preparedStatement = connection.prepareStatement(removeLocation);
			
			preparedStatement.setInt(1, location_id);
			resultSet = preparedStatement.executeQuery();
			
		}catch(Exception deleteUserAccountError) {
			if(type.equalsIgnoreCase("Customer"))
				System.out.println("Error is generated when deleting a user");
		}
		
		return false;
	}
	
	public boolean deleteDeliveryBoy(String email_address, String type) {
		try {
			// ------------------- Remove Data From Users, DeliveryBoy --------------- //
			String deleteUser = "DELETE FROM Users WHERE email_address = ? and type= ?";
			preparedStatement = connection.prepareStatement(deleteUser);
					
			preparedStatement.setString(1, email_address);
			preparedStatement.setString(2, type);
			resultSet = preparedStatement.executeQuery();
			
		}catch(Exception deleteDeliveryBoyAccountError) {
			if(type.equalsIgnoreCase("DeliveryBoy"))
				System.out.println("Error is generated when deleting a delivery boy");
		}
		return false;
	}
	
	public void insertMenuItem(String name, double price, String description, String imageURL, String category_name) {
		try {
			//--------------------- CHECKING Category --------------------//
			
			String categoryQuery = "SELECT category_id FROM category WHERE name = ?";
			preparedStatement = connection.prepareStatement(categoryQuery);
			preparedStatement.setString(1, category_name);
			resultSet = preparedStatement.executeQuery();
			
			int category_id = 0;
			
			while (resultSet.next()) {
				category_id = resultSet.getInt("category_id");
		    }
			
			System.out.println(category_id);
			
			if(category_id != 0) {
				//--------------------- INSERTING INTO Food --------------------//
				String query = "INSERT INTO Food(FOOD_ID, NAME, PRICE, DESCRIPTION, IMAGE, CATEGORY_CATEGORY_ID) VALUES (?, ?, ?, ?, ?, ?)";
				preparedStatement = connection.prepareStatement(query);
				ResultSet rs_foodID = executeQuery("SELECT COUNT(*) FROM Food");
				
				int food_id = 0;
				
				while (rs_foodID.next()) {
					food_id = rs_foodID.getInt("count(*)") + 1;
			    }
				
				System.out.println(food_id);

				preparedStatement.setInt(1, food_id);
				preparedStatement.setString(2, name);
				preparedStatement.setDouble(3, (double) price);
				preparedStatement.setString(4, description);
				preparedStatement.setString(5, imageURL);
				preparedStatement.setInt(6, category_id);
				resultSet = preparedStatement.executeQuery();
				
				JOptionPane.showMessageDialog(null, "Menu Added Successfully");
			}else {
				JOptionPane.showMessageDialog(null, "Category Doesn't Exist");
			}
		
		}catch(Exception ex) {
			System.out.println("Error registering the rider");
		}
	}
	
	
	public void closeConnection() {
		try
		{
			connection.close();
		}
		catch(Exception e)
		{
			System.out.println("Error while closing the connection");
		}
	}
}
