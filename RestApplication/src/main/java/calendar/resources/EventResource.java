package calendar.resources;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import calendar.models.Event;
import calendar.models.User;
import calendar.services.EventsService;
import calendar.services.UsersService;

@Path("/events")
public class EventResource {

	EventsService eventsService = new EventsService();
	UsersService usersService = new UsersService();

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Event.class)
			.addAnnotatedClass(User.class).buildSessionFactory();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEvents(@Context HttpHeaders headers) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}

		List<Event> eventList = eventsService.getAllEventsByUserId(userId, session);
		// return all events;
		return Response.status(Status.OK).entity(eventList).build();
	}

	@GET
	@Path("/{eventid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEventById(@Context HttpHeaders headers, @PathParam("eventid") int eventId) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}

		Event event = eventsService.getEventById(eventId, userId, session);
		// When no event is present with the given Event Id for this User
		if (event == null) {
			return Response.status(Status.NOT_FOUND)
					.entity("No event with the provided Event Id is found for this user").build();
		}
		// return event;
		else {
			return Response.status(Status.OK).entity(event).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response addEvent(@Context HttpHeaders headers, @QueryParam("title") String title,
			@QueryParam("description") String description, @QueryParam("date") String date) {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}

		// creating a new events with parameters
		Event newEvent = eventsService.createNewEvent(userId, title, description, date, session);
		// return newly created event
		return Response.status(Status.CREATED).entity(newEvent).build();
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateEvent(@Context HttpHeaders headers, @QueryParam("eventid") int eventId,
			@QueryParam("title") String title, @QueryParam("description") String description) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}

		// updating event with parameters
		Event updatedEvent = eventsService.updateEvent(eventId, title, description, userId, session);

		// When no event is present with the given Event Id for this User
		if (updatedEvent == null) {
			return Response.status(Status.NOT_FOUND)
					.entity("No event with the provided Event Id is found for this user").build();
		}
		// return updated event
		else {
			return Response.status(Status.OK).entity(updatedEvent).build();
		}
	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteEvent(@Context HttpHeaders headers, @QueryParam("eventid") int eventId) {

		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}

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
