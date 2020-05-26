package calendar.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import calendar.models.User;
import calendar.services.UsersService;

@Path("/")
public class UserAuthenticationResource {

	UsersService usersService = new UsersService();

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
			.buildSessionFactory();

	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	public Response login(@Context HttpHeaders headers) {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByAuthorizationHeader(headers, session);

		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}
		// User found
		return Response.status(Status.OK).entity("Authentication successfull").build();
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public Response signup(@Context HttpHeaders headers) {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		usersService.createNewUserUsingCredentialsFromAuthorizationHeader(headers, session);
		return Response.status(Status.CREATED).entity("Signup successfull").build();
	}
}
