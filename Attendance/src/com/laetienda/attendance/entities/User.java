package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.utilities.Mailer;


@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
	@NamedQuery(name="User.findByUsername", query="SELECT u FROM User u WHERE u.username = :username"),
})
public class User extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USERS_ID_GENERATOR", sequenceName="USERS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USERS_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	private String description;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String password;

	private String username;

	//bi-directional many-to-one association to Log
	@OneToMany(mappedBy="user")
	private List<Log> logs;

	//bi-directional many-to-one association to UserRole
	@OneToMany(mappedBy="user", cascade=CascadeType.PERSIST, orphanRemoval=true)
	private List<UserRole> userRoles;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Event> events;
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Email> emails;
	
	@Column(name="\"email_server\"", nullable = true)
	private String emailServer;
	
	@Column(name="\"email_user\"", nullable = true)
	private String emailUser;
	
	@Column(name="\"email_password\"", nullable = true)
	private String emailPassword;
	
	@Column(name="\"email_port\"", nullable = true)
	private int emailPort; 

	public User(){
	}
	
	public User(Logger log) {
		super(log);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return this.created;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getModified() {
		return this.modified;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Log> getLogs() {
		return this.logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public Log addLog(Log log) {
		getLogs().add(log);
		log.setUser(this);

		return log;
	}

	public Log removeLog(Log log) {
		getLogs().remove(log);
		log.setUser(null);

		return log;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public UserRole addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		userRole.setUser(this);

		return userRole;
	}

	public UserRole removeUserRole(UserRole userRole) {
		getUserRoles().remove(userRole);
		userRole.setUser(null);

		return userRole;
	}
	
	public List<Event> getEvents(){
		return this.events;
	}
	
	public void setEvents(List<Event> events){
		this.events = events;
	}
	
	public Event addEvent(Event event){
		getEvents().add(event);
		event.setUser(this);
		
		return event;
	}
	
	public Event removeEvent(Event event){
		getEvents().remove(event);
		//event.setUser(null);
		
		return event;
	}
	
	public Event getEventByUrl(String url){
		Event result = null;
		
		for(Event temp : getEvents()){
			if(temp.getUrlEncodedName().equals(url)){
				result = temp;
				break;
			}
		}
		
		return result;
	}
	
	public List<Email> getEmails() {
		return this.emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public Email addEmail(Email email) {
		getEmails().add(email);
		email.setUser(this);

		return email;
	}
	
	public Email removeEmail(Email email) {
		
		if(getId() == email.getUser().getId()){
			
			for(Event evento : getEvents()){
				
				if(evento.getEmail().getId() == email.getId()){	
					addError("email", "This email can't be delted because it is being used by any event");
					break;
				}
			}
		}else{
			addError("email", "You don't have rights to delete this email.");
		}
		
		if(getErrors().get("email") == null){
			getEmails().remove(email);
		}

		return email;
	}
	
	public Email getEmailByUrl(String url){
		
		Email result = null;
		
		for(Email temp : getEmails()){
			if(temp.getUrl().equals(url)){
				result = getEmails().get(getEmails().indexOf(temp));
				break;
			}
		}
			
		return result;
	}
	
	public boolean setEmailConnection(String server, String username, String password, String port){
		boolean result = false;
		
		Mailer test = new Mailer(log);
		
		setEmailServer(server);
		setEmailUser(username);
		setEmailPassword(password);
		setEmailPort(port);
		
		test.setSettings(server, getEmailPort(), username, password);
		
		if(!test.testConnection()){
			addError("email server", "Email settings are wrong");
		}
		
		return result;
	}
	
	private void setEmailServer(String server){
		this.emailServer = server;
	}
	
	public String getEmailServer(){
		return this.emailServer;
	}
	
	private void setEmailUser(String username){
		this.emailUser = username;
	}
	
	public String getEmailUser(){
		return this.emailUser;
	}
	
	private void setEmailPassword(String password){
		this.emailPassword = encrypt(password);
	}
	
	public String getEmailPassword(){
		return decrypt(emailPassword);
	}
	
	private void setEmailPort(String port){
		try{
			int temp = Integer.parseInt(port);
			setEmailPort(temp);
		}catch (Exception ex){
			setEmailPort(0);
		}
	}
	
	private void setEmailPort(int port){
		this.emailPort = port;
	}
	
	public int getEmailPort(){
		return this.emailPort;
	}
}