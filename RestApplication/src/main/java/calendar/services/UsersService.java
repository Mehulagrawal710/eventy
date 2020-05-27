package calendar.services;

import org.hibernate.Session;

import calendar.DAO.UsersDAO;
import calendar.models.User;

public class UsersService {

	UsersDAO usersDao = new UsersDAO();

	public boolean createNewUserUsingCredentials(String username, String password, Session session) {
		User newUser = new User(username, password);
		boolean created = usersDao.createNewUser(newUser, session);
		if (!created) {
			return false;
		} else {
			return true;
		}
	}

	public int getUserIdByCredentials(String username, String password, Session session) {
		int userId = usersDao.getUserIdByCredentials(username, password, session);
		return userId;
	}

}
