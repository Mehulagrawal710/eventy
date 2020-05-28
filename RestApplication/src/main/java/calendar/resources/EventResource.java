package calendar.resources;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import calendar.models.Event;
import calendar.models.Link;
import calendar.models.Token;
import calendar.models.User;
import calendar.services.AuthService;
import calendar.services.EventsService;
import calendar.services.UsersService;

@Path("/events")
public class EventResource {

	EventsService eventsService = new EventsService();
	UsersService usersService = new UsersService();
	AuthService authService = new AuthService();

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
			return Response.status(Status.BAD_REQUEST).entity("Required Parameter 'token' is missing in the request")
					.build();
		} else if (validity == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build();
		} else if (validity == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Token has expired").build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// return all events;
		List<Event> eventList = eventsService.getAllEventsByUserId(userId, session);
		for (Event event : eventList) {
			String selfUri = uri.getAbsolutePath().toString() + "/" + event.getEventId();
			event.setLink(new Link(selfUri, "self", "GET"));
		}
		return Response.status(Status.OK).entity(mapper.writeValueAsString(eventList)).build();
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
			return Response.status(Status.BAD_REQUEST).entity("Required Parameter 'token' is missing in the request")
					.build();
		} else if (validity == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build();
		} else if (validity == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Token has expired").build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		Event event = eventsService.getEventById(eventId, userId, session);
		// When no event is present with the given Event Id for this User
		if (event == null) {
			return Response.status(Status.NOT_FOUND)
					.entity("No event with the provided Event Id is found for this user").build();
		}
		// return event;
		else {
			String selfUri = uri.getAbsolutePath().toString();
			event.setLink(new Link(selfUri, "self", "GET"));
			return Response.status(Status.OK).entity(mapper.writeValueAsString(event)).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEvent(@QueryParam("token") String token, @QueryParam("title") String title,
			@QueryParam("description") String description, @QueryParam("date") String date, @Context UriInfo uri)
			throws JsonProcessingException, ParseException {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			return Response.status(Status.BAD_REQUEST).entity("Required Parameter 'token' is missing in the request")
					.build();
		} else if (validity == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build();
		} else if (validity == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Token has expired").build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// creating a new events with parameters
		Event newEvent = eventsService.createNewEvent(userId, title, description, date, session);
		String selfUri = uri.getAbsolutePath().toString() + "/" + newEvent.getEventId();
		newEvent.setLink(new Link(selfUri, "self", "GET"));
		// return newly created event
		return Response.status(Status.CREATED).entity(mapper.writeValueAsString(newEvent)).build();
	}

	@PUT
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEvent(@QueryParam("token") String token, @PathParam("eventid") int eventId,
			@QueryParam("title") String title, @QueryParam("description") String description, @Context UriInfo uri)
			throws JsonProcessingException {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			return Response.status(Status.BAD_REQUEST).entity("Required Parameter 'token' is missing in the request")
					.build();
		} else if (validity == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build();
		} else if (validity == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Token has expired").build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// updating event with parameters
		Event updatedEvent = eventsService.updateEvent(eventId, title, description, userId, session);

		// When no event is present with the given Event Id for this User
		if (updatedEvent == null) {
			return Response.status(Status.NOT_FOUND)
					.entity("No event with the provided Event Id is found for this user").build();
		}
		// return updated event
		else {
			String selfUri = uri.getAbsolutePath().toString();
			updatedEvent.setLink(new Link(selfUri, "self", "GET"));
			return Response.status(Status.OK).entity(mapper.writeValueAsString(updatedEvent)).build();
		}
	}

	@DELETE
	@Path("/{eventid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteEvent(@QueryParam("token") String token, @PathParam("eventid") int eventId) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int validity = authService.checkTokenValidity(token, session);

		if (validity == -2) {
			return Response.status(Status.BAD_REQUEST).entity("Required Parameter 'token' is missing in the request")
					.build();
		} else if (validity == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Token").build();
		} else if (validity == 0) {
			return Response.status(Status.NOT_ACCEPTABLE).entity("Token has expired").build();
		}
		int userId = authService.getUserIdFromToken(token, session);

		// checking for validity and deleting event
		boolean success = eventsService.deleteEvent(eventId, userId, session);

		// When no event is present with the given Event Id for this User
		if (!success) {
			return Response.status(Status.NOT_FOUND)
					.entity("No event with the provided Event Id is found for this user").build();
		}
		// returning 200 OK and success message
		else {
			String successMsg = "Event with event-Id " + eventId + " deleted successfully";
			return Response.status(Status.OK).entity(successMsg).build();
		}
	}

}
