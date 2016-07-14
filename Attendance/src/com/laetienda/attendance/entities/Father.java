package com.laetienda.attendance.entities;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import com.laetienda.attendance.utilities.Logger;

public abstract class Father {
	
	Logger log;
	private HashMap<String, List<String>> errors;
	
	Father(){
		log = new Logger();
		errors = new HashMap<String, List<String>>();
	}
	
	Father(Logger log){
		this.log = log;
		errors = new HashMap<String, List<String>>();
	}
	
	public void setLogger(Logger log){
		this.log = log;
	}
	
	public void addError(String list, String error){
		log.info("Adding error: $list: " + list);
		log.info("Adding error: $error: " + error);
		
		List<String> errorList;
		
		if(errors.get(list) == null){
			errorList = new ArrayList<String>();
		} else{
			errorList = errors.get(list);			
		}
		
		errorList.add(error);
		errors.put(list, errorList);
	}
	
	public HashMap<String, List<String>> getErrors(){
		return this.errors;
	}
	
	protected Calendar getCalendarFormat(String fecha, String hora){
		
		log.debug("fecha: " + fecha);
		log.debug("hora: " + hora);
		
		String[] fechaParts = fecha.split("-");
		int year = Integer.parseInt(fechaParts[0]);
		int month = Integer.parseInt(fechaParts[1]) - 1;
		int day = Integer.parseInt(fechaParts[2]);
		
		String[] horaParts = hora.split(":");
		int hour = Integer.parseInt(horaParts[0]);
		int minute = Integer.parseInt(horaParts[1]);
		
		Calendar date = new GregorianCalendar(year, month, day, hour, minute);	
		log.debug("final date: " + date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH) + " hora: " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE));
		return date;
	}
	
	protected String calendarToString(Calendar date){
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX");
		
		String result = format1.format(date.getTime());
		log.debug("date: " + format2.format(date.getTime()));
		
		return result;
	}
	
	protected String dateToString(Calendar date){
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String result = format1.format(date.getTime());
				
		return result;
	}
	
	protected String timeToString(Calendar date){
		
		SimpleDateFormat format1 = new SimpleDateFormat("HH:mm");
		String result = format1.format(date.getTime());
				
		return result;
	}
	
	public Calendar getAhora(){
		return new GregorianCalendar();
	}
	
	public abstract Integer getId();

}
