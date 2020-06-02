package calendar.services;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.hibernate.Session;

import calendar.DAO.EventsDAO;
import calendar.models.Creater;
import calendar.models.Event;
import calendar.models.Link;
import calendar.models.Notification;
import calendar.models.User;

public class EventsService {

	EventsDAO eventsDao = new EventsDAO();

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public List<Event> getAllEventsByUserId(int userId, UriInfo uri, Session session) {
		List<Event> eventList = eventsDao.getAllEventsByUserId(userId, session);
		User user = session.get(User.class, userId);
		for (Event event : eventList) {
			if (event.getDate().compareTo(LocalDateTime.now()) > 0) {
				event.setStatus("upcoming");
			} else {
				event.setStatus("passed");
			}
			Creater creater = new Creater(user.getUsername(), user.getEmail());
			Notification notification = new Notification(event.getIsNotifActive(), event.getTimeBeforeToNotify(),
					event.getNotifMessage());
			event.setCreater(creater);
			event.setNotification(notification);
			String selfUri = uri.getAbsolutePath().toString() + "/" + event.getEventId();
			event.setLink(new Link(selfUri, "self", "GET"));
		}
		return eventList;
	}

	public Event createNewEvent(int userId, String title, String description, String dateString, boolean isNotifActive,
			int timeBeforeToNotify, String notifMessage, UriInfo uri, Session session) throws ParseException {
		LocalDateTime date = LocalDateTime.parse(dateString, formatter);
		LocalDateTime creationDatetime = LocalDateTime.now();
		String status = null;
		if (date.compareTo(creationDatetime) > 0) {
			status = "upcoming";
		} else {
			status = "passed";
		}
		User user = session.get(User.class, userId);
		Creater creater = new Creater(user.getUsername(), user.getEmail());
		Notification notification = new Notification(isNotifActive, timeBeforeToNotify, notifMessage);

		Event newEvent = new Event(userId, title, description, date, status, creationDatetime, isNotifActive,
				timeBeforeToNotify, notifMessage, creater, notification);

		String selfUri = uri.getAbsolutePath().toString() + "/" + newEvent.getEventId();
		newEvent.setLink(new Link(selfUri, "self", "GET"));

		eventsDao.createNewEvent(newEvent, session);
		return newEvent;
	}

	public Event updateEvent(int eventId, String title, String description, boolean isNotifActive,
			int timeBeforeToNotify, String notifMessage, int userId, UriInfo uri, Session session) {
		Event event = session.get(Event.class, eventId);
		// When no such event with provided event Id is present at all
		if (event == null) {
			return event;
		}

		if (event.getUserId() == userId) {
			event.setTitle(title);
			event.setDescription(description);
			event.setLastUpdatedDatetime(LocalDateTime.now());
			event.setNoOfTimesUpdated(event.getNoOfTimesUpdated() + 1);
			event.setIsNotifActive(isNotifActive);
			event.setTimeBeforeToNotify(timeBeforeToNotify);
			event.setNotifMessage(notifMessage);

			User user = session.get(User.class, userId);
			if (event.getDate().compareTo(LocalDateTime.now()) > 0) {
				event.setStatus("upcoming");
			} else {
				event.setStatus("passed");
			}
			Creater creater = new Creater(user.getUsername(), user.getEmail());
			Notification notification = new Notification(event.getIsNotifActive(), event.getTimeBeforeToNotify(),
					event.getNotifMessage());
			event.setCreater(creater);
			event.setNotification(notification);
			String selfUri = uri.getAbsolutePath().toString();
			event.setLink(new Link(selfUri, "self", "GET"));
			
			return event;
		}
		// When no such event with provided event Id is present for this user
		else {
			return null;
		}
	}

	public boolean deleteEvent(int eventId, int userId, Session session) {
		Event event = session.get(Event.class, eventId);
		// When no such event with provided event Id is present at all
		if (event == null) {
			return false;
		}

		if (event.getUserId() == userId) {
			session.delete(event);
			session.getTransaction().commit();
			return true;
		}
		// When no such event with provided event Id is present for this user
		else {
			return false;
		}
	}

	public Event getEventById(int eventId, int userId, UriInfo uri, Session session) {
		Event event = session.get(Event.class, eventId);
		// When no such event with provided event Id is present at all
		if (event == null) {
			return event;
		}

		if (event.getUserId() == userId) {
			User user = session.get(User.class, userId);
			if (event.getDate().compareTo(LocalDateTime.now()) > 0) {
				event.setStatus("upcoming");
			} else {
				event.setStatus("passed");
			}
			Creater creater = new Creater(user.getUsername(), user.getEmail());
			Notification notification = new Notification(event.getIsNotifActive(), event.getTimeBeforeToNotify(),
					event.getNotifMessage());
			event.setCreater(creater);
			event.setNotification(notification);
			String selfUri = uri.getAbsolutePath().toString();
			event.setLink(new Link(selfUri, "self", "GET"));
			return event;
		}
		// When no such event with provided event Id is present for this user
		else {
			return null;
		}
	}

}
