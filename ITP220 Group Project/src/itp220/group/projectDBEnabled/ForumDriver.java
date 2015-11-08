package itp220.group.projectDBEnabled;

//PK's version edited by Aaron

import itp220.group.project.*;
import java.util.*;

public class ForumDriver {
	
        private static User currentUser;
	private static TextInputs inputs;
	private static NationalsJDBCConnection database;
        
	private static final int POST_MAX_LEN = 300; //max char length of post content
	
	public static void main(String[] args) {
		
		inputs = new TextInputs();
		currentUser = null;
		database = new NationalsJDBCConnection();
                
		loadData();
		
		String choice = mainMenu();
		while (!choice.equals(MenuChoices.QUIT))
		{
			switch(choice)
			{
			case MenuChoices.LOGIN:
				login();
				break;
			case MenuChoices.LOGOUT:
				logout();
				break;
			case MenuChoices.VIEW_THREADS:
				viewThreads();
				break;
			case MenuChoices.NEW_THREAD:
				createThread();
				break;
			case MenuChoices.SEARCH_MESSAGEBOARD:
				searchMessageBoard();
				break;
			default:
				//do nothing
			}
			
			choice = mainMenu();
		}
		
		print("Goodbye!");
		inputs.close();
		System.exit(0);

	}
	
	private static void print(String output)
	{
		System.out.println(output);
	}
	
	private static void loadData()
	{
//		users.addUser(new RegularUser("Bob", "itp220"));
//		users.addUser(new RegularUser("Dave"));
//		users.addUser(new RegularUser("Scott"));
	}
	
	private static String mainMenu()
	{
		ArrayList<String> options = new ArrayList<>();
		Collections.addAll(options, 
				MenuChoices.LOGIN,
				MenuChoices.LOGOUT,
				MenuChoices.VIEW_THREADS,
				MenuChoices.NEW_THREAD,
				MenuChoices.SEARCH_MESSAGEBOARD,
				MenuChoices.QUIT);
		
		String result = inputs.chooseOption(options, "Main Menu:");
		while (result == null)
		{
			print("Invalid choice.");
			result = inputs.chooseOption(options,  "Main Menu:");
		}
		
		return result;
	}
	
	private static void searchMessageBoard()
	{
		ArrayList<String> options = new ArrayList<>();
		Collections.addAll(options,
				MenuChoices.SEARCH_AUTHORS,
				MenuChoices.SEARCH_TEXT);
		String result = inputs.chooseOption(options, 
				"What would you like to search for?" +
				"\nPress 0 to go back.");
		
		if (result == null)
			return;
		
		switch (result)
		{
		case MenuChoices.SEARCH_AUTHORS:
			searchAuthors();
			break;
		case MenuChoices.SEARCH_TEXT:
			searchText();
			break;
		default:
				//do nothing;	
		}
		
	}
	
	
	private static void searchAuthors()
	{
                ArrayList<User> users = database.getUsers();
		User author = inputs.chooseUser(users);
		ArrayList<Thread> threads = database.getThreadsByAuthor(author);
		if (threads.isEmpty())
		{
			print("No threads found for this author.");
			return;
		}
		
		print(threads.size() + " threads found by " + author.getName() + ":");
		viewThreads(threads);
	}
	
	private static void searchText()
	{
		String text = inputs.getSearchText();
		ArrayList<Thread> threads = database.getThreadsWithText(text);
		if (threads.isEmpty())
		{
			print("No threads found with matching text.");
			return;
		}
		
		print(threads.size() + " thread(s) found with matching text [" + text + "]:");
		viewThreads(threads);
	}
	
	private static boolean loginCheck()
	{
		if (currentUser == null)
			print("You must be logged in to do that.");
		
		return currentUser != null;
	}
	
	private static void login()
	{
		if (currentUser != null)
		{
			print("Already logged in as " + currentUser.getName() + ".");
			return;
		}
		
                ArrayList<String> options = new ArrayList<>();
		Collections.addAll(options, 
				MenuChoices.EXISTING_USER,
				MenuChoices.NEW_USER);
		
                String loginPrompt = "Login:\n(Press 0 to go back.)";
		String result = inputs.chooseOption(options, loginPrompt);
		if (result == null)
                        return;
                
                switch(result)
                {
                    case MenuChoices.EXISTING_USER:
                        loginExisting();
                        break;
                    case MenuChoices.NEW_USER:
                        loginNew();
                        break;
                    default:
                        //do nothing
                }
                
		
	}
	
        private static void loginNew()
        {
            String userName = inputs.getName();
            String password = inputs.getNewPassword();
            User newUser = new RegularUser(userName, password);
            database.createNewUser(newUser);
            
            print("Created and logged in as " + userName + ".");
            currentUser = newUser;
        }
        
        private static void loginExisting()
        {
            print("");
            print("");
            print("Select user:\n(Press 0 to go back)");
            
            ArrayList<User> users = database.getUsers();
            User user = inputs.chooseUser(users);
            
            if (user == null)
                return;
            
            String userName = user.getName();
            String password = inputs.getPassword();

            user.tryLogin(password);

            if (!user.isLoggedIn())
                    print("Incorrect password.");
            else
            {
                    print("Successfully logged in as " + userName + ".");
                    currentUser = user;
            }
        }
        
	private static void logout()
	{
		if (currentUser != null)
		{
			print(currentUser.getName() + " logged out.");
                        currentUser = null;
		}
		else
			print("No one is logged in.");
	}
	
	private static void viewThreads()
	{
		// If you call viewThreads() without argument, it 
		// assumes you want all threads from the MessageBoard.
		viewThreads(database.getThreads());
	}
	
	private static void viewThreads(ArrayList<Thread> threadList)
	{
		//Basic instructions to user
                print("");
                print("");
		print("Enter a thread's number to read.");
		print("Press 'r' to sort the threads by rating.");
		print("Press 'd' to sort the threads by date.");
                print("Press 't' to sort the threads by topic.");
		print("Press 0 to go back.");
		print("");
		
		inputs.setValidAlts(new String[]{"r", "d", "t"});
		Thread selectedThread = inputs.chooseThread(threadList);
		inputs.clearValidAlts();
		
		switch (inputs.getLastInput())
		{
		case "r":
                        threadList.sort(new PostRatingComparator());
			viewThreads(threadList);
			return;
		case "d":
			Collections.sort(threadList);
			viewThreads(threadList);
			return;
                case "t":
                        threadList.sort(new ThreadTopicComparator());
                        viewThreads(threadList);
                        return;
		default:
			if (selectedThread == null)
				return;
		}
		
		viewPosts(selectedThread, new PostDateComparator());
	}
	
	private static void postReply(Thread thread)
	{
		if (!loginCheck())
			return;
		
		User author = currentUser;
		String content = inputs.getPostContent(300);
		database.replyToThread(thread, author, content);
	}
	
	private static void viewPosts(Thread thread, Comparator<Post> sortMethod)
	{
		//Basic instructions for users
                print("");
                print("");
		print("Enter a post's number to rate (or delete if you're the author)");
		print("Press 'p' to post a reply.");
		print("Press 'r' to sort by rating.");
		print("Press 'd' to sort by date.");
		print("Press 0 to go back.");
		print("\n" + thread.toString());
		print("\n-----------------------------\n");
		
		
		// Normally, chooseObject() (which choosePost feeds into)
		// won't accept non-numeric input. This lets it accept the 
		// following.
		inputs.setValidAlts(new String[]{"r", "d", "p"});
                database.getFullThread(thread);
                ArrayList<Post> posts = thread.getAllPosts();
                Collections.sort(posts, sortMethod);
		Post selectedPost = inputs.choosePost(posts);
		
		// Clear the valid alternate inputs so you don't cause problems
		// later
		inputs.clearValidAlts();
		
		switch(inputs.getLastInput())
		{
		case "p":
			postReply(thread);
			viewPosts(thread, sortMethod);
			return;
		case "d":
			thread.sortRepliesByDate();
			viewPosts(thread, new PostDateComparator());
			return;
		case "r":
			thread.sortRepliesByRating();
			viewPosts(thread, new PostRatingComparator());
			return;
		default:
			//This block is reached if they input 0
			if (selectedPost == null)
			{
				viewThreads();
				return;
			}
		}
		
		// If they've selected a post, rate it and then show the posts again.
                rateOrDeletePost(selectedPost, thread);
                database.getFullThread(thread);
                viewPosts(thread, sortMethod);
	}
	
	public static void rateOrDeletePost(Post post, Thread thread)
	{
		final String UP = "Upvote";
		final String DOWN = "Downvote";
		final String DELETE = "Delete post";
                
               if (!loginCheck())
			return;
		
                String options[];
                if (!post.isAuthoredBy(currentUser) ||
                        post.isThreadStarter(thread))
                    options = new String[]{UP, DOWN}; 
                else
                    options = new String[]{UP, DOWN, DELETE};
                
		ArrayList<String> optionsList = new ArrayList<>(Arrays.asList(options));
		
		String choice = inputs.chooseOption(optionsList, "Press 0 to cancel.");
		
		// If they press 0, choice == null.
		if (choice == null)
			return;
		
		switch(choice)
                {
                    case UP:
                        database.upVote(currentUser, post);
                        break;
                    case DOWN:
                        database.downVote(currentUser, post);
                        break;
                    case DELETE:
                        deletePost(post);
                        break;
                    default:
                        //no default needed
                }
                
        }
	
        private static void deletePost(Post post)
        {
            final String YES = "Delete the post";
            
            print("");
            print("");
            print("Are you sure you want to delete your post?");
            String choice = inputs.chooseOption(new ArrayList<>(Arrays.asList(YES)), "Press 0 to cancel.");
            if (choice == null)
                return;
            
            database.deletePost(post);
            
        } 
        
	private static void createThread()
	{
		if (!loginCheck())
			return;
		
		User author = currentUser;
		String topic = inputs.getThreadTopic();
		String content = inputs.getPostContent(POST_MAX_LEN);
		database.createThread(author, topic, content);
	}

}
