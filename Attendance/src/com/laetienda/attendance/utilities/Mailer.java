package com.laetienda.attendance.utilities;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.laetienda.attendance.entities.User;

public class Mailer {
	
	private Logger log;
	private String server;
	private String username;
	private String password;
	private int port;
	private String subject;
	private String content;
	private String to;
	private Properties mailServerProperties;
	private Session getMailSession;
	private MimeMessage generateMailMessage;
	
	
	public Mailer(Logger log){
		this.log = log;
		init();
	}
	
	public Mailer(User user, Logger log){
		this.log = log;
		init();
		setSettings(user.getEmailServer(), user.getEmailPort(), user.getEmailUser(), user.getEmailPassword());
	}
	
	private void init(){
		
		subject = "New email";
		content = "Content for new email";
	}
	
	public void setSettings(String server, int port, String username, String password){
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	public void setSubject(String subject){
		this.subject = subject;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public void setTo(String to){
		this.to= to;
	}
	
	public String getTo(){
		return this.to;
	}
	
	public boolean send(){
		boolean result = false;
		
		try{
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", Integer.toString(port));
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			
			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);
			
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(getTo()));
			//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("test2@crunchify.com"));
			generateMailMessage.setSubject(getSubject(), "utf-8");
			 
			String emailBody = getContent();
			generateMailMessage.setContent(emailBody, "text/html; charset=utf-8");
			
			Transport transport = getMailSession.getTransport("smtp");
			transport.connect(server, username, password);
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			
			result = true;
		}catch(Exception ex){
			log.notice("Exception caught while test connection to email server");
			log.exception(ex);
		}
		
		return result;
	}
	
	public boolean testConnection(){
		
		setSubject("Test email connection");
		setContent("Test email connection by JavaMail API example. " + "<br><br> Regards");
		setTo("myself@cafeterosclub.ca");
		
		return send();
	}

}
