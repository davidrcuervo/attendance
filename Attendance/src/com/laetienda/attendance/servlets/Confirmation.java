package com.laetienda.attendance.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.entities.EventsPeople;
import com.laetienda.attendance.entities.Person;
import com.laetienda.attendance.entities.Event;
import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;

public class Confirmation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger log;
	private DB db;
	private String[] pathParts;
       
    public Confirmation() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
		log = (Logger)config.getServletContext().getAttribute("Logger");
		db = (DB)config.getServletContext().getAttribute("db");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("processiong get from confirmation servlet");
		
		pathParts = (String[])request.getAttribute("pathParts");
		
		// /confirm/event/:urlEventName
		if(pathParts.length == 4 && pathParts[2].equals("event") && db.findEventByUrl(pathParts[3]) != null){
			confirm(pathParts[3], request, response);
		}else{
			response.getWriter().append("Served at: ").append(request.getContextPath()).append("\n")
			.append("requested URL: ").append(request.getRequestURL()).append("\n");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void confirm(String eventEncodedUrlName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Confirmming guest to event");
		
		String encodedEmailGuest = request.getParameter("guest");
		String encryptedAnswer = request.getParameter("answer");
		String urlEncryptedEventId = request.getParameter("eventId");
		
		
		Person person = db.findGuestByEncodedEmail(encodedEmailGuest);
		int eventID = person.decryptUrlId(urlEncryptedEventId);
		String answer = person.decryptUrlString(encryptedAnswer);

		try{
			
							
			EntityManager em = db.getEntityManager();
			em.getTransaction().begin();
			
			Event evento = em.find(Event.class, eventID);
			EventsPeople ep = em.createNamedQuery("EventsPeople.findByUserAndEvent", EventsPeople.class).setParameter("person", person).setParameter("event", evento).getSingleResult();
			ep.setStatus(answer);
			
			try{
				em.flush();
				em.getTransaction().commit();
				
			}catch (Exception ex1){
				ep.addError("confirmation", "There was an internal errror while regestering your assistance");
				log.notice("Exception caught while comminting answer of the client");
				log.exception(ex1);
				
				try{
					em.getTransaction().rollback();
				}catch (Exception ex2){
					log.exception(ex2);
				}
			}finally{
				em.clear();
				em.close();
				request.setAttribute("eventConfirmation", ep);
				request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "thankyou/eventConfirm.jsp").forward(request, response);
			}
				
		}catch(Exception ex){
			log.notice("Exception caught while decoding encrypted answer");
			log.exception(ex);
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
