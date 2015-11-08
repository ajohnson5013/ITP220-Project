package itp220.group.projectDBEnabled;

//PK's version

import itp220.group.project.*;
import java.util.*;

// I wrapped all the input gathering here so that the driver class wasn't so
// crowded. This handles the nitty gritty of getting scans and such.
public class TextInputs
{
	private Scanner scan;
	private String lastInput;
	private String[] altInputs;
	
	public TextInputs()
	{
		scan = new Scanner(System.in);
		altInputs = new String[0];
	}
	
	private void print(String output)
	{
		System.out.println(output);
	}
	
	public User chooseUser(Collection<User> users)
	{
		return chooseObject(users);
	}
	
	public Thread chooseThread(Collection<Thread> threads)
	{
		return chooseObject(threads);
	}
	
	public Post choosePost(Collection<Post> posts)
	{
		return chooseObject(posts);
	}
	
	private <T> T chooseObject(Collection<T> objects)
	{
            // This looks like a mess, but once I saw that all of our 
            // choose__fill in the blank__() methods look the same, 
            // I made this a generic method to save LOTS of typing. 
            // Basically, wherever you see 'T' in this method, 'T' gets
            // replaced by whatever type you feed it in...
            // So choosePost(Collection<Post> posts) runs this method as 
            // if all the 'T's were 'Post'. 
            
		ArrayList<T>objectList = new ArrayList<>(objects);
		StringBuilder sb = new StringBuilder("\n\n");
		String delim = "";
		for (int i = 0; i < objects.size(); i++)
		{
                    // For every object in the collection passed in...
			
                    sb.append(delim);                           // Add nothing in front for the first,
                    sb.append( (i + 1) );                       // add a number for the user to see,
                    sb.append(". ");                            // add a decimal to make it look nice,
                    sb.append(objectList.get(i).toString());    // add the object's description,
                    delim = "\n";                               // and make it so that any objects past the first add a newline in front of them.
		}
		
                //The above loop makes a string something like:
                // 1. Object 1
                // 2. Object 2
                // 3. Object 3
                // ...
		String prompt = sb.toString();
		
		int choice = readInt(prompt);
                
                // 0 is the default 'back' or 'quit' input
		while (choice != 0 && (choice < 1 || choice > objectList.size()))
		{
			print("Invalid choice.");
			choice = readInt(prompt);
		}
		
		if (choice == 0)
			return null;
		
		return objectList.get(choice - 1);
	}
	
	public String getPassword()
	{
		print("Password: ");
		lastInput = scan.nextLine();
		return lastInput;
	}
	
        public String getName()
        {
            print("New user name: ");
            lastInput = scan.nextLine();
            return lastInput;
        }
        
        public String getNewPassword()
        {
            String attempt1 = "pass1";
            String attempt2 = "pass2";
            
            while (!attempt1.equals(attempt2))
            {
                attempt1 = getPassword();
                print("Repeat password: ");
                lastInput = scan.nextLine();
                attempt2 = lastInput;
                
                if (!attempt1.equals(attempt2))
                    print("The passwords do not match.");
            }
            
            return attempt1;
        }
        
	public void setValidAlts(String alts[])
	{
            // altInputs allows the readInt method to terminate the loop
            // if it receives an input in the altInputs array. You'll see why
            // I did this.
		altInputs = alts;
	}
	
	public void clearValidAlts()
	{
            // Sets it up so that readInt will only accept integers again
		altInputs = new String[0];
	}
	
	public String getPostContent(int maxLen, String prompt)
	{
		print(prompt);
		String content = scan.nextLine();
		while (content.length() > maxLen)
		{
			print("Maximum post length is " + maxLen + " characters.");
			print(prompt);
			content = scan.nextLine();
		}
		lastInput = content;
		return content;
	}
	
	public String getPostContent(int maxLen)
	{
		String prompt = "Enter post content (" + maxLen + " chars max):";
		return getPostContent(maxLen, prompt);
	}
	
	public String getLastInput()
	{
		return lastInput;
	}
	
	private boolean isValidAlt(String input)
	{
		for(String test : altInputs)
			if (test.equals(input))
				return true;
		
		return false;
	}
	
	private int readInt(String prompt)
	{
		print(prompt);
		int choice = 0;
		boolean good = false;
		try
		{
			lastInput = scan.nextLine();
			choice = Integer.parseInt(lastInput);
			good = true;
		}
		catch (Exception e)
		{
			if (isValidAlt(lastInput))
				good = true;
			else
				print("Invalid input.");
		}
		
		if (good)
			return choice;
		else
			return readInt(prompt);
	}
	
	public String chooseOption(ArrayList<String> options, String prompt)
	{
		print(prompt);
		return chooseObject(options);
	}
	
	public String getThreadTopic()
	{
		print("Thread topic:");
		lastInput = scan.nextLine();
		return lastInput;
	}
	
	public void close()
	{
		scan.close();
	}
	
	public String getSearchText()
	{
		print("What text you you like to search for?");
		lastInput = scan.nextLine();
		return lastInput;
	}
}
