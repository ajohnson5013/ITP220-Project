package messageboaredv2.pkg1;

//PK's version

import java.util.*;

// I think I didn't explain what I wanted the Collection<User> users
// to be. Basically, any given User ought to only be able to rate a 
// post once. When they do, they're added to this post's users collection.
// Before a rating can be committed, it makes sure that the user trying to
// submit a rating hasn't already done so. --PK

public class Rating implements Comparable<Rating>
{
	private int upVotes;
	private int downVotes;
	private HashSet<User> usersAlreadyVoted;
	
	public Rating()
	{
		usersAlreadyVoted = new HashSet<User>();
	}
	
	public Rating(int upVotes, int downVotes)
	{
		this.upVotes = upVotes;
		this.downVotes = downVotes;
	}
	
	
	public String toString()
	{
		return String.format("Thumbs up: %d  Thumbs down: %d",  upVotes, downVotes);
	}
	
	public void upVote(User user)
	{
		if (userHasAlreadyVoted(user))
			return;
		
		upVotes++;
	}
	
	public void downVote(User user)
	{
		if (userHasAlreadyVoted(user))
			return;
		
		downVotes++;
	}
	
	private boolean userHasAlreadyVoted(User user)
	{
            // This method is what I'm talking about in my comment
            // up top. If the collection has the user already,
            // they've already voted. If not, you need to add them
            // and let the program know that they *weren't already* 
            // in...and therefore you're good to go! --PK
            
		if (usersAlreadyVoted.contains(user))
			return true;
		
		usersAlreadyVoted.add(user);
		return false;
	}
	
	private int score()
	{
            // I don't really know any better way to do this.
            // You could do something with the ratio of good votes to 
            // bad votes, but in general I think this is more accurate. 
            // Plus, you don't want somthing with 100 good votes and 50 bad
            // votes to be ranked lower than something with only 1 good vote and 
            // nothing else, hahaha. --PK
            
            
		return upVotes - downVotes;
	}
	
	public int compareTo(Rating other)
	{
		return other.score() - score();
	}
}
