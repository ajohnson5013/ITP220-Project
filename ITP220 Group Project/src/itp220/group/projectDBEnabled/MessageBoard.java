package itp220.group.projectDBEnabled;
//PK's version
import itp220.group.project.*;
import java.util.*;


// The MessageBoard class will hold a collection of threads. It will
// be responsible for all aggregate thread-level methods, like sorting 
// threads, adding threads, etc. --PK

public class MessageBoard 
{
	private String boardName;
	private ArrayList<Thread> threads;
	
	public MessageBoard()
	{
		threads = new ArrayList<>();
	}
	
	public MessageBoard(String name)
	{
		boardName = name; 
		threads = new ArrayList<>();
	}
	
	public void createThread(Thread thread)
	{
		threads.add(thread);
	}
	
	public void createThread(User author, String topic, String content)
	{
		threads.add(new Thread(author, topic, content));
	}
	
	public void sortThreadsByRating()
	{
		threads.sort(new PostDateComparator());
	}
	
	public void sortThreadsByDate()
	{
		// Threads are compared by their time of creation by default --PK
		Collections.sort(threads);
	}
	
        public void sortThreadsByTopic()
        {
            threads.sort(new ThreadTopicComparator());
        }
        
        @Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder(boardName);
		sb.append("\n================================");
		for (Thread thread : threads)
		{
			sb.append("\n");
			sb.append(thread.toString());
		}
		
		return sb.toString();
	}
	
	public ArrayList<Thread> getThreads()
	{
		return threads;
	}
	
	public ArrayList<Thread> getThreadsWithText(String text)
	{
		ArrayList<Thread> results = new ArrayList<>();
		
		for (Thread thread : threads)
			if (thread.hasText(text))
				results.add(thread);
		
		return results;
	}
	
        public void deletePost(Post post)
        {
            for (Thread thread : threads)
                if (thread.delete(post))
                    return;
        }
        
	public ArrayList<Thread> getThreadsByAuthor(User author)
	{
		ArrayList<Thread> results = new ArrayList<>();
		
		for (Thread thread : threads)
		{
			if (thread.isAuthoredBy(author))
				results.add(thread);
		}
		
		return results;
	}
}
