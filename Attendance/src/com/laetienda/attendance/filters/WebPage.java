package com.laetienda.attendance.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.laetienda.attendance.beans.Attendance;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.entities.User;
import com.laetienda.attendance.utilities.DB;

public class WebPage implements Filter {
	
	private DB db;
	private Logger log;
	private Attendance app;

    public WebPage() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("Running filter WebPage");
		
		HttpServletRequest httpReq = (HttpServletRequest)request;
		app.setRequest(httpReq);
		
		String path = httpReq.getRequestURI();
		log.debug("path = request.getRequestURI(); -> $path: " + path);
		
		String ctxPath = httpReq.getContextPath();
		path = path.substring(path.indexOf(ctxPath) + ctxPath.length());
		String[] pathParts = path.split("/");
		
		log.debug("ctxPath = request.getContextPath(); -> $path: " + ctxPath);
		log.debug("path = path.substring(path.indexOf(ctxPath) + ctxPath.length()); -> $path: " + path);
		
		for(int c=0; c < pathParts.length; c++){
			log.debug("pathParts[" + c + "] = " + pathParts[c]);
		}
		
		if(pathParts.length > 0 && (pathParts[0].equals("confirm"))){
			log.info("This request does not ask for authorization");
		}else{
			String username = (String)httpReq.getRemoteUser();
			User user = db.getUser(username);
			user.setLogger(log);
			db.setUser(user);
			httpReq.setAttribute("user", user);
		}
		
		httpReq.setAttribute("pathParts", pathParts);
		httpReq.setAttribute("app", app);
		
		chain.doFilter(request, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		log = (Logger)fConfig.getServletContext().getAttribute("Logger");
		db = (DB)fConfig.getServletContext().getAttribute("db");
		app = new Attendance(log);
	}
}
