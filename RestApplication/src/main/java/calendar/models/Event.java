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
	private LocalDateTime date;

	@Transient
	@Column(name = "status")
	private String status;

	@Column(name = "creation_datetime")
	private LocalDateTime creationDatetime;

	@Column(name = "last_updated_datetime")
	private LocalDateTime lastUpdatedDatetime;

	@Column(name = "no_of_times_updated")
	private int noOfTimesUpdated;

	@JsonIgnore
	@Column(name = "is_notif_active")
	private boolean isNotifActive;

	@JsonIgnore
	@Column(name = "time_before_to_notify")
	private int timeBeforeToNotify;

	@JsonIgnore
	@Column(name = "notif_message")
	private String notifMessage;

	@Transient
	private Link link;

	@Transient
	private Creater creater;

	@Transient
	private Notification notification;

	public Event() {

	}

	public Event(int userId, String title, String description, LocalDateTime date, String status,
			LocalDateTime creationDatetime, boolean isNotifActive, int timeBeforeToNotify, String notifMessage,
			Creater creater, Notification notification) {
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.date = date;
		this.status = status;
		this.creationDatetime = creationDatetime;
		this.isNotifActive = isNotifActive;
		this.timeBeforeToNotify = timeBeforeToNotify;
		this.notifMessage = notifMessage;
		this.creater = creater;
		this.notification = notification;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreationDatetime() {
		return creationDatetime;
	}

	public void setCreationDatetime(LocalDateTime creationDatetime) {
		this.creationDatetime = creationDatetime;
	}

	public LocalDateTime getLastUpdatedDatetime() {
		return lastUpdatedDatetime;
	}

	public void setLastUpdatedDatetime(LocalDateTime lastUpdatedDatetime) {
		this.lastUpdatedDatetime = lastUpdatedDatetime;
	}

	public int getNoOfTimesUpdated() {
		return noOfTimesUpdated;
	}

	public void setNoOfTimesUpdated(int noOfTimesUpdated) {
		this.noOfTimesUpdated = noOfTimesUpdated;
	}

	public boolean getIsNotifActive() {
		return isNotifActive;
	}

	public void setIsNotifActive(boolean isNotifActive) {
		this.isNotifActive = isNotifActive;
	}

	public int getTimeBeforeToNotify() {
		return timeBeforeToNotify;
	}

	public void setTimeBeforeToNotify(int timeBeforeToNotify) {
		this.timeBeforeToNotify = timeBeforeToNotify;
	}

	public String getNotifMessage() {
		return notifMessage;
	}

	public void setNotifMessage(String notifMessage) {
		this.notifMessage = notifMessage;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Creater getCreater() {
		return creater;
	}

	public void setCreater(Creater creater) {
		this.creater = creater;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", userId=" + userId + ", title=" + title + ", description=" + description
				+ ", date=" + date + ", status=" + status + ", creationDatetime=" + creationDatetime
				+ ", lastUpdatedDatetime=" + lastUpdatedDatetime + ", noOfTimesUpdated=" + noOfTimesUpdated
				+ ", isNotifActive=" + isNotifActive + ", timeBeforeToNotify=" + timeBeforeToNotify + ", notifMessage="
				+ notifMessage + ", link=" + link + ", creater=" + creater + ", notification=" + notification + "]";
	}

}
