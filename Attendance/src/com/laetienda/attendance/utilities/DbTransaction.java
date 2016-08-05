package com.laetienda.attendance.utilities;

import javax.persistence.EntityManager;


public class DbTransaction {
	
	private EntityManager em;
	private Logger log;
	
	public DbTransaction(DB db, Logger log){
		
		try{
			em = db.getEntityManager();
			em.getTransaction().begin();
			this.log = log;
		}catch(Exception ex){
			log.critical("Error while starting database transaction");
			log.exception(ex);
		}
	}
	
	public boolean commit(){
		log.info("comminting changes");
		
		boolean result = false;
		
		try{
			
			em.flush();
			em.getTransaction().commit();
			result = true;
			
		}catch(Exception ex){
			
			try{
				em.getTransaction().rollback();
			}catch(Exception ex1){
				log.error("Exception caught while rolling back");
				log.exception(ex1);
			}
			
			log.notice("Exception caught while commiting in the database");
			log.exception(ex);
		}finally{
			em.clear();
			em.close();
		}
		
		return result;
	}
	
	public void close(){
		log.info("Closing entity manager");
		em.clear();
		em.close();
	}
	
	public EntityManager getEm(){
		return this.em;
	}
}
