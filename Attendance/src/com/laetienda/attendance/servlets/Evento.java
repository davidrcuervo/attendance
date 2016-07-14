package com.laetienda.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.persistence.EntityManager;

import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.entities.User;
import com.laetienda.attendance.entities.Event;
import com.laetienda.attendance.entities.Person;

public class Evento extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String[] pathParts;
	private Logger log;
	private DB db;
	private Event evento;
       
    public Evento() {
        super();
        
    }
    
    public void init(ServletConfig config) throws ServletException {
		log = (Logger)config.getServletContext().getAttribute("Logger");
		db = (DB)config.getServletContext().getAttribute("db");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("processiong get from evento servlet");
		
		pathParts = (String[])request.getAttribute("pathParts");
		
		// /event/add
		if(pathParts.length == 3 && pathParts[2].equals("add")){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "event/add.jsp").forward(request, response);
		
		// /event/:urlEventName	
		}else if(pathParts.length == 3 && findEventByUrl(pathParts[2], request, response)){
			request.setAttribute("event", this.evento);
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "event/show.jsp").forward(request, response);
		
		// /event/edit/:urlEventName
		}else if(pathParts.length == 4 && pathParts[2].equals("edit") && findEventByUrl(pathParts[3], request, response)){
			
			if(request.getParameter("submit") == null){
				request.setAttribute("event", this.evento);
			}
					
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "event/edit.jsp").forward(request, response);
		
		// /event/delete/:urlEventName
		}else if(pathParts.length == 4 && pathParts[2].equals("delete") && findEventByUrl(pathParts[3], request, response)){
			delete(request, response);
			
		// /event/addGuest/:urlEventName
		}else if(pathParts.length == 4 && pathParts[2].equals("addGuest") && findEventByUrl(pathParts[3], request, response)){
			
			if(request.getParameter("submit") == null){
				request.setAttribute("event", this.evento);
			}
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "guest/add.jsp").forward(request, response);
		
		// /event/:urlEventName/guest/:urlEncodedEmail	
		}else if(pathParts.length == 5 && pathParts[3].equals("guest") && findEventByUrl(pathParts[2], request, response) && db.findGuestByEncodedEmail(pathParts[4]) != null){
			request.setAttribute("event", this.evento);
			request.setAttribute("person", db.findGuestByEncodedEmail(pathParts[4]));
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "event/show.jsp").forward(request, response);

		}
		else{
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing form from evento servlet");
		
		// add or edit event
		if(request.getParameter("submit").equals("add") || request.getParameter("submit").equals("edit")){
			addOrEdit(request, response);
		
		// addGuest or editGues
		} else if(request.getParameter("submit").equals("addGuest")){
			addGuest(request, response);
		}
		else{
			doGet(request, response);
		}
	}
	
	private boolean findEventByUrl(String urlEncodedName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.evento = db.findEventByUrl(urlEncodedName);
		
		boolean result = false;
		
		if(this.evento != null){
			result = true;
		}
		
		return result;
	}
	/*
	private boolean findGuestByEncodedEmail(String urlEncodedEmail, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Finding guest by url. $urlEncodedEmail: " + urlEncodedEmail);
		
		boolean result = false;
		Person person;
		
		try{
			EntityManager em = db.getEntityManager();
			String email = URLDecoder.decode(urlEncodedEmail, "UTF-8");
			person = em.createNamedQuery("Person.findByEmail", Person.class).setParameter("email", email).getSingleResult();
			request.setAttribute("person", person);
			result = true;
			em.clear();
			em.close();
			
		}catch (Exception ex){
			log.notice("event not find. $urlEncodedName: " + urlEncodedEmail);
			log.exception(ex);
		}
		
		return result;
	}
	*/
	private void addOrEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Adding or editing event");
		
		String submit = request.getParameter("submit");
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		String event_date = request.getParameter("event_date");
		String event_time = request.getParameter("event_time");
		String unit_before = request.getParameter("unit_before");
		String quantity_before = request.getParameter("quantity_before");
		
		int eventID = Integer.parseInt(request.getParameter("eventID"));
		User temp = (User)request.getAttribute("user");
		
		try{
			EntityManager em = db.getEntityManager();
			em.getTransaction().begin();
			
			User user = em.find(User.class, temp.getId());
			
			if(submit.equals("add")){
				evento = new Event(log);
				evento.setUser(user);
			}else{
				evento = em.find(Event.class, eventID);
			}
			
			evento.setName(name);
			evento.setDescription(description);
			evento.setDate(event_date, event_time);
			evento.setConfirmationLimitDate(event_date, event_time, quantity_before, unit_before);
			
			if(evento.getErrors().size() <= 0){
				try{
					if(submit.equals("add")){
						em.persist(evento);
					}
					
					em.flush();
					em.getTransaction().commit();
					
				}catch (Exception ex1){
					evento.addError("event", "Internal error while processiong the event");
					log.notice("Exception thrown while saving event in the database");
					log.exception(ex1);
					
					try{
						em.getTransaction().rollback();
					}catch (Exception ex2){
						log.error("Error while rolling back");
						log.exception(ex2);
					}
				}finally{
					em.clear();
					em.close();
				}
			}
			
			if(evento.getErrors().size() > 0){
				request.setAttribute("event", evento);
				doGet(request, response);
			}else{
				response.sendRedirect(request.getContextPath() + "/event/" + evento.getUrlEncodedName());
			}
			
		}catch(Exception ex){
			log.error("Event failed when adding or editing");
			log.exception(ex);
			ex.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("deleting event");
		
		
		if(evento.getGuests().size() > 0){
			evento.addError("delete", "You can't delete event when it has any guest");
			
		}else{
			User temp = (User)request.getAttribute("user");
			EntityManager em = db.getEntityManager();
			em.getTransaction().begin();
			
			try{
				User user = em.find(User.class, temp.getId());
				evento = em.find(Event.class, evento.getId());
				
				if(user.getEvents().contains(evento)){
					log.debug("event found in user events");
					user.removeEvent(evento);
				}else{
					em.remove(evento);
				}
				
				em.getTransaction().commit();
				
				log.debug("object removed");
			}catch (Exception ex){
				evento.addError("delete", "An internal error has ocurred while deleting the event");
				log.notice("Exception while trying to delete event");
				log.exception(ex);
				ex.printStackTrace();
				try{
					em.getTransaction().rollback();
				}catch (Exception ex2){
					log.error("error while rolling back delete of event");
					log.exception(ex2);
				}
				
			}finally{
				em.clear();
				em.close();
			}
		}
		
		if(evento.getGuests().size() > 0){
			request.setAttribute("event", evento);
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "event/show.jsp").forward(request, response);
		}else{
			response.sendRedirect(request.getContextPath());
		}
	}

	private void addGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Adding guest");
		
		Person person;
		boolean newPersonFlag = false;
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		//String submit = request.getParameter("submit");
		//log.debug("event_id: " + request.getParameter("event_id"));
		int eventID = Integer.parseInt(request.getParameter("event_id"));
		
		if(findEventByUrl(pathParts[3], request, response) && this.evento.getId() == eventID){
			
			try{
				EntityManager em = db.getEntityManager();
				em.getTransaction().begin();
				
				Event event = em.find(Event.class, eventID);
				event.setLogger(log);
				
				if(em.createNamedQuery("Person.findByEmail", Person.class).setParameter("email", email).getResultList().size() > 0){
					person = em.createNamedQuery("Person.findByEmail", Person.class).setParameter("email", email).getSingleResult();
					person.setLogger(log);
					
				}else{
					person = new Person(log);
					person.setEmail(email);
					newPersonFlag = true;
				}
				
				person.setName(name);
				person.addEvent(event);						
				
				if(person.getErrors().size() <= 0){				
					try{
						if(newPersonFlag){
							em.persist(person);
						}
						
						em.flush();
						em.getTransaction().commit();
					}catch(Exception ex1){
						person.addError("person", "An internal error has occured while adding the guest");
						log.notice("Exception catched while persistin and commiting new guest.");
						log.exception(ex1);
						
						try{
							em.getTransaction().rollback();
						}catch(Exception ex2){
							log.error("Exception catched while rolling back adding new guest");
							log.exception(ex2);
						}
						
					}finally{
						em.clear();
						em.close();
					}
				}
				
				if(person.getErrors().size() > 0){
					request.setAttribute("guest", person);
					request.setAttribute("event", this.evento);
					doGet(request, response);
				}else{
					response.sendRedirect(request.getContextPath() + "/event/" + event.getUrlEncodedName());
				}
				
			}catch (Exception ex){
				log.notice("Exception caught wehn adding a guest");
				log.exception(ex);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			
		} else {
			log.error("event id from the form does not correspond to the id of the url");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}