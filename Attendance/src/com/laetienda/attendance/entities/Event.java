package com.laetienda.attendance.entities;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;

import javax.persistence.*;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.laetienda.attendance.utilities.Logger;

@Entity
@Table(name="events")
@NamedQueries({
	@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e ORDER BY e.date DESC"),
	@NamedQuery(name="Event.FindAllByUser", query="SELECT e FROM Event e WHERE e.user = :user ORDER BY e.date DESC"),
	@NamedQuery(name="Event.FindByUrl", query="SELECT e FROM Event e WHERE e.urlEncodedName = :urlEncodedName")
})
public class Event extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="events_id_seq", sequenceName="events_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="events_id_seq")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"confirmation_limit_date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar confirmationLimitDate;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String name;

	//bi-directional one-to-one association to TextReference
	@OneToOne(cascade = CascadeType.PERSIST, orphanRemoval=true)
	@JoinColumn(name="description")
	private TextReference description;
	
	//bi-directional many-to-one association to EventsPeople
	@OneToMany(mappedBy="event")
	private List<EventsPeople> guests;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="\"userID\"")
	private User user;
	
	//bi-directional many-to-one association to Email
	@ManyToOne
	private Email email;
	
	@Column(name="\"urlEncodedName\"")
	private String urlEncodedName;
	
	@Column(name="\"confirmation_limit_unit\"")
	private String confirmationLimitUnit;
	
	@Column(name="\"confirmation_limit_quantity\"")
	private int confirmationLimitQuantity;
	
	@Transient
	private Calendar ahora;

	public Event() {
		super();
		ahora = new GregorianCalendar();
	}
	
	public Event(Logger log){
		super(log);
		ahora = new GregorianCalendar();
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getConfirmationLimitDate() {
		return this.confirmationLimitDate;
	}

	public void setConfirmationLimitDate(Calendar confirmationLimitDate) {
		this.confirmationLimitDate = confirmationLimitDate;
	}
	
	public void setConfirmationLimitDate(String fecha, String hora, String quantity, String unit) {
		
		String[] unidad = {"Years", "Months", "Weeks", "Days", "Hours", "mins", "none"};
		int[] unidadInt = {Calendar.YEAR, Calendar.MONTH, Calendar.WEEK_OF_YEAR, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE};
		
		if(quantity != null && !quantity.isEmpty()){
			confirmationLimitQuantity = Integer.parseInt(quantity);
		}else{
			confirmationLimitQuantity = 0;
		}
		
		if(unit != null && !unit.isEmpty()){
			confirmationLimitUnit = unit;
		}else{
			confirmationLimitUnit = null;
		}
		
		if(fecha != null && hora != null && quantity != null && unit != null && !fecha.isEmpty() && !hora.isEmpty() && !quantity.isEmpty() && !unit.isEmpty()){
			
			confirmationLimitDate = getCalendarFormat(fecha, hora);
						
			int c;
			for(c=0; c < unidad.length; c++){
				if(unidad[c].equals(unit)){
					confirmationLimitDate.add(unidadInt[c], (-1 * Integer.parseInt(quantity)));
					break;
				}
			}
						
			if(confirmationLimitDate.before(ahora)){
				addError("confirmationLimitDate", "The confirmation limit can't be in the past");
				
			}else if(confirmationLimitDate.after(date)){
				confirmationLimitDate = (Calendar)date.clone();
			}
			
			log.debug("confirmation date is: " + this.calendarToString(confirmationLimitDate));
			
		}else{
			addError("confirmationLimitDate", "Select when is the deadline to confirm the assistance");
		}
	}
	
	public void setDate(String fecha, String hora){
		
		if(fecha != null && hora != null && !fecha.isEmpty() && !hora.isEmpty()){
			this.date = getCalendarFormat(fecha, hora);
			log.debug("fecha: " + this.calendarToString(date));
			log.debug("ahora:" + this.calendarToString(ahora));
			
			if(date.before(ahora)){
				addError("date", "The date of the event can't be in the past");
			}
			
		}else{
			this.addError("date", "A date for the event must be selected");
			this.date = new GregorianCalendar();
		}
	}

	public Date getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}
	
	public Date getCreated() {
		return this.created;
	}

	public String getDate() {
		return super.dateToString(this.date); 
	}
	
	public String getTime(){
		return super.timeToString(this.date);
	}

	public void setDate(Calendar date) {
		this.date = date;
	}
	
	public int getQuantity(){
		return this.confirmationLimitQuantity;
	}
	
	public String getUnit(){
		return this.confirmationLimitUnit;
	}

	public void setName(String name) {
		
		String temp;
		
		if(name == null || name.isEmpty()){
			
			addError("name", "The name of the event can't be empty");
			
		}else{
			try{
				temp = URLEncoder.encode(name,"UTF-8");
				
				if(temp.length() > 254 || name.length() > 64){
					addError("name", "The name can't be longer than 64 characters");
				}
				
				for(Event userEvent : user.getEvents()){
					if(name.equals(userEvent.getName()) && !(this.id == userEvent.getId())){
						addError("name", "There is an existing event with the suggested name");
						break;
					}
				}
				
				this.urlEncodedName = temp;
				
			}catch(Exception ex){
				log.critical("Unsupported encoder");
				log.exception(ex);
			}
		}
		
		this.name = name;
	}
	
	public String getUrlEncodedName(){
		return this.urlEncodedName;
	}

	public String getDescription() {
		return this.description.getText();
	}

	public void setDescription(String description) {
		
		if(this.description == null){
			this.description = new TextReference();
		}
		
		this.description.setText(description);
	}
	
	public User getUser(){
		return this.user;
	}
	
	public void setUser(User user){
		this.user = user;
		if(user != null && !user.getEvents().contains(this)){
			user.getEvents().add(this);
		}
	}
	
	public List<EventsPeople> getGuests() {
		return this.guests;
	}

	public void setGuests(List<EventsPeople> guests) {
		if(this.guests == null){
			this.guests = new ArrayList<EventsPeople>();
		}
		this.guests = guests;
	}

	public EventsPeople addGuest(EventsPeople guest) {
		getGuests().add(guest);
		guest.setEvent(this);

		return guest;
	}
	
	public void addGuest(Person person){
		EventsPeople newGuest = new EventsPeople(log);
		newGuest.setPerson(person);
		newGuest.setEvent(this);
		
		getGuests().add(newGuest);
	}

	public EventsPeople removeGuest(EventsPeople guest) {
		getGuests().remove(guest);
		guest.setEvent(null);

		return guest;
	}
	
	public String encrypt(String original){
		String result = null;
		
		byte[] encrypted = Base64.getEncoder().encode(original.getBytes());
		result = new String(encrypted);
		
		return result;
	}
	
	public Calendar getDateObject(){
		return this.date;
	}
	
	public Email getEmail() {
		return this.email;
	}
	
	public void setEmail(Email email) {
		this.email = email;
	}
}