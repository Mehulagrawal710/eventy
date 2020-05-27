package calendar.services;

import java.text.ParseException;
import java.time.LocalDateTime;

import java.util.List;

import org.hibernate.Session;

import calendar.DAO.EventsDAO;
import calendar.models.Event;

public class EventsService {

	EventsDAO eventsDao = new EventsDAO();

	public List<Event> getAllEventsByUserId(int userId, Session session) {
		List<Event> eventList = eventsDao.getAllEventsByUserId(userId, session);
		return eventList;
	}

	public Event createNewEvent(int userId, String title, String description, String date, Session session)
			throws ParseException {
		LocalDateTime datetime = LocalDateTime.now();
		Event newEvent = new Event(userId, title, description, date, datetime);
		eventsDao.createNewEvent(newEvent, session);
		return newEvent;
	}

	public Event updateEvent(int eventId, String title, String description, int userId, Session session) {
		Event event = session.get(Event.class, eventId);
		// When no such event with provided event Id is present at all
		if (event == null) {
			return event;
		}

		if (event.getUserId() == userId) {
			event.setTitle(title);
			event.setDescription(description);
			session.getTransaction().commit();
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

	public Event getEventById(int eventId, int userId, Session session) {
		Event event = session.get(Event.class, eventId);
		// When no such event with provided event Id is present at all
		if (event == null) {
			return event;
		}

		if (event.getUserId() == userId) {
			return event;
		}
		// When no such event with provided event Id is present for this user
		else {
			return null;
		}
	}

}
