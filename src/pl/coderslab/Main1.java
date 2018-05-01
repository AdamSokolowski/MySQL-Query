package pl.coderslab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import pl.coderslab.models.User;

public class Main1 {
	
	private static Connection conn=null;

	public static void main(String[] args) {
		setDBConn();
		
		User user = new User();
		try{user = user.loadUserById(conn,5);}catch (SQLException e){}
		System.out.println(user.getId()+" "+user.getUsername()+" "+ user.getEmail());
		
		//User user = new User("Hania","hania@mail.com","pass");
		//try {
		//	user.saveToDB(conn);
		//} catch (SQLException e) {e.printStackTrace();}
		try{if (conn!=null) {conn.close();}}catch (SQLException e){}

	}
	
	private static void setDBConn(){
		try  {
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Warsztaty2?useSSL=false","root","coderslab");
			conn = connection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	

}
