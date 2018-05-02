package pl.coderslab.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private int userGroupId;
	
	public User(String username, String email, String password){
		this.username = username;
		this.email = email;
		this.setPassword(password);
	}
	
	public User(){};
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(int userGroupId) {
		this.userGroupId = userGroupId;
	}
	public int getId() {
		return id;
	}
	


	public void saveToDB(Connection conn) throws SQLException {
		if (this.id ==0) {
			String sql = "INSERT INTO Users(username, email, password) VALUES (?,?,?);";
			String generatedColumns[] = { "ID" };
		    PreparedStatement preparedStatement;
		    preparedStatement = conn.prepareStatement(sql, generatedColumns);
		    preparedStatement.setString(1,this.username);
		    preparedStatement.setString(2,this.email);
		    preparedStatement.setString(3,this.password);
		    preparedStatement.executeUpdate();
		    ResultSet rs = preparedStatement.getGeneratedKeys();
		    if (rs.next()) {
		    	this.id = rs.getInt(1);
		    }
		}else {
			String sql	= "UPDATE Users SET username=?, email=?, password=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement =	conn.prepareStatement(sql);
			preparedStatement.setString(1,	this.username);
			preparedStatement.setString(2,	this.email);
			preparedStatement.setString(3,	this.password);
			preparedStatement.setInt(4,	this.id);
			preparedStatement.executeUpdate();
		}
	}
	


	static public User loadUserById(Connection conn, int id) throws	SQLException {
		String	sql	=	"SELECT	* FROM Users where id=?";
		PreparedStatement preparedStatement;
		preparedStatement =	conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet	= preparedStatement.executeQuery();
		if	(resultSet.next()) {
			User loadedSolution	= new User();
			loadedSolution.id=(resultSet.getInt("id"));
			loadedSolution.setUsername(resultSet.getString("username"));
			loadedSolution.setPassword(resultSet.getString("password"));
			loadedSolution.setEmail(resultSet.getString("email"));
			return	loadedSolution;
		}
		return	null;
	}
	
	
	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User>	users	=	new	ArrayList<User>();
		String	sql	=	"SELECT	*	FROM	Users";
		PreparedStatement	preparedStatement;
		preparedStatement	=	conn.prepareStatement(sql);
		ResultSet	resultSet	=	preparedStatement.executeQuery();
		while	(resultSet.next())	{
			User	loadedSolution	=	new	User();
			loadedSolution.id	=	resultSet.getInt("id");
			loadedSolution.username	=	resultSet.getString("username");
			loadedSolution.password	=	resultSet.getString("password");
			loadedSolution.email	=	resultSet.getString("email");
			users.add(loadedSolution);}
		User[]	uArray	=	new	User[users.size()];
		uArray	=	users.toArray(uArray);
		return	uArray;}
	
	public void	delete(Connection conn)	throws SQLException	{
		if (this.id	!= 0) {
			String	sql	= "DELETE FROM Users WHERE id= ?";
			PreparedStatement preparedStatement;
						preparedStatement =	conn.prepareStatement(sql);
						preparedStatement.setInt(1,	this.id);
						preparedStatement.executeUpdate();
						this.id=0;
		}
}


	
}
