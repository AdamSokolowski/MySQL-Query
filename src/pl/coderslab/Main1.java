package pl.coderslab;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pl.coderslab.models.Excercise;
import pl.coderslab.models.User;

public class Main1 {

	private static Connection conn = null;

	public static void main(String[] args) {
		setDBConn();

		initDatabaseTables();
		
		//addNewExcercise("æwiczenie1", "Pêtle");
		
		showAllExcercises();
		
		
		/*
		if (deleteUser(6)) {
			System.out.println("Usuniêto u¿ytkownika o id 6 lub taki u¿ytkownik nie istnia³");
		}

		/*
		 * if(modifyUser(5, "Kris", null, "kwiatek")) {
		 * System.out.println("Zmodyfikowano konto kris@poczta.com z sukcesem"); } else
		 * { System.out.println("Nie uda³o siê zmodyfikowaæ konta kris@mail.com"); } /
		 **/
		// showAllUsers();

		// showUserById(3);

		// AddNewUser("Krystyna", "krysia@mail.com", "kwiatek");

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {}

	}
	
	
	private static void addNewExcercise(String title, String description) {
		Excercise excercise = new Excercise(title, description);

		try {
			excercise.saveToDB(conn);
		} catch (SQLException e) {}
	}
	
	
	private static void showAllExcercises() {
		try {
			Excercise[] excercises = Excercise.loadAllExcercises(conn);
			if (excercises != null) {
				for (int i = 0; i < excercises.length; i++) {
					System.out.println(excercises[i].getId() + " " + excercises[i].getTitle() + " " + excercises[i].getDescription());
				}
			} else {
				System.out.println("There are no excercises in database");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	

	private static boolean deleteUser(int id) {
		User user = new User();
		try {
			user = User.loadUserById(conn, id);
			if(user!=null) {user.delete(conn);}else {return true;}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (user.getId() == 0);
	}

	private static boolean modifyUser(int id, String newUsername, String newEmail, String newPassword) {
		User user = new User();
		try {
			user = user.loadUserById(conn, id);
		} catch (SQLException e) {
		}
		if (newPassword != null) {
			user.setPassword(newPassword);
		}
		if (newEmail != null) {
			user.setEmail(newEmail);
		}
		if (newUsername != null) {
			user.setUsername(newUsername);
		}

		try {
			user.saveToDB(conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			if (e1.getMessage().startsWith("Duplicate entry") && (e1.getMessage().endsWith("for key 'email'"))) {
				System.out.println("Podany adres email ju¿ jest wykorzystany");
			}
		}
		User userDB = new User();
		try {
			userDB = user.loadUserById(conn, id);
		} catch (SQLException e) {
		}
		return (Integer.compare(user.getId(), userDB.getId()) == 0 && user.getEmail().matches(userDB.getEmail())
				&& user.getPassword().matches(userDB.getPassword())
				&& user.getUsername().matches(userDB.getUsername()));
	}

	private static void showAllUsers() {
		try {
			User[] users = User.loadAllUsers(conn);
			if (users != null) {
				for (int i = 0; i < users.length; i++) {
					System.out.println(users[i].getId() + " " + users[i].getUsername() + " " + users[i].getEmail());
				}
			} else {
				System.out.println("There are no users in database");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private static void showUserById(int id) {
		User user = new User();
		try {
			user = user.loadUserById(conn, id);
		} catch (SQLException e) {
		}
		if (user != null) {
			System.out.println(user.getId() + " " + user.getUsername() + " " + user.getEmail());
		} else {
			System.out.println("U¿ytkownik o podanym Id nie istnieje");
		}
	}

	private static void AddNewUser(String userName, String email, String password) {
		User user = new User(userName, email, password);

		try {
			user.saveToDB(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			if (e.getMessage().startsWith("Duplicate entry") && (e.getMessage().endsWith("for key 'email'"))) {
				System.out.println("Podany adres email ju¿ jest wykorzystany");
			}
		}
	}

	private static void setDBConn() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Warsztaty2?useSSL=false",
					"root", "coderslab");
			conn = connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void initDatabaseTables() {
		if (createTable("user_group",
				"id int(11) AUTO_INCREMENT PRIMARY KEY, name varchar(255) UNIQUE")) {
			System.out.println("Table 'user_group' did not exist. Creating table.");
		}
		
		
		if (createTable("users",
				"id int(11) AUTO_INCREMENT PRIMARY KEY, email varchar(255) UNIQUE, username varchar(255), password varchar(60), user_group_id int(11),"
				+ " FOREIGN KEY(user_group_id) REFERENCES User_Group(id)")) {
			System.out.println("Table 'users' did not exist. Creating table.");
		}
		
		if (createTable("excercise",
				"id int(11) AUTO_INCREMENT PRIMARY KEY, title varchar(255), description TEXT")) {
			System.out.println("Table 'excercise' did not exist. Creating table.");
		}
		
		if (createTable("solution",
				"id int(11) AUTO_INCREMENT PRIMARY KEY, created DATETIME, updated DATETIME, description TEXT, excercise_id int(11), users_id int(11),"
				+ " FOREIGN KEY(excercise_id) REFERENCES excercise(id), FOREIGN KEY(users_id) REFERENCES users(id)")) {
			System.out.println("Table 'solution' did not exist. Creating table.");
		}

	}

	private static boolean tableExists(String tableName) {
		DatabaseMetaData dbm;
		try {
			dbm = conn.getMetaData();

			ResultSet tables = dbm.getTables(null, null, tableName, null);

			if (tables.next()) {
				return true; // table exists
			} else {
				return false; // table not exists
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean createTable(String tableName, String recordsTypesInit) {

		if (tableExists(tableName)) {
			return false;
		} else {
			String sql = "CREATE TABLE " + tableName + " (" + recordsTypesInit + ")";
			try {	
				Statement stat = conn.createStatement();			
				stat.executeUpdate(sql);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}

	}

	private static boolean alterTableAddColumns(String tableName, String newRecordTypesInit) {
		if (tableExists(tableName)) {
			String sql = "ALTER TABLE "+ tableName + " ADD " +newRecordTypesInit;
			try {
				Statement stat = conn.createStatement();			
				stat.executeUpdate(sql);
				return true;
			}catch (SQLException e) {}

		}
		return false;
	}

	private static boolean alterTableDeleteColumn(String tableName, String deleteColumnName) {
		if (tableExists(tableName)) {
			String sql = "ALTER TABLE "+ tableName + " DROP COLUMN " +deleteColumnName;
			try {
				Statement stat = conn.createStatement();			
				stat.executeUpdate(sql);
				return true;
			}catch (SQLException e) {}

		}
		return false;
	}
	


}
