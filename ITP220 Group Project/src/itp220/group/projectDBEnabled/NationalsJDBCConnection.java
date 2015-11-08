/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itp220.group.projectDBEnabled;

/**
 *
 * @author pkkee_000
 */

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class NationalsJDBCConnection 
{
    private Connection connection;
    
    private static String FIND_USERS = "CALL findAllUsers();";
    private static String FIND_THREADS = "CALL findAllThreads();";
    private static String GET_THREAD_POSTS = "CALL findAllReplies(";
    private static String FIND_THREADS_BY_AUTHOR = "CALL searchThreadsByUser(";
    private static String FIND_THREADS_WITH_TEXT = "CALL searchThreadsByText('";
    private static String MAKE_NEW_POST = "CALL addPost(";
    private static String UP_VOTE = "CALL upVotePost(";
    private static String DOWN_VOTE = "CALL downVotePost(";
    private static String DELETE_POST = "CALL dropPost(";
    private static String START_THREAD = "CALL addThread(";
    private static String NEW_USER = "CALL addUser('";
    
    public NationalsJDBCConnection()
    {
        connection = JDBCConnection.connect();
    }
    
    //mock up of various methods so that I can program the basics
    public ArrayList<User> getUsers()
    {
        ArrayList<User> users = new ArrayList<User>();
        try
        {
            Statement command = connection.createStatement();
            ResultSet results = command.executeQuery(FIND_USERS);
            while (results.next())
            {
                users.add(makeUser(results));
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
        finally
        {
            return users;
        }
    }   
    
    public RegularUser makeUser(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String pass = rs.getString(3);
        return new RegularUser(name, pass, id);
    }
    
    public ArrayList<Thread> getThreads()
    {
        return getThreads(FIND_THREADS);
    }
    
    public ArrayList<Thread> getThreads(String threadCommand)
    {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        try
        {
            Statement command = connection.createStatement();
            ResultSet results = command.executeQuery(threadCommand);
            while (results.next())
            {
                threads.add(makeThread(results));
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
        finally
        {
            return threads;
        }
    }   
    
    public Thread makeThread(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(1);
        String topic = rs.getString(2);
        
        Date time = rs.getDate(3);
        LocalDateTime postTime = LocalDateTime.ofEpochSecond(time.getTime() / 1000, 0, ZoneOffset.ofHours(-5));
        
        int userID = rs.getInt(4);
        String userName = rs.getString(5);
        
        RegularUser user = new RegularUser(userName, userID);
        
        int upVotes = rs.getInt(6);
        int downVotes = rs.getInt(7);
        Rating rating = new Rating(upVotes, downVotes);
        
        return new Thread(id, user, topic, "", rating, postTime);
    }
    
    public void getFullThread(Thread thread)
    {
        thread.clearReplies();
        try
        {
            Statement command = connection.createStatement();
            ResultSet results = command.executeQuery(GET_THREAD_POSTS + thread.getID() + ");");
            results.next();
            thread.setContent(results.getString(2));
            thread.setID(results.getInt(1));
            thread.setRating(new Rating(results.getInt(6), results.getInt(7)));
            
            while (results.next())
            {
                Post reply = makePost(results);
                thread.addReply(reply);
            }
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
    }
    
    public Post makePost(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(1);
        String content = rs.getString(2);
        
        Date time = rs.getDate(3);
        LocalDateTime postTime = LocalDateTime.ofEpochSecond(time.getTime() / 1000, 0, ZoneOffset.ofHours(-5));
        
        int userID = rs.getInt(4);
        String userName = rs.getString(5);
        
        RegularUser user = new RegularUser(userName, userID);
        
        int upVotes = rs.getInt(6);
        int downVotes = rs.getInt(7);
        Rating rating = new Rating(upVotes, downVotes);
        
        return new Post(id, user, content, rating, postTime);
    }
    
    public ArrayList<Thread> getThreadsByAuthor(User author)
    {
        String command = FIND_THREADS_BY_AUTHOR + author.getID() + ");";
        return getThreads(command);
    }
    
    public ArrayList<Thread> getThreadsWithText(String text)
    {
        String command = FIND_THREADS_WITH_TEXT + "%" + text + "%');";
        System.out.println(command);
        return getThreads(command);
    }
    
    public void replyToThread(Thread thread, User author, String content)
    {
        //Thread has some built in methods to nicely construct a reply,
        //so let it do its thing
        thread.reply(author, content);
        
        //And now we can join all the variables together for our database command
        
        //get the new post
        ArrayList<Post> replies = thread.getReplies();
        Post newestPost = replies.get(replies.size() - 1);
        
        int threadID = thread.getID();
        int authorID = author.getID();
        
        StringBuilder sb = new StringBuilder(MAKE_NEW_POST);
        sb.append(authorID);
        sb.append(", ");
        sb.append(threadID);
        sb.append(", '");
        sb.append(newestPost.getContent());
        sb.append("', ");
        
        String dateString = makeTimeString(newestPost.getPostTime());
        
        sb.append("'");
        sb.append(dateString);
        sb.append("');");
        
        String commandStatement = sb.toString();
        
        try
        {
            Statement command = connection.createStatement();
            command.execute(commandStatement);
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
       
    }
    
    public void createThread(User author, String topic, String content)
    {
        Thread thread = new Thread(author, topic, content);
        int authorID = author.getID();
        
        StringBuilder sb = new StringBuilder(START_THREAD);
        sb.append(authorID);
        sb.append(", '");
        sb.append(topic);
        sb.append("', '");
        sb.append(content);
        sb.append("', '");
        sb.append(makeTimeString(thread.getPostTime()));
        sb.append("');");
        
        String commandStatement = sb.toString();
        try
        {
            Statement command = connection.createStatement();
            command.execute(commandStatement);
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
    }
    
    public void votePost(User user, Post post, String direction)
    {
        StringBuilder sb = new StringBuilder(direction);
        sb.append(user.getID());
        sb.append(",");
        sb.append(post.getID());
        sb.append(");");
        
        String commandStatement = sb.toString();
        try
        {
            Statement command = connection.createStatement();
            command.execute(commandStatement);
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
    }
    
    public void upVote(User user, Post post)
    {
        votePost(user, post, UP_VOTE);
    }
    
    public void downVote(User user, Post post)
    {
        votePost(user, post, DOWN_VOTE);
    }
    
    public void deletePost(Post post)
    {
        StringBuilder sb = new StringBuilder(DELETE_POST);
        sb.append(post.getID());
        sb.append(");");
        
        String commandStatement = sb.toString();
        try
        {
            Statement command = connection.createStatement();
            command.execute(commandStatement);
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
    }
    
    public String makeTimeString(LocalDateTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateString = time.format(formatter);
        return dateString;
    }
    
    public void createNewUser(User user)
    {
        StringBuilder sb = new StringBuilder(NEW_USER);
        sb.append(user.getName());
        sb.append("', '");
        sb.append(user.getPassword());
        sb.append("');");
        
        String commandStatement = sb.toString();
        try
        {
            System.out.println("Creating new user...");
            Statement command = connection.createStatement();
            ResultSet rs = command.executeQuery(commandStatement);
            rs.next();
            user.setID(rs.getInt(1));
        }
        catch(SQLException e)
        {
            System.out.println("Something went wrong with the SQL.");
            System.out.println(e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Something really weird happened.");
            System.out.println(e.getMessage());
        }
    }
}
