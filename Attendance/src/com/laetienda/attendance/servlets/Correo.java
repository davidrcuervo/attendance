package com.laetienda.attendance.servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.entities.Email;
import com.laetienda.attendance.entities.User;

public class Correo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String[] pathParts;
	private Logger log;
	private DB db;
	private Email email;
	
    public Correo() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		log = (Logger)config.getServletContext().getAttribute("Logger");
		db = (DB)config.getServletContext().getAttribute("db");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("processiong get from correo(email) servlet");
		
		pathParts = (String[])request.getAttribute("pathParts");
		
		// /email
		if(pathParts.length == 2){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/all.jsp").forward(request, response);
		
		// /email/add
		}else if(pathParts.length == 3 && pathParts[2].equals("add")){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/add.jsp").forward(request, response);
		
		// /email/:urlEncodedEmailName
		}else if(pathParts.length == 3 && db.findEmailByEncodedName(pathParts[2]) != null){
			request.setAttribute("email", db.findEmailByEncodedName(pathParts[2]));
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/show.jsp").forward(request, response);
			
		// /email/edit/:urlEncodedEmailName	
		}else if(pathParts.length == 4 && pathParts[2].equals("edit") && db.findEmailByEncodedName(pathParts[3]) != null){
			if(email == null){
				email = db.findEmailByEncodedName(pathParts[3]);
			}
			request.setAttribute("email", pathParts[3]);
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/edit.jsp").forward(request, response);
			
		// /email/delete/:urlEcodedEmailName
		}else if(pathParts.length == 4 && pathParts[2].equals("delete") && db.findEmailByEncodedName(pathParts[3]) != null){
			
		}
		else{
			response.getWriter().append("Served at: ").append(request.getContextPath()).append("\n")
					.append(request.getRequestURI()).append("\n");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing form from email servlet");
		
		// add or edit event
		if(request.getParameter("submit").equals("addEmail") || request.getParameter("submit").equals("editEmail")){
			addOrEdit(request, response);
		}
	}
	
	private void addOrEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Adding or editing email");
		
		String submit = request.getParameter("submit");
		String name = request.getParameter("name");
		String emailContent = request.getParameter("content");
		String emailID = request.getParameter("emailID");
		
		try{
			EntityManager em = db.getEntityManager();
			em.getTransaction().begin();
			
			if(submit.equals("addEmail")){
				email = new Email(log);
				email.setUser((User)request.getAttribute("user"));
			}else{
				email = em.find(Email.class, Integer.parseInt(emailID));
			}
			
			email.setName(name);
			email.setEmail(emailContent);
			
			if(email.getErrors().size() <= 0){
				
				try{
					if(submit.equals("addEmail")){
						em.persist(email);
					}
					
					em.flush();
					em.getTransaction().commit();
					
				}catch(Exception ex1){
					email.addError("email", "An internal error occurred while saving the Email");
					log.notice("Exception caught while comminting email");
					log.exception(ex1);
					
					try{
						em.getTransaction().rollback();
					}catch(Exception ex2){
						log.error("Exception caught while rolling back email trasaction");
						log.exception(ex2);
					}
					
				}finally{
					em.clear();
					em.close();
				}
			}
			
			if(email.getErrors().size() > 0){
				request.setAttribute("email", email);
				doGet(request, response);
			}else{
				response.sendRedirect(request.getContextPath() + "/email/" + email.getUrlEncodedName());
			}
			
		}catch(Exception ex){
			log.notice("Exception caught while adding or editing an email");
			log.exception(ex);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
