package messageboaredv2.pkg1;

//PK's version

import java.time.LocalDateTime;
import java.util.*;

// I thought about it more, and a thread really ought to be able to be rated
// just like a post, so I went ahead and made it inherit from Post since
// it shares soooo much. --PK

public class Thread extends Post
{
	private String topic;
	private ArrayList<Post> replies;
	
	public Thread()
	{
		super();
		replies = new ArrayList<>();
	}
	
	public Thread(User author, String topic, String content)
	{
		super(author, content);
		this.topic = topic;
		replies = new ArrayList<>();
	}
	
	public Thread(User author, String topic, String content, Rating rating)
	{
		super(author, content, rating);
		this.topic = topic;
		replies = new ArrayList<>();
	}
	
	public Thread(User author, String topic, String content, Rating rating, LocalDateTime timeOfCreation)
	{
		super(author, content, rating, timeOfCreation);
		this.topic = topic;
		replies = new ArrayList<>();
	}
	
	public String getTopic()
	{
		return topic;
	}
	
	public String toString()
	{
		return topic + " by " + author.getName();
	}
	
	public void sortRepliesByRating()
	{
		replies.sort(new PostRatingsComparator());
	}
	
	public void sortRepliesByDate()
	{
		Collections.sort(replies);
	}
	
	public ArrayList<Post> getReplies()
	{
		return replies;
	}
	
	public void reply(User author, String content)
	{
		replies.add(new Post(author, content));
	}
	
	public ArrayList<Post> getAllPosts()
	{
		ArrayList<Post> allPosts = new ArrayList<>();
		allPosts.add(this.asPost()); //adds a copy of itself as a Post
		if (!replies.isEmpty())
			allPosts.addAll(replies);
		return allPosts;
	}
	
	public Post asPost()
	{
            // This is so you can easily get a copy of this as a Post, 
            // mostly used to access the Post.toString() instead of the 
            // Thread.toString() --PK
            
		return new Post(author, content, rating, timeOfCreation);
	}
	
	@Override
	public boolean hasText(String text)
	{
		for (Post post : getAllPosts())
			if (post.hasText(text))
				return true;
		
		return false;
	}
	
}
