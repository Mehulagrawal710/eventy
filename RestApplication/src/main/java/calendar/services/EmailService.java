package calendar.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.ws.rs.core.UriInfo;

import org.hibernate.Session;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import calendar.models.Event;
import calendar.models.User;

public class EmailService {

	String senderName = "Calendar";
	String senderEmail = "Calendar@gmail.com";

	public void scheduleNewNotification(Event newEvent, Session session) throws IOException {
		User user = session.get(User.class, newEvent.getUserId());
		String receiverName = user.getUsername();
		String receiverEmail = user.getEmail();

		String emailSubject = "Notification: " + newEvent.getTitle();

		LocalDateTime sendTime = newEvent.getDate().minusSeconds(newEvent.getTimeBeforeToNotify());
		ZoneId zoneId = ZoneId.systemDefault();
		long epoch = sendTime.atZone(zoneId).toEpochSecond();
		System.out.println(epoch);

		String htmlContent = getNotificationTemplate(newEvent.getDate(), newEvent.getTitle(), newEvent.getDescription(),
				newEvent.getNotifMessage(), receiverName, receiverEmail, newEvent.getCreationDatetime(),
				newEvent.getEventId(), newEvent.getLastUpdatedDatetime(), newEvent.getNoOfTimesUpdated());

		/*---------------------------------------------------------------*/
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(senderName);
		fromEmail.setEmail(senderEmail);
		mail.setFrom(fromEmail);

		mail.setSubject(emailSubject);

		Personalization personalization = new Personalization();
		Email to = new Email();
		to.setName(receiverName);
		to.setEmail(receiverEmail);
		personalization.addTo(to);
		personalization.setSendAt(epoch);
		mail.addPersonalization(personalization);

		Content content = new Content();
		content.setType("text/html");
		content.setValue(htmlContent);
		mail.addContent(content);
		/*---------------------------------------------------------------*/
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	private String getNotificationTemplate(LocalDateTime date, String title, String description, String notifMessage,
			String receiverName, String receiverEmail, LocalDateTime creationDatetime, int eventId,
			LocalDateTime lastUpdatedDatetime, int noOfTimesUpdated) throws IOException {
		String fileName = "event-notification-template.html";
		ClassLoader classLoader = new EmailService().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		content = content.replace("@date", date.getDayOfMonth() + " " + date.getDayOfWeek() + " " + date.getYear())
				.replace("@time", date.getHour() + ":" + date.getMinute()).replace("@title", title)
				.replace("@description", description).replace("@message", notifMessage)
				.replace("@username", receiverName).replace("@email", receiverEmail)
				.replace("@createdat", creationDatetime.toString()).replace("@eventid", "" + eventId)
				.replace("@updatedcount", "" + noOfTimesUpdated);
		if (lastUpdatedDatetime == null) {
			content = content.replace("@lastupdated", "Not updated");
		} else {
			content = content.replace("@lastupdated", lastUpdatedDatetime.toString());
		}
		return content;
	}

	public void sendEmailVerification(String username, String email, String verificationKey, UriInfo uri)
			throws IOException {
		String receiverName = username;
		String receiverEmail = email;

		String emailSubject = "Verify your Email";

		String htmlContent = getEmailVerificationTemplate(username, verificationKey, uri);

		/*---------------------------------------------------------------*/
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(senderName);
		fromEmail.setEmail(senderEmail);
		mail.setFrom(fromEmail);

		mail.setSubject(emailSubject);

		Personalization personalization = new Personalization();
		Email to = new Email();
		to.setName(receiverName);
		to.setEmail(receiverEmail);
		personalization.addTo(to);
		mail.addPersonalization(personalization);

		Content content = new Content();
		content.setType("text/html");
		content.setValue(htmlContent);
		mail.addContent(content);
		/*---------------------------------------------------------------*/
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	private String getEmailVerificationTemplate(String username, String verificationKey, UriInfo uri)
			throws IOException {
		String link = uri.getAbsolutePath().toString().replace("signup", "activate/") + username + "/"
				+ verificationKey;
		System.out.println(link);

		String fileName = "email-verification-template.html";
		ClassLoader classLoader = new EmailService().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		content = content.replace("@link", link);
		return content;
	}

	public String getVerificationSuccessTemplate() throws IOException {
		String fileName = "verification-success-template.html";
		ClassLoader classLoader = new EmailService().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		return content;
	}

	public String getVerificationFailureTemplate() throws IOException {
		String fileName = "verification-failure-template.html";
		ClassLoader classLoader = new EmailService().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		return content;
	}

	public void sendWelcomeEmail(User user) throws IOException {
		String receiverName = user.getUsername();
		String receiverEmail = user.getEmail();

		String emailSubject = "Welcome Onboard!";

		String htmlContent = getWelcomeTemplate(receiverName);

		/*---------------------------------------------------------------*/
		Mail mail = new Mail();

		Email fromEmail = new Email();
		fromEmail.setName(senderName);
		fromEmail.setEmail(senderEmail);
		mail.setFrom(fromEmail);

		mail.setSubject(emailSubject);

		Personalization personalization = new Personalization();
		Email to = new Email();
		to.setName(receiverName);
		to.setEmail(receiverEmail);
		personalization.addTo(to);
		mail.addPersonalization(personalization);

		Content content = new Content();
		content.setType("text/html");
		content.setValue(htmlContent);
		mail.addContent(content);
		/*---------------------------------------------------------------*/
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	private String getWelcomeTemplate(String receiverName) throws IOException {
		String aboutApplication = "Calendar is a one stop solution for Users and Developers. Calendar Application comes with an interactive interface and notification system giving convenience to the users to easily manage their schedule. Calendar Application works by integrating with it's own REST API which is finely architectured and secured with a self developed token authentication system. The API endpoints are available for everyone's use, therefore creating a scheduling service alternative on top of which, developers can build there applications.";

		String fileName = "welcome-template.html";
		ClassLoader classLoader = new EmailService().getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		String content = new String(Files.readAllBytes(file.toPath()));
		content = content.replace("@username", receiverName).replace("@aboutapplication", aboutApplication)
				.replace("@senderemail", senderEmail);
		return content;
	}

}
