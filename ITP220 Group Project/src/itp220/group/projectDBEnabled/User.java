package itp220.group.projectDBEnabled;

//PK's version

import itp220.group.project.*;
import java.security.*;

// The biggest difference here is that I you wouldn't want
// to store user passwords in plaintext at any point. Check out 
// MD5 hash online to see what I'm talking about below. 

// It's not a requirement of the assignment, but it is nice to do
// anyhow since this is the kind of thing that got Target and other
// large companies who've been hacked in trouble. --PK

// ***EDIT: We've decided to forego the hashing just for simplicity. --PK

public abstract class User 
{
	protected String name;
	protected String passHash;
	protected int ID;
	protected boolean isLoggedIn;
	protected static int nextID;
	
	public User()
	{
		setID();
		isLoggedIn = false;
		this.passHash = null;
	}
	
        public User(String name, String passHash)
        {
            setID();
            this.name = name;
            this.passHash = passHash;
            isLoggedIn = false;
        }
        
	public User(String name, String passHash, int ID)
	{
		this.name = name;
		this.passHash = passHash;
		this.ID = ID;
		isLoggedIn = false;
	}
	
	public User(String name, int ID)
	{
		this.name = name;
		this.passHash = null;
		this.ID = ID;
		isLoggedIn = false;
	}
	
	public User(String name)
	{
		this.name = name;
		setID();
		this.passHash = null;
		isLoggedIn = false;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	protected void setID()
	{
		ID = nextID;
		nextID++;
	}
	
	public void tryLogin(String password)
	{
		if (isLoggedIn)
			return;
		
		String newHash = createHash(password);
		if (passHash == null)
			passHash = newHash;
		
		isLoggedIn = newHash.equals(passHash);		
	}
	
	public boolean isLoggedIn()
	{
		return isLoggedIn;
	}
	
	public void logout()
	{
		isLoggedIn = false;
	}
	
	public String createHash(String md5) 
	{
		// creates an MD5 hash of the input string so that the plaintext
		// password is never stored anywhere. Basic digital security.
	/*	
	   try 
	   {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        
	        StringBuffer sb = new StringBuffer();
	        
	        for (int i = 0; i < array.length; ++i) 
	        {
	        	sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        
	        return sb.toString();
	    } 
	   
	   	catch (java.security.NoSuchAlgorithmException e) 
	   	{
	   		//do nothing
	    }
	  */  
	   return md5;
	}
	
	public String getName()
	{
		return name;
	}
        
        public boolean equals(User other)
        {
            return this.ID == other.ID;
        }
        
        public int getID()
        {
            return ID;
        }
        
        public String getPassword()
        {
            return passHash;
        }
        
        public void setID(int id)
        {
            ID = id;
        }
}
