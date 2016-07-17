package com.laetienda.attendance.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.entities.User;
import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.DbTransaction;
import com.laetienda.attendance.utilities.Logger;
import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;
import com.laetienda.attendance.utilities.DbTransaction;

public class Attendance extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Logger log;
	private DB db;
	private String[] pathParts;
	private User user;
       
    public Attendance() {
        super();
        // TODO Auto-generated constructor stub
    }

	public void init(ServletConfig config) throws ServletException {
		log = (Logger)config.getServletContext().getAttribute("Logger");
		db = (DB)config.getServletContext().getAttribute("db");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//user = (User)request.getServletContext().getAttribute("user");
		pathParts = (String[])request.getAttribute("pathParts");
		
		// root
		if(!(pathParts.length > 0)){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "home.jsp").forward(request, response);
			
		// /home
		} else if(pathParts[1].equals("home") && pathParts.length == 2){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "home.jsp").forward(request, response);
			
		// /logout	
		} else if(pathParts[1].equals("logout") && pathParts.length == 2){
			this.logout(request, response);
				
		// /password	
		} else if(pathParts[1].equals("password") && pathParts.length == 2){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "password.jsp").forward(request, response);
		
		// /email_server
		} else if(pathParts.length == 2 && pathParts[1].equals("email_server")){
			request.getRequestDispatcher(db.getSettings().get("jsp_folder") + "email/server.jsp").forward(request, response);
		}
		else{
			log.info("Visitor has requested and invalid URL. url: " + request.getRequestURI());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Processing form from email servlet");
		
		// email server settings
		if(request.getParameter("submit").equals("email_server")){
			emailServer(request, response);
		}else{
			doGet(request, response);
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Logging out. Invalidating session");
		
		try{
			request.getSession().invalidate();
		}catch (NullPointerException ex){
			log.info(ex.getMessage());
		}finally{
			response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/home"));
		}
	}
	
	private void emailServer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("Saving email server settings");
		
		String server = request.getParameter("server");
		String username = request.getParameter("username");
		String port = request.getParameter("port");
		String password = request.getParameter("password");
		user = (User)request.getAttribute("user");
		request.removeAttribute("user");
		
		DbTransaction trans = new DbTransaction(db, log);
		
		user = trans.getEm().find(User.class, user.getId());
		user.setEmailConnection(server, username, password, port);
		
		request.setAttribute("user", user);
		if(user.getErrors().size() <= 0){
			if(trans.commit()){
				response.sendRedirect(request.getContextPath());
			}else{
				user.addError("user email", "Internal error ocurred while saving email server settings");
				doGet(request, response);
			}
		}else{
			trans.close();
			doGet(request,response);
		}
	}
}
