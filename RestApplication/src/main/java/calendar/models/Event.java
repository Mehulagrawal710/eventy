package calendar.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "events")
@Table(name = "events")
public class Event {

	@Id
	@Column(name = "event_id")
	private int eventId;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id")
	User userEntity;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "date")
	private String date;

	@Column(name = "creation_datetime")
	private String creationDatetime;

	public Event(int eventId, User userEntity, String title, String description, String date, String creationDatetime) {
		this.eventId = eventId;
		this.userEntity = userEntity;
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

	public User getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(User userEntity) {
		this.userEntity = userEntity;
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

	public String getCreationDatetime() {
		return creationDatetime;
	}

	public void setCreationDatetime(String creationDatetime) {
		this.creationDatetime = creationDatetime;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", userEntity=" + userEntity + ", title=" + title + ", description="
				+ description + ", date=" + date + ", creationDatetime=" + creationDatetime + "]";
	}

}
