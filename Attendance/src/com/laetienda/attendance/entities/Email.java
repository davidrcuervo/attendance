package com.laetienda.attendance.entities;

import java.io.Serializable;
import java.net.URLEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import com.laetienda.attendance.utilities.Logger;

@Entity
@Table(name="emails")
@NamedQueries({
	@NamedQuery(name="Email.findAll", query="SELECT e FROM Email e"),
	@NamedQuery(name="Email.findByName", query="SELECT e FROM Email e WHERE e.name = :emailName AND e.user = :user"),
})

public class Email extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EMAILS_ID_GENERATOR", sequenceName="EMAILS_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EMAILS_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String name;

	//bi-directional many-to-one association to UserTest
	@ManyToOne
	private User user;

	//bi-directional many-to-one association to EventTest
	@OneToMany(mappedBy="email")
	private List<Event> events;

	//uni-directional one-to-one association to TextReferenceTest
	@OneToOne(cascade = CascadeType.PERSIST, orphanRemoval=true)
	@JoinColumn(name="\"email\"")
	private TextReference email;

	public Email() {
		super();
	}
	
	public Email(Logger log){
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

	public Date getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		
		if(name == null || name.isEmpty()){
			addError("name", "The name of the email can't be empty");
			
		}else{
			if(name.length() > 25){
				addError("name", "The name of the email must have maximum 25 letters");
			}
			
			if(getUrlEncodedName() == null){
				addError("name", "The name of the email can not contain special letters");
			}else{
				if(getUrlEncodedName().length() > 254){
					addError("name", "The name is not compatible");
				}
			}
			
			for(Email userEmail : getUser().getEmails()){
				if(name.equals(userEmail.getName()) && !(this.id == userEmail.getId())){
					addError("name", "There is an existing email name with the suggested name");
					break;
				}
			}
		}
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		if(user != null && !user.getEmails().contains(this)){
			user.getEmails().add(this);
		}
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setEmail(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setEmail(null);

		return event;
	}
	
	public void setEmail(String email) {
		
		if(this.email  == null){
			this.email = new TextReference();
		}
		
		this.email.setText(email);
	}
	
	public String getEmail(){
		return this.email.getText();
	}
	
	public String getUrlEncodedName(){
		
		String result = null;
		
		try{		
			result = URLEncoder.encode(getName(), "UTF-8");
		}catch(Exception ex){
			log.critical("Exception caught while trying to encode url email name");
			log.exception(ex);
		}
		
		return result;
	}
	
	public String getUrl(){
		return getUrlEncodedName();
	}
}