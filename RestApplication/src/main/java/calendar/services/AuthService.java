package calendar.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.hibernate.Session;

import calendar.models.Token;

public class AuthService {

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public Token createAndReturnNewToken(int userId, Session session) {
		String token = UUID.randomUUID().toString().replace("-", "");
		LocalDateTime issued = LocalDateTime.now();
		int timeToLive = 3600;
		LocalDateTime expires = issued.plusSeconds(timeToLive);
		Token newToken = new Token(token, userId, issued, timeToLive, expires);
		session.save(newToken);
		session.getTransaction().commit();
		return newToken;
	}

	public int checkTokenValidity(String token, Session session) {
		// When token is not received in query parameter
		if(token==null) {
			return -2;
		}
		
		Token tokenEntity = session.get(Token.class, token);

		// When token is not present in token pool
		if (tokenEntity == null) {
			return -1;
		}

		// When token has expired
		LocalDateTime currDatetime = LocalDateTime.now();
		LocalDateTime expiry = tokenEntity.getExpires();
		if(expiry.compareTo(currDatetime) <= 0) {
			return 0;
		}

		// When token is present and is not expired yet
		return 1;
	}

	public int getUserIdFromToken(String token, Session session) {
		Token tokenEntity = session.get(Token.class, token);
		return tokenEntity.getUserId();
	}

}
