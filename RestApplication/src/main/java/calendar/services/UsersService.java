package calendar.services;

import java.util.Base64;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.hibernate.Session;

import calendar.DAO.UsersDAO;
import calendar.models.User;

public class UsersService {

	UsersDAO usersDao = new UsersDAO();

	public int getUserIdByAuthorizationHeader(HttpHeaders headers, Session session) {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);

		// decoding Authorization header value
		Base64.Decoder decoder = Base64.getDecoder();
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];

		int userId = usersDao.getUserIdByCredentials(username, password, session);
		return userId;
	}

	public void createNewUserUsingCredentialsFromAuthorizationHeader(HttpHeaders headers, Session session) {
		List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
		// decoding Authorization header value
		Base64.Decoder decoder = Base64.getDecoder();
		String userpass = new String(decoder.decode(authHeaders.get(0).split(" ")[1]));
		String username = userpass.split(":")[0];
		String password = userpass.split(":")[1];

		User newUser = new User(username, password);
		usersDao.createNewUser(newUser, session);
	}

}
