package com.laetienda.attendance.listeners;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.laetienda.attendance.utilities.DB;
import com.laetienda.attendance.utilities.Logger;

public class LoadUtilities implements ServletContextListener {
	
	private Logger log;
	private DB db;

    public LoadUtilities() {}

    public void contextDestroyed(ServletContextEvent arg0)  { 
    	log.info("Destroying context");
    	
    	db.close();
    }

    public void contextInitialized(ServletContextEvent arg0)  { 
    	ServletContext sc = arg0.getServletContext();
    	
    	initializeLogger(sc);
    	intializeDB(sc);
    	
    }
    
    private void intializeDB(ServletContext sc){
    	log.info("Initializing database....");
    	
    	String persistenceUnitName = (String)sc.getInitParameter("persistence-unit-name");
    	
    	try{
    		db = new DB(log, persistenceUnitName);
    		//log.setDatabaseConnection(db);
    		    		
    		log.debug("The application has been succesfully conected to the database");
    		
    		sc.setAttribute("db", db);
    		
    	}catch (IOException ex){
    		log.emergency("Can not create connection to the database because entity manager has been closed.");
    		System.exit(1);
    	}
    }
    
    private void initializeLogger(ServletContext sc){
    	
    	this.log = new Logger();
    	//String logsFile = sc.getRealPath((String)sc.getInitParameter("logs-file"));
    	//log.setFile(logsFile);
    	//log.debug("Logs file is: " + log.getFilePath());
    	
    	sc.setAttribute("Logger", log);
    	log.debug("Logger has been initialized");
    }
	
}
