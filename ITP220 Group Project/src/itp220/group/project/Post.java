package itp220.group.project;

//PK's version

import java.time.*;
import java.time.format.DateTimeFormatter;

// I don't think I added anything (except maybe some constructors).
// Some method names might be different, but the spirit is the same,
// I believe. --PK

public class Post implements Comparable<Post>
{
	protected User author;
	protected String content;
	protected Rating rating;
	protected LocalDateTime timeOfCreation;
	protected final int DEFAULT_CONTENT_WIDTH = 40;
	
	public Post()
	{
		rating = new Rating();
		timeOfCreation = LocalDateTime.now();
	}
	
	public Post(User author, String content)
	{
		this.author = author;
		this.content = content;
		rating = new Rating();
		timeOfCreation = LocalDateTime.now();
	}
	
	public Post(User author, String content, Rating rating)
	{
		this.author = author;
		this.content = content;
		this.rating = rating;
		timeOfCreation = LocalDateTime.now();
	}
	
	public Post(User author, String content, Rating rating, LocalDateTime timeOfCreation)
	{
		this.author = author;
		this.content = content;
		this.rating = rating;
		this.timeOfCreation = timeOfCreation;
	}
	
        @Override
	public String toString()
	{
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MMM d, uuuu 'at' h:mm:ss a");
		return String.format("Posted by %s on %s%n%n\t%s%n%n%s%n%s",
				author.getName(),
				timeFormat.format(timeOfCreation),
				narrowContent(),
				rating.toString(),
				"........................");
	}
	
	public String narrowContent(int width)
	{
		// This is the most complicated looking thing, but it basically keeps 
		// the text in a block format. Long posts look nicer that way. The visual
		// result is much easier to understand than this algorithm 
		// and you don't need to understand this algorithm to understand
		// the program.
		
		StringBuilder sb = new StringBuilder(content);
		int i = width;
		
		// we're going to look every nth character (where n = width) and then
		// look backwards to find the last space that occurred in the string
		// and replace it with a new line instead of just a space. This makes 
		// sure that the block of text is at most n characters wide.
		
		while (i < sb.length() - 1)
		{
			// we're going to set j = i and work backwards.
			int j = i;
			
			// while j is > 0 and j isn't a space and isn't further from
			// i than the specified width...
			while (j > 0 && sb.charAt(j) != ' ' && j > i - width)
			{
				j--;
			}
			
			// if you find a space, replace it with a newline and a tab.
			// otherwise, that means there isn't a space for more than 
			// n characters, so we'll need to break a word in half.
			// Insert a newline and tab where you started the this loop
			// of the search, i. 
			if (sb.charAt(j) != ' ')
				sb.insert(i,  "\n\t");
			else
				sb.replace(j,  j+1,  "\n\t");
			
			// go to the next nth location and repeat...
			i += width;
		}
		
		return sb.toString();
			
	}
	
	public String narrowContent()
	{
		return narrowContent(DEFAULT_CONTENT_WIDTH);
	}
	
	public int compareRatings(Post other)
	{
		// the rating ought to do the comparing of
		// ratings. Delegate it.
		return rating.compareTo(other.rating);
	}
	
	public int compareTo(Post other)
	{
		return timeOfCreation.compareTo(other.timeOfCreation);
	}
	
	public void upVote(User user)
	{
		rating.upVote(user);
	}
	
	public void downVote(User user)
	{
		rating.downVote(user);
	}
	
	public boolean isAuthoredBy(User author)
	{
		return this.author.equals(author);
	}
	
	public boolean hasText(String text)
	{
		String cont = content.toLowerCase();
		if (cont.contains(text.toLowerCase()))
			return true;
		
		return false;
	}
}
