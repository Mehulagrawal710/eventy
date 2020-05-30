package calendar.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "tokens")
@Table(name = "tokens")
public class Token {

	@Id
	@Column(name = "token")
	private String token;

	@JsonIgnore
	@Column(name = "user_id")
	private int userId;

	@Transient
	private String message;

	@Column(name = "issued")
	private LocalDateTime issued;

	@Column(name = "time_to_live")
	private int timeToLive;

	@Column(name = "expires")
	private LocalDateTime expires;

	public Token() {

	}

	public Token(String token, int userId, LocalDateTime issued, int timeToLive, LocalDateTime expires) {
		this.token = token;
		this.userId = userId;
		this.issued = issued;
		this.timeToLive = timeToLive;
		this.expires = expires;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getIssued() {
		return issued;
	}

	public void setIssued(LocalDateTime issued) {
		this.issued = issued;
	}

	public int getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}

	public LocalDateTime getExpires() {
		return expires;
	}

	public void setExpires(LocalDateTime expires) {
		this.expires = expires;
	}

	@Override
	public String toString() {
		return "Token [token=" + token + ", userId=" + userId + ", issued=" + issued + ", timeToLive=" + timeToLive
				+ ", expires=" + expires + "]";
	}

}
