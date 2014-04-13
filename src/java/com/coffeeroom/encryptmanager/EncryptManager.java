package com.coffeeroom.encryptmanager;

import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

public final class EncryptManager {					// manager to encrypt passwords
	
	private static EncryptManager instance;

	private EncryptManager()
	{
		// nothing to do
	}

	public synchronized String encrypt(String text) throws Exception
	{
		MessageDigest md = null;
		try
		{
			md = MessageDigest.getInstance("SHA"); 		// give the algorithm
			md.update(text.getBytes("UTF-8"));		// give the character encoding
		}
		catch(Exception e)
		{
			throw new Exception(e.getMessage());
		}

		byte raw[] = md.digest(); 
		String hash = (new BASE64Encoder()).encode(raw);		// encrypt the text
		return hash; 											// return the encryption
	}
  
	public static synchronized EncryptManager getInstance() 	// create an instance of EncryptManager
	{
		if(instance == null)
		{
			instance = new EncryptManager(); 
		} 
		return instance;
	}
}