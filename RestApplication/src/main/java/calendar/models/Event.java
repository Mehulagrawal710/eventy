package calendar.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "events")
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_id")
	private int eventId;

	@JsonIgnore
	@Column(name = "user_id")
	int userId;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "date")
	private String date;

	@Column(name = "creation_datetime")
	private LocalDateTime creationDatetime;

	@Transient
	Link link;

	public Event() {

	}

	public Event(int userId, String title, String description, String date, LocalDateTime creationDatetime) {
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.date = date;
		this.creationDatetime = creationDatetime;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public LocalDateTime getCreationDatetime() {
		return creationDatetime;
	}

	public void setCreationDatetime(LocalDateTime creationDatetime) {
		this.creationDatetime = creationDatetime;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link links) {
		this.link = links;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", userId=" + userId + ", title=" + title + ", description=" + description
				+ ", date=" + date + ", creationDatetime=" + creationDatetime + "]";
	}

}
