package com.hatraz.bucketlist.services_old;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory emfInstance = 
			Persistence.createEntityManagerFactory("transactions-optional");
	
	private EMF() {}
	
	public static EntityManagerFactory get() {
		System.out.println("Testing");
		return emfInstance;
	}
}
