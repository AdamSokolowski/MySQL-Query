package pl.coderslab.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class Excercise {
	private int id;
	private String title;
	private String description;
	
	public Excercise(String title, String description){
		this.title = title;
		this.description = description;
	}
	
	public Excercise(){};
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}


	public void saveToDB(Connection conn) throws SQLException {
		if (this.id ==0) {
			String sql = "INSERT INTO excercise(title, description) VALUES (?,?);";
			String generatedColumns[] = { "ID" };
		    PreparedStatement preparedStatement;
		    preparedStatement = conn.prepareStatement(sql, generatedColumns);
		    preparedStatement.setString(1,this.title);
		    preparedStatement.setString(2,this.description);
		    preparedStatement.executeUpdate();
		    ResultSet rs = preparedStatement.getGeneratedKeys();
		    if (rs.next()) {
		    	this.id = rs.getInt(1);
		    }
		}else {
			String sql	= "UPDATE excercise SET title=?, description=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement =	conn.prepareStatement(sql);
			preparedStatement.setString(1,	this.title);
			preparedStatement.setString(2,	this.description);
			preparedStatement.setInt(3,	this.id);
			preparedStatement.executeUpdate();
		}
	}
	


	static public Excercise loadExcerciseById(Connection conn, int id) throws	SQLException {
		String	sql	=	"SELECT	* FROM excercise where id=?";
		PreparedStatement preparedStatement;
		preparedStatement =	conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet	= preparedStatement.executeQuery();
		if	(resultSet.next()) {
			Excercise loadedExcercise	= new Excercise();
			loadedExcercise.id=(resultSet.getInt("id"));
			loadedExcercise.setTitle(resultSet.getString("title"));
			loadedExcercise.setDescription(resultSet.getString("description"));
			return	loadedExcercise;
		}
		return	null;
	}
	
	
	
	public Solution[] loadAllByUserId(Connection conn, int id) throws	SQLException {
		ArrayList<Solution>	excercises	=	new	ArrayList<Solution>();
		String	sql	=	"SELECT	* FROM solution where excercise_id=? users_id=?";
		PreparedStatement preparedStatement;
		preparedStatement =	conn.prepareStatement(sql);
		preparedStatement.setInt(1, this.id);
		preparedStatement.setInt(2, id);
		
		ResultSet resultSet	= preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution	= new Solution();
			loadedSolution.id=(resultSet.getInt("id"));
			loadedSolution.setTitle(resultSet.getString("title"));
			loadedSolution.setDescription(resultSet.getString("description"));
			return	loadedExcercise;
		}
		return	null;
	}
	
	
	
	static public Excercise[] loadAllExcercises(Connection conn) throws SQLException {
		ArrayList<Excercise>	excercises	=	new	ArrayList<Excercise>();
		String	sql	=	"SELECT	*	FROM	excercise";
		PreparedStatement	preparedStatement;
		preparedStatement	=	conn.prepareStatement(sql);
		ResultSet	resultSet	=	preparedStatement.executeQuery();
		while	(resultSet.next())	{
			Excercise	loadedExcercise	=	new	Excercise();
			loadedExcercise.id	=	resultSet.getInt("id");
			loadedExcercise.title	=	resultSet.getString("title");
			loadedExcercise.description	=	resultSet.getString("description");
			excercises.add(loadedExcercise);}
		Excercise[]	uArray	=	new	Excercise[excercises.size()];
		uArray	=	excercises.toArray(uArray);
		return	uArray;}
	
	public void	delete(Connection conn)	throws SQLException	{
		if (this.id	!= 0) {
			String	sql	= "DELETE FROM excercise WHERE id= ?";
			PreparedStatement preparedStatement;
						preparedStatement =	conn.prepareStatement(sql);
						preparedStatement.setInt(1,	this.id);
						preparedStatement.executeUpdate();
						this.id=0;
		}
}

	
}
