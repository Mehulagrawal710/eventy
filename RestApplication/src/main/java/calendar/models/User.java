package calendar.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "user")
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id")
	private int UserId;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	public User() {

	}

	public User(int userId, String username, String password) {
		UserId = userId;
		this.username = username;
		this.password = password;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

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
		this.password = password;
	}

}
