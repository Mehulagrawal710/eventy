package calendar.resources;

import com.sendgrid.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Test {
	public static void main(String[] args) throws IOException {
		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/batch");
			//request.setBody("");

			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}
}

//public class Test {
//	public static void main(String[] args) throws IOException {
//		LocalDateTime sendTime = LocalDateTime.now().plusSeconds(60);
//		ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
//		long epoch = sendTime.atZone(zoneId).toEpochSecond();
//		System.out.println(epoch);
//		
//		Mail mail = new Mail();
//
//		Email fromEmail = new Email();
//		fromEmail.setName("Chanda Garg");
//		fromEmail.setEmail("chandagarg3966@gmail.com");
//		mail.setFrom(fromEmail);
//
//		mail.setSubject("Hello Mehul");
//		
//		Personalization personalization = new Personalization();
//	    Email to = new Email();
//	    to.setName("Mehul Agrawal");
//	    to.setEmail("mehulagrawal710@gmail.com");
//	    personalization.addTo(to);
//	    personalization.setSendAt(epoch);
//	    mail.addPersonalization(personalization);
//		
//	    Content content = new Content();
//	    content.setType("text/plain");
//	    content.setValue("some text here");
//	    mail.addContent(content);
//	    content.setType("text/html");
//	    content.setValue("<html><body>some text here</body></html>");
//	    mail.addContent(content);
//		
//
//		SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
//		Request request = new Request();
//		try {
//			request.setMethod(Method.POST);
//			request.setEndpoint("mail/send");
//			request.setBody(mail.build());
//
//			Response response = sg.api(request);
//			System.out.println(response.getStatusCode());
//			System.out.println(response.getBody());
//			System.out.println(response.getHeaders());
//		} catch (IOException ex) {
//			throw ex;
//		}
//	}
//}