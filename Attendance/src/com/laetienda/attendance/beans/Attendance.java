package com.laetienda.attendance.beans;

import javax.servlet.http.HttpServletRequest;
import com.laetienda.attendance.utilities.Logger;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Attendance {
	
	private Logger log;
	private HttpServletRequest request;
	private String url;
	private Calendar ahora;
	
	public Attendance(Logger log){
		this.log = log;
		ahora = new GregorianCalendar();
	}
	
	public void setRequest(HttpServletRequest request){
		this.request = request;
		buildUrl();
	}
	
	private String buildUrl(){
		
		int port = request.getServerPort();
		url = request.getHeader("X-Forwarded-Proto") + "://" + request.getServerName();
		//url = request.getScheme() + "://" + request.getServerName();
		
		if(!(port == 80 || port == 443)){
			url += ":" + Integer.toString(port);
		}
		
		url += request.getContextPath();
				
		return url;
		
	}
	
	public String getUrl(){
		return url;
	}
	
	public Calendar getAhora(){
		return this.ahora;
	}
}
