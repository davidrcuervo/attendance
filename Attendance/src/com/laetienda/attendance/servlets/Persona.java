package com.laetienda.attendance.servlets;

import java.io.IOException;
import java.util.List;
import java.net.URLDecoder;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.entities.Person;
import com.laetienda.attendance.entities.User;

public class Persona extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String[] pathParts;
	private Logger log;
	private DB db;
	private Person person;
	private String referer;
       
    public Persona() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		log = (Logger)config.getServletContext().getAttribute("Logger");
		db = (DB)config.getServletContext().getAttribute("db");
	}
	
	private void preRun(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		pathParts = (String[])request.getAttribute("pathParts");
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		preRun(request, response);
		
		// /guest/all
		if(pathParts.length == 3 && pathParts[2].equals("all")){
			request.setAttribute("guests", findAllGuests(request, response));
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "guest/all.jsp").forward(request, response);
			
		// /guest/:urlEncodedEmail	
		}else if(pathParts.length == 3 && findPersonByUrl(pathParts[2], request, response)){
			request.setAttribute("person", person);
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "guest/show.jsp").forward(request, response);
		
		// /guest/edit/:urlEncodedMail
		}else if(pathParts.length == 4  && pathParts[2].equals("edit") && findPersonByUrl(pathParts[3], request, response)){
			if(request.getParameter("submit") == null){
				request.setAttribute("guest", person);
			}
			request.setAttribute("cancel_link", findReferer(request, response));
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "guest/edit.jsp").forward(request, response);
		}
		else{
			response.getWriter().append("Served at: ").append(request.getContextPath()).append("<br />")
								.append("requested URL: ").append(request.getRequestURL()).append("<br />");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("do post in persona servlet");
		
		preRun(request, response);
		
		if(request.getParameter("submit").equals("editGuest")){
			editPerson(request, response);
		}
	}
	
	private List<Person> findAllGuests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		List<Person> guests = null;
				
		try{
			User user = (User)request.getAttribute("user");
			EntityManager em = db.getEntityManager();
			guests = em.createNamedQuery("Person.findAllByUser", Person.class).setParameter("user", user).getResultList();
			em.clear();
			em.close();
			
		}catch(Exception ex){
			log.error("Error while finding all guests");
			log.exception(ex);
		}
		
		return guests;
	}
	
	private boolean findPersonByUrl(String urlEncodedEmail, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Searching person by encrypted email: urlEncodedEmail: " + urlEncodedEmail);
		boolean result = false;
		
		try{
			String email = URLDecoder.decode(urlEncodedEmail, "UTF-8");
			EntityManager em = db.getEntityManager();
			person = em.createNamedQuery("Person.findByEmail", Person.class).setParameter("email", email).getSingleResult();
			em.clear();
			em.close();
			result = true;
			
		}catch(Exception ex){
			log.notice("Exception while looking for person by encrypted mail");
			log.exception(ex);
		}
		
		return result;
	}
	
	private String findReferer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

		String eventUrl = request.getParameter("eventUrl");
		
		if(eventUrl == null){
			referer = request.getContextPath() + "/guest/" + person.getUrlEncodedEmail();
		}else{
			referer = request.getContextPath() + "/event/" + eventUrl + "/guest/" + person.getUrlEncodedEmail();
		}
		
		return referer;
	}
	
	private void editPerson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("editing person");
		
		try{
			EntityManager em = db.getEntityManager();
			em.getTransaction().begin();
			
			person = em.find(Person.class, person.getId());
			person.editName(request.getParameter("name"));
			person.setEmail(request.getParameter("email"));
			
			if(person.getErrors().size() <= 0){
				try{
					em.flush();
					em.getTransaction().commit();
				}catch (Exception ex1){
					
					person.addError("guest", "Internal error occured when saving changes");
					log.notice("Error while comminting chages of person");
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
			
			if(person.getErrors().size() > 0){
				request.setAttribute("guest", person);
				doGet(request, response);
			}else{
				response.sendRedirect(findReferer(request, response));
			}
			
		}catch (Exception ex){
			log.error("Exception occured while editing person");
			log.exception(ex);
		}
	}
}
