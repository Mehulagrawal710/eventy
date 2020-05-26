package calendar.DAO;

import java.util.List;

import org.hibernate.Session;

import calendar.models.Event;

public class EventsDAO {
	
	public List<Event> getAllEventsByUserId(int userId, Session session) {
		String query = "FROM events WHERE user_id = "+userId;
		List<Event> eventList = session.createQuery(query).getResultList();
		return eventList;
	}

	public void createNewEvent(Event newEvent, Session session) {
		session.save(newEvent);
		session.getTransaction().commit();
	}

}
