package com.laetienda.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.utilities.DbTransaction;
import com.laetienda.attendance.entities.Email;
import com.laetienda.attendance.entities.User;

public class Correo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String[] pathParts;
	private Logger log;
	private DB db;
	private Email email;
	private User user;
	
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
		user = (User)request.getAttribute("user");
		
		// /email
		if(pathParts.length == 2){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/all.jsp").forward(request, response);
		
		// /email/add
		}else if(pathParts.length == 3 && pathParts[2].equals("add")){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/add.jsp").forward(request, response);
		
		// /email/:urlEncodedEmailName
		}else if(pathParts.length == 3 && user.getEmailByUrl(pathParts[2]) != null){
			
			request.setAttribute("email", user.getEmailByUrl(pathParts[2]));
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/show.jsp").forward(request, response);
			
		// /email/edit/:urlEncodedEmailName	
		}else if(pathParts.length == 4 && pathParts[2].equals("edit") && user.getEmailByUrl(pathParts[3]) != null){
			if(email == null){
				email = user.getEmailByUrl(pathParts[3]);
			}
			request.setAttribute("email", email);
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/edit.jsp").forward(request, response);
			
		// /email/delete/:urlEcodedEmailName
		}else if(pathParts.length == 4 && pathParts[2].equals("delete") && user.getEmailByUrl(pathParts[3]) != null){
			delete(pathParts[3], request, response);
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
		String emailSubject = request.getParameter("subject");
		String emailID = request.getParameter("emailID");
		User tempUser = (User)request.getAttribute("user");
		
		DbTransaction trans = new DbTransaction(db, log);
		
		if(submit.equals("addEmail")){
			email = new Email(log);
			User user = trans.getEm().find(User.class, tempUser.getId());
			user.addEmail(email);
		}else{
			email = trans.getEm().find(Email.class, Integer.parseInt(emailID));
		}
		
		email.setEditUser(tempUser);
		email.setName(name);
		email.setSubject(emailSubject);
		email.setEmail(emailContent);
			
		if(email.getErrors().size() <= 0 && trans.commit()){
			response.sendRedirect(request.getContextPath() + "/email/" + email.getUrlEncodedName());
		}else{
			request.setAttribute("email", email);
			doGet(request, response);
		}
	}

	private void delete(String urlEncodedEmailName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		log.info("Deleting email. $urlEncodedEmailName: " + urlEncodedEmailName);
		
		User tempUser = (User)request.getAttribute("user");
		String tempUrl = request.getParameter("url");
		
		DbTransaction tran = new DbTransaction(db, log);
		
		User user = tran.getEm().find(User.class, tempUser.getId());
		Email email = tran.getEm().find(Email.class, user.getEmailByUrl(urlEncodedEmailName).getId());
		
		user.removeEmail(email);
		
		if(user.getErrors().size() <= 0 && tran.commit()){	
			response.sendRedirect(request.getContextPath() + "/" + tempUrl);
		}else{
			
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/edit.jsp").forward(request, response);
		}
	}
}