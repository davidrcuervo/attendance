package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.utilities.Mailer;


@Entity
@Table(name="events_people")
@NamedQueries({
	@NamedQuery(name="EventsPeople.findAll", query="SELECT e FROM EventsPeople e"),
	@NamedQuery(name="EventsPeople.findByUserAndEvent", query="SELECT ep FROM EventsPeople ep WHERE ep.person = :person AND ep.event = :event"),
})

public class EventsPeople extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EVENTS_PEOPLE_ID_GENERATOR", sequenceName="EVENTS_PEOPLE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EVENTS_PEOPLE_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP) 
	private Date created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String status;

	//bi-directional many-to-one association to EventTest
	@ManyToOne
	@JoinColumn(name="\"events_id\"")
	private Event event;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="\"people_id\"")
	private Person person;
	
	@Transient
	private String appUrl;

	public EventsPeople() {
		super();
	}
	
	public EventsPeople(Logger log){
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
				
		if(!(status.equals("yes") || status.equals("no") || status.equals("invited") || status.equals("invite_sent"))){
			addError("confirmation", "Sorry, the provided answer doesn't make sense");
		}
		
		if(getEvent().getConfirmationLimitDate().before(getAhora())){
			addError("confirmation", "Sorry, The confirmation limit date has expired.");
		}
		
		if(getErrors().get("confirmation") == null){
			if(status.equals("invite_sent")){
				if(sendInvitation()){
					this.status = status;
				}else{
					addError("send email", "Email has not been sent");
				}
			}else{
				this.status = status;
			}
		}
	}
	
	private boolean sendInvitation(){
				
		Mailer invitation = new Mailer(getEvent().getUser(), log);
		invitation.setContent(getEvent().getEmail().getFormatedEmail(this));
		invitation.setTo(getPerson().getEmail());
		
		return invitation.send();
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event event) {
		this.event = event;
		if(!event.getGuests().contains(this)){
			event.getGuests().add(this);
		}
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public void setAppUrl(String appUrl){
		this.appUrl = appUrl;
	}
	
	public String getAppUrl(){
		return this.appUrl;
	}
	
	
	public boolean getIsAttending(){
		boolean result = false;
		
		if(this.getStatus().equals("yes")){
			result = true;
		}
		
		return result;
	}
}