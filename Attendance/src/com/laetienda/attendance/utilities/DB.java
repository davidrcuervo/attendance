package com.laetienda.attendance.utilities;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import com.laetienda.attendance.entities.Setting;
import com.laetienda.attendance.utilities.Logger;
import com.laetienda.attendance.entities.User;
import com.laetienda.attendance.entities.Event;
import com.laetienda.attendance.entities.Person;
import com.laetienda.attendance.entities.Email;

public class DB {
	
	private EntityManagerFactory emfactory;
    private Logger log;
    private HashMap<String, String> settings;
    private User user;
    
    public DB(String persistenceUnitName) throws IOException{
    	log = new Logger();
    	createEmFactory(persistenceUnitName);
    	buildSettingsMap();
    }
    
    public DB(Logger log, String persistenceUnitName) throws IOException{
    	this.log = log;
    	createEmFactory(persistenceUnitName);
    	buildSettingsMap();
    }
    
    
    
    private HashMap<String, String> buildSettingsMap() throws IOException{
    	log.info("Getting settings from database to set in object");
    	
    	settings = new HashMap<String, String>();
    	try{
    		EntityManager em = this.getEntityManager();
    		List<Setting> temp = em.createNamedQuery("Setting.findAll", Setting.class).getResultList();
    		em.clear();
    		em.close();
    		
    		for(Setting setting : temp){
    			settings.put(setting.getSetting(), setting.getValue());
    			
    		}
    		
    	}catch (Exception ex){
    		log.critical("Error while getting settings from database");
    		log.debug("Exception name: " + ex.getClass().getName());
    		log.debug("Exception message: " + ex.getMessage());
    	}
    	
    	return this.settings;
    }
    
    private void createEmFactory(String persistenceUnitName) throws IOException{
    	log.info("Creating entity manager. $persistenceUnitName: " + persistenceUnitName);
    	
    	try{
    		emfactory = Persistence.createEntityManagerFactory(persistenceUnitName);
    	}catch (IllegalStateException ex){
    		log.emergency("Can not create connection to the database because entity manager has been closed.");
    		throw new IOException(ex);
    	}catch(PersistenceException ex){
    		log.emergency(ex.getMessage());
    		throw new IOException(ex);
    	}
    }
    
    public void close(){
    	log.info("Closing database connection");
    	
    	emfactory.close();
    }
    
    public EntityManager getEntityManager() throws IllegalStateException {
    	
		EntityManager em; 
		em = emfactory.createEntityManager();
		return em;
    }
    
    public HashMap<String, String> getSettings(){
    	return this.settings;
    }
    
    public void setUser(User user){
    	this.user = user;
    }
    
    public User getUser(){
    	return this.user;
    }
    
    public User getUser(String username){
    	log.info("Getting user from username: $username: " + username);
    	User result = new User();
    	
    	try{
    		EntityManager em = emfactory.createEntityManager();
    		result = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
    		em.clear();
    		em.close();
    	}catch(Exception ex){
    		log.notice("User has not been found in the database");
    		log.debug("Exception name: " + ex.getClass().getName());
    		log.debug("Exception message: " + ex.getMessage());
    	}
    	
    	return result;
    }
    
    public Event findEventByUrl(String urlEncodedName){
    	log.info("Finding event by url. $urlEncodedName: " + urlEncodedName);
    			
    	Event result = null;
    	
    	try{
    		EntityManager em = getEntityManager();
    		result = em.createNamedQuery("Event.FindByUrl", Event.class).setParameter("urlEncodedName", urlEncodedName).getSingleResult();
    		em.clear();
    		em.close();
    	}catch(Exception ex){
    		log.notice("Exception caught while finding event by url");
    		log.exception(ex);
    	}
    	
    	return result;	
    }
    
    public Person findGuestByEncodedEmail(String encodedEmail){
    	log.info("Finding person by encoded email");
    	
    	Person result = null;
    	
    	try{
    		EntityManager em = getEntityManager();
    		String email = URLDecoder.decode(encodedEmail, "UTF-8");
    		result = em.createNamedQuery("Person.findByEmail", Person.class).setParameter("email", email).getSingleResult();
    		em.clear();
    		em.close();
		}catch (Exception ex){
			log.notice("Exeption caught while finding person by url");
			log.exception(ex);
		}finally{
			
		}
    	return result;
    }
    
    public Email findEmailByEncodedName(String encodedEmailName){
    	log.info("Finding person by encoded email. $encodedEmailName: " + encodedEmailName);
    	
    	Email email = null;
    	
    	try{
    		EntityManager em = getEntityManager();
    		String emailName = URLDecoder.decode(encodedEmailName, "UTF-8");
    		email = em.createNamedQuery("Email.findByName", Email.class).setParameter("emailName", emailName).setParameter("user", getUser()).getSingleResult();
    		em.clear();
    		em.close();
    	}catch(Exception ex){
    		log.notice("Exception caught while finding Email by name");
    		log.exception(ex);
    	}
    	return email;
    }
}
