package itp220.group.project;

//PK's version

import java.util.*;

public class ForumDriver {
	
	private static MessageBoard board;
	private static Users users;
	private static TextInputs inputs;
	
	private static final int POST_MAX_LEN = 300; //max char length of post content
	
	public static void main(String[] args) {
		
		inputs = new TextInputs();
		board = new MessageBoard();
		users = new Users();
		
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
		users.addUser(new RegularUser("Bob"));
		users.addUser(new RegularUser("Dave"));
		users.addUser(new RegularUser("Scott"));
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
		User author = inputs.chooseUser(users.getUsers());
		ArrayList<Thread> threads = board.getThreadsByAuthor(author);
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
		ArrayList<Thread> threads = board.getThreadsWithText(text);
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
		if (!users.someoneLoggedIn())
			print("You must be logged in to do that.");
		
		return users.someoneLoggedIn();
	}
	
	private static void login()
	{
		if (users.someoneLoggedIn())
		{
			print("Already logged in as " + users.getCurrentUser().getName() + ".");
			return;
		}
		
		print("Select user:");
		User user = inputs.chooseUser(users.getUsers());
		String userName = user.getName();
		String password = inputs.getPassword();
		
		users.login(user,  password);
		
		if (!users.someoneLoggedIn())
			print("Incorrect password.");
		else
			print("Successfully logged in as " + userName + ".");
	}
	
	private static void logout()
	{
		if (users.someoneLoggedIn())
		{
			users.logout();
			print(users.getCurrentUser().getName() + " logged out.");
		}
		else
			print("No one is logged in.");
	}
	
	private static void viewThreads()
	{
		// If you call viewThreads() without argument, it 
		// assumes you want all threads from the MessageBoard.
		viewThreads(board.getThreads());
	}
	
	private static void viewThreads(ArrayList<Thread> threadList)
	{
		//Basic instructions to user
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
			board.sortThreadsByRating();
			viewThreads(threadList);
			return;
		case "d":
			board.sortThreadsByDate();
			viewThreads(threadList);
			return;
                case "t":
                        board.sortThreadsByTopic();
                        viewThreads(threadList);
                        return;
		default:
			if (selectedThread == null)
				return;
		}
		
		viewPosts(selectedThread);
	}
	
	private static void postReply(Thread thread)
	{
		if (!loginCheck())
			return;
		
		User author = users.getCurrentUser();
		String content = inputs.getPostContent(300);
		thread.reply(author, content);
	}
	
	private static void viewPosts(Thread thread)
	{
		//Basic instructions for users
		print("Enter a post's number to rate.");
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
		Post selectedPost = inputs.choosePost(thread.getAllPosts());
		
		// Clear the valid alternate inputs so you don't cause problems
		// later
		inputs.clearValidAlts();
		
		switch(inputs.getLastInput())
		{
		case "p":
			postReply(thread);
			viewPosts(thread);
			return;
		case "d":
			thread.sortRepliesByDate();
			viewPosts(thread);
			return;
		case "r":
			thread.sortRepliesByRating();
			viewPosts(thread);
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
		ratePost(selectedPost);
		viewPosts(thread);
	}
	
	public static void ratePost(Post post)
	{
		final String UP = "Upvote";
		final String DOWN = "Downvote";
		
		if (!loginCheck())
			return;
		
		String options[] = {UP, DOWN};
		ArrayList<String> optionsList = new ArrayList<>(Arrays.asList(options));
		
		String choice = inputs.chooseOption(optionsList, "Press 0 to cancel.");
		
		// If they press 0, choice == null.
		if (choice == null)
			return;
		
		User user = users.getCurrentUser();
		if (choice.equals(UP))
			post.upVote(user);
		else
			post.downVote(user);
	}
	
	private static void createThread()
	{
		if (!loginCheck())
			return;
		
		User author = users.getCurrentUser();
		String topic = inputs.getThreadTopic();
		String content = inputs.getPostContent(POST_MAX_LEN);
		board.createThread(author, topic, content);
	}

}
