package calendar;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import calendar.models.Event;
import calendar.models.User;

@Path("/")
public class Resource {

	@GET
	@Path("/demo")
	@Produces(MediaType.TEXT_PLAIN)
	public String demo() {

		return "hello";

	}

	@GET
	@Path("/events")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents(@Context HttpHeaders headers) {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

		Base64.Decoder decoder = Base64.getDecoder();
		System.out.println(authHeaders.get(0).split(" ")[1]);
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));

		System.out.println(userpass);
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);

		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class).addAnnotatedClass(User.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		List<Event> eventList = new ArrayList<Event>();
		String userQuery = "from users where password = " + password;
		System.out.println(userQuery);
		List<User> userList = session.createQuery(userQuery).getResultList(); 
		int userId = userList.get(0).getUserId();
		System.out.println(userId);
		
		
		
		String query = "FROM events WHERE user_id = "+userId;
		eventList = session.createQuery(query).getResultList();
		return eventList;

	}
	
	@POST
	@Path("/events")
	public void addEvent(@Context HttpHeaders headers,
			@QueryParam("title") String title,
			@QueryParam("description") String description,
			@QueryParam("date") String date) {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

		Base64.Decoder decoder = Base64.getDecoder();
		System.out.println(authHeaders.get(0).split(" ")[1]);
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));

		System.out.println(userpass);
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class).addAnnotatedClass(User.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		String userQuery = "from users where password = " + password;
		System.out.println(userQuery);
		List<User> userList = session.createQuery(userQuery).getResultList(); 
		int userId = userList.get(0).getUserId();
		System.out.println(userId);
		
		Event newEvent = new Event(userId, title, description, date, "2038-01-19 03:14:07");
		
		session.save(newEvent);
		session.getTransaction().commit();
		
	}
	
	@PUT
	@Path("/events")
	public void updateEvent(@Context HttpHeaders headers,
			@QueryParam("eventid") int eventId,
			@QueryParam("title") String title,
			@QueryParam("description") String description) {
		
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

		Base64.Decoder decoder = Base64.getDecoder();
		System.out.println(authHeaders.get(0).split(" ")[1]);
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));

		System.out.println(userpass);
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class).addAnnotatedClass(User.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		List<Event> eventList = new ArrayList<Event>();
		String userQuery = "from users where password = " + password;
		System.out.println(userQuery);
		List<User> userList = session.createQuery(userQuery).getResultList(); 
		int userId = userList.get(0).getUserId();
		System.out.println(userId);
		
		Event event = session.get(Event.class, eventId);
		System.out.println(event);
		
		if(event.getUserId() == userId) {
			event.setTitle(title);
			event.setDescription(description);
			session.getTransaction().commit();
		}else {
			System.out.println("This user does not have such an event");
		}
	}
	
	@DELETE
	@Path("/events")
	public void deleteEvent(@Context HttpHeaders headers,
			@QueryParam("eventid") int eventId) {
		
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

		Base64.Decoder decoder = Base64.getDecoder();
		System.out.println(authHeaders.get(0).split(" ")[1]);
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));

		System.out.println(userpass);
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		
		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class).addAnnotatedClass(User.class)
				.buildSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		List<Event> eventList = new ArrayList<Event>();
		String userQuery = "from users where password = " + password;
		System.out.println(userQuery);
		List<User> userList = session.createQuery(userQuery).getResultList(); 
		int userId = userList.get(0).getUserId();
		System.out.println(userId);
		
		Event event = session.get(Event.class, eventId);
		System.out.println(event);
		
		if(event.getUserId() == userId) {
			session.delete(event);
			session.getTransaction().commit();
		}else {
			System.out.println("This user does not have such an event");
		}
	}
	
}
