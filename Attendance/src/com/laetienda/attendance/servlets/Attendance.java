package com.laetienda.attendance.servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.laetienda.attendance.entities.User;
import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;

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
		
		user = (User)request.getServletContext().getAttribute("user");
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
		}
		else{
			log.info("Visitor has requested and invalid URL. url: " + request.getRequestURI());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
}
