package com.laetienda.attendance.entities;

import java.io.Serializable;
import java.net.URLEncoder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import com.laetienda.attendance.utilities.Logger;

@Entity
@Table(name="people")
@NamedQueries({
	@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p ORDER BY p.name"),
	@NamedQuery(name="Person.findAllByUser", query="SELECT p FROM Person p JOIN p.eventsPeoples ep JOIN ep.event e WHERE e.user = :user ORDER BY p.name"),
	@NamedQuery(name="Person.findByEmail", query="SELECT p FROM Person p WHERE p.email = :email"),
})
public class Person extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PEOPLE_ID_GENERATOR", sequenceName="PEOPLE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PEOPLE_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="\"email\"", unique=true)
	private String email;

	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String name;

	//bi-directional many-to-one association to EventsPeople
	@OneToMany(mappedBy="person", cascade=CascadeType.PERSIST)
	private List<EventsPeople> eventsPeoples;
	
	public Person() {
		super();
	}
	
	public Person(Logger log){
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		
		if(email == null || email.isEmpty()){
			addError("email", "E-Mail is empty. Make sure to enter a valid email address");
		}else{
			
			Pattern p = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			Matcher m = p.matcher(email);
			
			if(email.length() > 254){
				addError("email", "e-Mail address is too long. Make sure to enter a valid email address");
			}else if(!m.matches()){
				addError("email", "Make sure to enter a valid e-Mail address");
			}else{
				log.debug("The email complains the requirementes. $email: " + email);
			}
		}
		
		this.email = email;
	}

	public Date getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		
		if(getName() == null || getName().isEmpty()){
			
			isValidName(name);
			
		}else{
			if(!getName().equals(name)){
				addError("name", "A different name has been set for this email before.");
			}
		}
		this.name = name;
	}
	
	public void editName(String name){
		isValidName(name);
		
		this.name = name;
	}
	
	private boolean isValidName(String name){
		boolean result = false;
		
		if(name == null || name.isEmpty()){
			addError("name", "Name can't be empty");
		
		}else{
			
			Pattern p = Pattern.compile("^[\\p{L} .'-]+");
			Matcher m = p.matcher(name);
			
			if(name.length() > 25){
				addError("name", "The name may have 25 letters maximun");
			
			}else if(!m.matches()){
				addError("name", "The name can't have special characters only letters.");
			}
			
			else{
				result = true;
			}
		}
		
		return result;
	}

	public List<EventsPeople> getEventsPeople() {
		if(this.eventsPeoples == null){
			this.eventsPeoples = new ArrayList<EventsPeople>();
		}
		
		return this.eventsPeoples;
	}

	public void setEventsPeoples(List<EventsPeople> eventsPeoples) {
		this.eventsPeoples = eventsPeoples;
	}

	public EventsPeople addEventsPeople(EventsPeople eventsPeople) {
		getEventsPeople().add(eventsPeople);
		eventsPeople.setPerson(this);

		return eventsPeople;
	}

	public EventsPeople removeEventsPeople(EventsPeople eventsPeople) {
		getEventsPeople().remove(eventsPeople);
		eventsPeople.setPerson(null);

		return eventsPeople;
	}
	
	public void addEvent(Event event){
		
		boolean flag = true;
		
		for(EventsPeople temp : this.getEventsPeople()){
			if(temp.getEvent().getId() == event.getId()){
				this.addError("event", "This guest has already been invited to the event");
				flag = false;
				break;
			}
		}
		
		if(flag){
			EventsPeople newEvent = new EventsPeople(log);
			newEvent.setEvent(event);
			newEvent.setPerson(this);
			newEvent.setStatus("invited");
			this.addEventsPeople(newEvent);
		}
	}
	
	public String getUrlEncodedEmail(){
		String result = "";
		
		try{
			result = URLEncoder.encode(this.email, "UTF-8");
		}catch (Exception ex){
			log.critical("Unsupported encoder");
			log.exception(ex);
		}
		
		return result;
	}

}