package com.hatraz.bucketlist.service;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
		.getPersistenceManagerFactory("transactions-optional");
 
	public PMF() {
	}
 
	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}