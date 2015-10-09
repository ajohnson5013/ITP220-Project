package messageboaredv2.pkg1;

//PK's version

import java.util.*;

// Handles all the aggregate issues of users, like logging in
// and keeping track of the current user. --PK

public class Users 
{
	private ArrayList<User> users;
	private User currentUser;
	
	public Users()
	{
		users = new ArrayList<>();
	}
	
	public void addUser(User user)
	{
		users.add(user);
	}
	
	public User getCurrentUser()
	{
		return currentUser;
	}
	
	public boolean someoneLoggedIn()
	{
		if (currentUser == null)
			return false;
		
		return currentUser.isLoggedIn();
	}
	
	public void login(User user, String password)
	{
		if (currentUser != null && currentUser.isLoggedIn())
		{
			//You're already logged in...Nothing to do
			return;
		}
		
		user.tryLogin(password);
		if (user.isLoggedIn())
			currentUser = user;
	}
	
	public void logout()
	{
		if (currentUser != null)
			currentUser.logout();
	}
	
	public ArrayList<User> getUsers()
	{
		return users;
	}
}
