package calendar.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import calendar.models.Token;
import calendar.models.User;
import calendar.services.AuthService;
import calendar.services.UsersService;

@Path("/")
public class UserAuthenticationResource {

	UsersService usersService = new UsersService();
	AuthService authService = new AuthService();

	ObjectMapper mapper = new ObjectMapper();

	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
			.addAnnotatedClass(Token.class).buildSessionFactory();

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("username") String username, @QueryParam("password") String password)
			throws JsonProcessingException {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		int userId = usersService.getUserIdByCredentials(username, password, session);
		// When no match found for username and password
		if (userId == -1) {
			return Response.status(Status.UNAUTHORIZED).entity("Invalid Credentials").build();
		}
		// User found
		Token newToken = authService.createAndReturnNewToken(userId, session);
		return Response.status(Status.OK).entity(mapper.writeValueAsString(newToken)).build();
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public Response signup(@QueryParam("username") String username, @QueryParam("password") String password) {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		boolean success = usersService.createNewUserUsingCredentials(username, password, session);
		if (success) {
			return Response.status(Status.CREATED).entity("Signup successfull").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("This username is taken").build();
		}
	}

}