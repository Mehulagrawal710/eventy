package calendar.resources;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

import calendar.models.Token;
import calendar.models.User;
import calendar.services.AuthService;
import calendar.services.EmailService;
import calendar.services.UsersService;

@Path("/")
public class UserAuthenticationResource {

	UsersService usersService = new UsersService();
	AuthService authService = new AuthService();
	EmailService emailService = new EmailService();

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
			return Response.status(Status.UNAUTHORIZED).entity("{\"message\": \"invalid credentials\"}").build();
		}
		// User found
		else {
			User user = session.get(User.class, userId);
			// when account is active
			if (user.isActive()) {
				Token newToken = authService.createAndReturnNewToken(userId, session);
				newToken.setMessage("ok");
				return Response.status(Status.OK).entity(mapper.writeValueAsString(newToken)).build();
			}
			// when account is NOT active
			else {
				return Response.status(Status.UNAUTHORIZED).entity("{\"message\": \"account not active\"}").build();
			}
		}
	}

	@POST
	@Path("/signup")
	@Produces(MediaType.TEXT_PLAIN)
	public Response signup(@QueryParam("username") String username, @QueryParam("password") String password,
			@QueryParam("email") String email, @Context UriInfo uri) throws IOException {
		if (username == null) {
			return Response.status(Status.BAD_REQUEST).entity("Required parameter 'username' is missing").build();
		}
		if (password == null) {
			return Response.status(Status.BAD_REQUEST).entity("Required parameter 'password' is missing").build();
		}
		if (email == null) {
			return Response.status(Status.BAD_REQUEST).entity("Required parameter 'email' is missing").build();
		}
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		User newUser = usersService.createNewUserUsingCredentials(username, password, email, session);
		if (newUser != null) {
			emailService.sendEmailVerification(username, email, newUser.getVerificationKey(), uri);
			return Response.status(Status.CREATED).entity("Signup successfull").build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE).entity("This username is taken").build();
		}
	}

	@GET
	@Path("/activate/{username}/{key}")
	@Produces(MediaType.TEXT_HTML)
	public Response activate(@PathParam("username") String username, @PathParam("key") String key) throws IOException {
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		String userQuery = "from users where username = '" + username + "' and verificationKey = '" + key + "'";
		List<User> userList = session.createQuery(userQuery).getResultList();
		if (userList.size() == 1) {
			if (userList.get(0).isActive()) {
				String alreadyActive = "<html><body><h1>Already Verified!</h1><body></html>";
				return Response.status(Status.OK).entity(alreadyActive).build();
			}
			userList.get(0).setActive(true);
			session.getTransaction().commit();
			String successHtml = emailService.getVerificationSuccessTemplate();
			emailService.sendWelcomeEmail(userList.get(0));
			return Response.status(Status.ACCEPTED).entity(successHtml).build();
		} else {
			String failHtml = emailService.getVerificationFailureTemplate();
			return Response.status(Status.NOT_ACCEPTABLE).entity(failHtml).build();
		}
	}

}