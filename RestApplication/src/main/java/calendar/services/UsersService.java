package calendar.services;

import java.util.UUID;

import org.hibernate.Session;

import calendar.DAO.UsersDAO;
import calendar.models.User;

public class UsersService {

	UsersDAO usersDao = new UsersDAO();

	public User createNewUserUsingCredentials(String username, String password, String email, Session session) {
		String verificationKey = UUID.randomUUID().toString().replace("-", "");
		User newUser = new User(username, password, email, verificationKey);
		boolean created = usersDao.createNewUser(newUser, session);
		if (!created) {
			return null;
		} else {
			return newUser;
		}
	}

	public int getUserIdByCredentials(String username, String password, Session session) {
		int userId = usersDao.getUserIdByCredentials(username, password, session);
		return userId;
	}

}
