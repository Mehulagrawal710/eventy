package calendar.resources;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import calendar.models.Event;
import calendar.models.Token;
import calendar.models.User;
import calendar.services.AuthService;
import calendar.services.EmailService;
import calendar.services.EventsService;
import calendar.services.UsersService;

@Path("/events")
public class EventResource {

	EventsService eventsService = new EventsService();
	UsersService usersService = new UsersService();
	AuthService authService = new AuthService();
	EmailService emailService = new EmailService();

	ObjectMapper mapper = new ObjectMapper();

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class)
			.addAnnotatedClass(User.class).addAnnotatedClass(Token.class).buildSessionFactory();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEvents(@QueryParam("token") String token, @Context UriInfo uri)
			throws JsonProcessingException {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			String jsonString = new JSONObject().put("message", "token missing").toString();
			return Response.status(Status.BAD_REQUEST).entity(jsonString).build();
		} else if (validity == -1) {
			String jsonString = new JSONObject().put("message", "invalid token").toString();
			return Response.status(Status.UNAUTHORIZED).entity(jsonString).build();
		} else if (validity == 0) {
			String jsonString = new JSONObject().put("message", "token expired").toString();
			return Response.status(Status.NOT_ACCEPTABLE).entity(jsonString).build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// return all events;
		List<Event> eventList = eventsService.getAllEventsByUserId(userId, uri, session);
		String jsonString = new JSONObject().put("message", "ok").put("resultCount", eventList.size())
				.put("events", "@events").toString();
		jsonString = jsonString.replace("\"@events\"", mapper.writeValueAsString(eventList));
		return Response.status(Status.OK).entity(jsonString).build();
	}

	@GET
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventById(@QueryParam("token") String token, @PathParam("eventid") int eventId,
			@Context UriInfo uri) throws JsonProcessingException {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			String jsonString = new JSONObject().put("message", "token missing").toString();
			return Response.status(Status.BAD_REQUEST).entity(jsonString).build();
		} else if (validity == -1) {
			String jsonString = new JSONObject().put("message", "invalid token").toString();
			return Response.status(Status.UNAUTHORIZED).entity(jsonString).build();
		} else if (validity == 0) {
			String jsonString = new JSONObject().put("message", "token expired").toString();
			return Response.status(Status.NOT_ACCEPTABLE).entity(jsonString).build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		Event event = eventsService.getEventById(eventId, userId, uri, session);
		// When no event is present with the given Event Id for this User
		if (event == null) {
			String jsonString = new JSONObject().put("message", "no such event found for this user").toString();
			return Response.status(Status.NOT_FOUND).entity(jsonString).build();
		}
		// return event;
		else {
			String jsonString = new JSONObject().put("message", "ok").put("event", "@events").toString();
			jsonString = jsonString.replace("\"@events\"", mapper.writeValueAsString(event));
			return Response.status(Status.OK).entity(jsonString).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEvent(@QueryParam("token") String token, @QueryParam("title") String title,
			@QueryParam("description") String description, @QueryParam("date") String date,
			@QueryParam("isNotifActive") boolean isNotifActive,
			@QueryParam("timeBeforeToNotify") int timeBeforeToNotify, @QueryParam("notifMessage") String notifMessage,
			@Context UriInfo uri) throws ParseException, IOException {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			String jsonString = new JSONObject().put("message", "token missing").toString();
			return Response.status(Status.BAD_REQUEST).entity(jsonString).build();
		} else if (validity == -1) {
			String jsonString = new JSONObject().put("message", "invalid token").toString();
			return Response.status(Status.UNAUTHORIZED).entity(jsonString).build();
		} else if (validity == 0) {
			String jsonString = new JSONObject().put("message", "token expired").toString();
			return Response.status(Status.NOT_ACCEPTABLE).entity(jsonString).build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// creating a new events with parameters
		Event newEvent = eventsService.createNewEvent(userId, title, description, date, isNotifActive,
				timeBeforeToNotify, notifMessage, uri, session);

		if (isNotifActive) {
			emailService.scheduleNewNotification(newEvent, session);
		}
		session.getTransaction().commit();
		// return newly created event
		String jsonString = new JSONObject().put("message", "ok").put("event", "@events").toString();
		jsonString = jsonString.replace("\"@events\"", mapper.writeValueAsString(newEvent));
		return Response.status(Status.CREATED).entity(jsonString).build();
	}

	@PUT
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEvent(@QueryParam("token") String token, @PathParam("eventid") int eventId,
			@QueryParam("title") String title, @QueryParam("description") String description,
			@QueryParam("isNotifActive") boolean isNotifActive,
			@QueryParam("timeBeforeToNotify") int timeBeforeToNotify, @QueryParam("notifMessage") String notifMessage,
			@Context UriInfo uri) throws IOException {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			String jsonString = new JSONObject().put("message", "token missing").toString();
			return Response.status(Status.BAD_REQUEST).entity(jsonString).build();
		} else if (validity == -1) {
			String jsonString = new JSONObject().put("message", "invalid token").toString();
			return Response.status(Status.UNAUTHORIZED).entity(jsonString).build();
		} else if (validity == 0) {
			String jsonString = new JSONObject().put("message", "token expired").toString();
			return Response.status(Status.NOT_ACCEPTABLE).entity(jsonString).build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// updating event with parameters
		Event updatedEvent = eventsService.updateEvent(eventId, title, description, isNotifActive, timeBeforeToNotify,
				notifMessage, userId, uri, session);

		// When no event is present with the given Event Id for this User
		if (updatedEvent == null) {
			String jsonString = new JSONObject().put("message", "no such event found for this user").toString();
			return Response.status(Status.NOT_FOUND).entity(jsonString).build();
		}
		// return updated event
		else {
			if (isNotifActive) {
				emailService.scheduleNewNotification(updatedEvent, session);
			}
			session.getTransaction().commit();
			String jsonString = new JSONObject().put("message", "ok").put("event", "@events").toString();
			jsonString = jsonString.replace("\"@events\"", mapper.writeValueAsString(updatedEvent));
			return Response.status(Status.OK).entity(jsonString).build();
		}
	}

	@DELETE
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteEvent(@QueryParam("token") String token, @PathParam("eventid") int eventId) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			String jsonString = new JSONObject().put("message", "token missing").toString();
			return Response.status(Status.BAD_REQUEST).entity(jsonString).build();
		} else if (validity == -1) {
			String jsonString = new JSONObject().put("message", "invalid token").toString();
			return Response.status(Status.UNAUTHORIZED).entity(jsonString).build();
		} else if (validity == 0) {
			String jsonString = new JSONObject().put("message", "token expired").toString();
			return Response.status(Status.NOT_ACCEPTABLE).entity(jsonString).build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// checking for validity and deleting event
		boolean success = eventsService.deleteEvent(eventId, userId, session);

		// When no event is present with the given Event Id for this User
		if (!success) {
			String jsonString = new JSONObject().put("message", "no such event found for this user").toString();
			return Response.status(Status.NOT_FOUND).entity(jsonString).build();
		}
		// returning 200 OK and success message
		else {
			String jsonString = new JSONObject().put("message", "ok").toString();
			return Response.status(Status.OK).entity(jsonString).build();
		}
	}

}
