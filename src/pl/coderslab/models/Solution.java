package pl.coderslab.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class Solution {
	private int id;
	private String created;
	private String updated;
	private String description;
	private int excerciseId;
	private int usersId;

	public Solution(String created, String updated, String description, int excerciseId, int usersId) {
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.excerciseId = excerciseId;
		this.usersId = usersId;
	}

	public Solution() {
	};

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExcerciseId() {
		return excerciseId;
	}

	public void setExcerciseId(int excerciseId) {
		this.excerciseId = excerciseId;
	}

	public int getUsersId() {
		return usersId;
	}

	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}

	public int getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO solution(created, updated, description, excercise_id, users_id) VALUES (?,?,?,?,?);";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.created);
			preparedStatement.setString(2, this.updated);
			preparedStatement.setString(3, this.description);
			preparedStatement.setInt(4, this.excerciseId);
			preparedStatement.setInt(5, this.usersId);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE solution SET created=?, updated=?, description=?, excercise_id=?, users_id=? where id =?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.created);
			preparedStatement.setString(2, this.updated);
			preparedStatement.setString(3, this.description);
			preparedStatement.setInt(4, this.excerciseId);
			preparedStatement.setInt(5, this.usersId);
			preparedStatement.setInt(6, this.id);
			preparedStatement.executeUpdate();
		}
	}

	static public Solution loadSolutionById(Connection conn, int id) throws SQLException {
		String sql = "SELECT	* FROM solution where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = (resultSet.getInt("id"));
			loadedSolution.setCreated(resultSet.getString("created"));
			loadedSolution.setDescription(resultSet.getString("description"));
			loadedSolution.setUpdated(resultSet.getString("updated"));
			loadedSolution.setExcerciseId(resultSet.getInt("excercise_id"));
			loadedSolution.setUsersId(resultSet.getInt("users_id"));
			
			return loadedSolution;
		}
		return null;
	}

	static public Solution[] loadAllSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		String sql = "SELECT * FROM solution";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			Solution loadedSolution = new Solution();
			loadedSolution.id = resultSet.getInt("id");
			loadedSolution.created = resultSet.getString("created");
			loadedSolution.description = resultSet.getString("description");
			loadedSolution.updated = resultSet.getString("updated");
			loadedSolution.excerciseId = resultSet.getInt("excercise_id");
			loadedSolution.usersId = resultSet.getInt("users_id");
			solutions.add(loadedSolution);
		}
		Solution[] uArray = new Solution[solutions.size()];
		uArray = solutions.toArray(uArray);
		return uArray;
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM solution WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

}
