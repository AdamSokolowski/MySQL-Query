package pl.coderslab.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

public class UserGroup {
	private int id;
	private String name;

	public UserGroup(String name) {
		this.name = name;

	}

	public UserGroup() {
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO user_group (name) VALUES (?);";
			String generatedColumns[] = { "ID" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.name);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE user_group SET name=? where id = ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.name);
			preparedStatement.setInt(2, this.id);
			preparedStatement.executeUpdate();
		}
	}

	static public UserGroup loadUserGroupById(Connection conn, int id) throws SQLException {
		String sql = "SELECT	* FROM user_group where id=?";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			UserGroup loadedSolutionGroup = new UserGroup();
			loadedSolutionGroup.id = (resultSet.getInt("id"));
			loadedSolutionGroup.setName(resultSet.getString("name"));

			return loadedSolutionGroup;
		}
		return null;
	}

	static public UserGroup[] loadAllUserGroups(Connection conn) throws SQLException {
		ArrayList<UserGroup> userGroups = new ArrayList<UserGroup>();
		String sql = "SELECT	*	FROM	user_group";
		PreparedStatement preparedStatement;
		preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			UserGroup loadedSolutionGroups = new UserGroup();
			loadedSolutionGroups.id = resultSet.getInt("id");
			loadedSolutionGroups.name = resultSet.getString("name");

			userGroups.add(loadedSolutionGroups);
		}
		UserGroup[] uArray = new UserGroup[userGroups.size()];
		uArray = userGroups.toArray(uArray);
		return uArray;
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE FROM user_group WHERE id= ?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

}
