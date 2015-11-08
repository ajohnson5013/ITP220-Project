package itp220.group.projectDBEnabled;

import itp220.group.project.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

/**
 
 * Use the connect method in GetJDBCInfo to get a connection to a specific database
 * Given the database, it gets the url and driver.
 * 
 * If the URL fits the specified driver, it will then load the driver and
 * get a connection.
 */

import java.util.*;
public class JDBCConnection {

	
  
  public static final int SQLSERVER = 1;
  public static final int MYSQLLOCAL = 2;
  public static final int MYSQLREMOTE=3;
  public static final int ACCESS=4;
  public static final int SQLSERVERLOCAL = 5;
  public static final int ORACLELOCAL = 6;
  public static final int UNKNOWN = -1;
  

/**
 * GetJDBCInfo constructor comment.
 */
public JDBCConnection() {
	super();
}

public static int convertName(String name)
{
	int which=0;
	if (name.equalsIgnoreCase("sqlserver"))
		which =1;
	else if (name.equalsIgnoreCase("mysqllocal"))
		which =2;
	else if (name.equalsIgnoreCase("mysqlremote"))
		which = 3;
	else if (name.equalsIgnoreCase("access"))
		which = 4;
    else if (name.equalsIgnoreCase("sqlserverlocal"))
		which = 5;
    else if (name.equalsIgnoreCase("oraclelocal"))
		which = 6;
	
	return which;
}	
			
		// use this version for databases that are secured with a username and password
public static Connection connect()
{
	Scanner scan = new Scanner(System.in);
	Connection connection=null;
	System.out.println("Which database?\n (1) SQLServer2008 at VWCC \n (2) MySQL (local) \n (3) MySQL at VWCC" +
            " \n (4) Access \n (5) SQLServer Express local \n (6) Oracle local ");
	int which = scan.nextInt();
	scan.nextLine();
	System.out.println("Username:");
	String uid = scan.nextLine();
	System.out.println("Password:");
	String pass = scan.nextLine();
	

	String driver = getDriver(which);
	String url = getURL(which);
	System.out.println(driver);
	System.out.println(url);
	try 
	{ // load the driver 
	  Class.forName(driver).newInstance();
	  System.out.println("Known drivers that are registered:");
	  Enumeration enumer = DriverManager.getDrivers();
	  while (enumer.hasMoreElements())
		  System.out.println(enumer.nextElement());	
	}
	catch( Exception e ) 
	{ // problem loading driver, class not exist?
	  e.printStackTrace();
	  
	}
	try 
	{
	  connection = DriverManager.getConnection(url, uid, pass);
	  System.out.println("Connection successful!");
	  
	}
	catch( SQLException e ) 
	{
	  e.printStackTrace();
	}
	return connection;
}

// use this version for databases that do not need a username or password
public static Connection connect(int which)
{
	Connection connection=null;
	String driver = getDriver(which);
	String url = getURL(which);
	System.out.println(driver);
	System.out.println(url);
	try 
	{ // load the driver 
	  Class.forName(driver).newInstance();
	  System.out.println("Known drivers that are registered:");
	  Enumeration enumer = DriverManager.getDrivers();
	  while (enumer.hasMoreElements())
		  System.out.println(enumer.nextElement());	
	}
	catch( Exception e ) 
	{ // problem loading driver, class not exist?
	  e.printStackTrace();
	  
	}
	try 
	{
	  connection = DriverManager.getConnection(url, "", "");
	  System.out.println("Connection successful!");
	  
	}
	catch( SQLException e ) 
	{
	  e.printStackTrace();
	}
	return connection;
}

public static Connection connect(int which, String user, String pass)
{
	Connection connection=null;
	String driver = getDriver(which);
	String url = getURL(which);
	System.out.println(driver);
	System.out.println(url);
	try 
	{ // load the driver 
	  Class.forName(driver).newInstance();
	  System.out.println("Known drivers that are registered:");
	  Enumeration enumer = DriverManager.getDrivers();
	  while (enumer.hasMoreElements())
		  System.out.println(enumer.nextElement());	
	}
	catch( Exception e ) 
	{ // problem loading driver, class not exist?
	  e.printStackTrace();
	  
	}
	try 
	{
	  connection = DriverManager.getConnection(url, user, pass);
	  System.out.println("Connection successful!");
	  
	}
	catch( SQLException e ) 
	{
	  e.printStackTrace();
	}
	return connection;
}
public  static String getDriver(int num) {
	 if (num==SQLSERVER)
		return	"com.microsoft.sqlserver.jdbc.SQLServerDriver";
	else if (num==ACCESS)
		return  "sun.jdbc.odbc.JdbcOdbcDriver";	
	else if (num==MYSQLLOCAL)
		return "com.mysql.jdbc.Driver";
	else if (num==MYSQLREMOTE)
		return "com.mysql.jdbc.Driver";
     else if (num==SQLSERVERLOCAL)
		return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
     else if (num==ORACLELOCAL)
		return "oracle.jdbc.driver.OracleDriver";
	else	
		return "error";	
}

 public static String getURL(int num) {
	Scanner scan = new Scanner(System.in);
	
	if (num==SQLSERVER)
	{
		System.out.println("What is the name of the database?(Type default if you want to use your default one)");
		String name= scan.nextLine();
		if (name.equals("default"))
			return 	"jdbc:sqlserver://164.106.3.23:9012";
		else
			return 	"jdbc:sqlserver://164.106.3.23:9012" +";databaseName=" + name;
	// change this to match your ODBC connection name
	}
	else if (num==ACCESS)
	{
		 System.out.println("What is the name of the ODBC connection?");
		 String name= scan.nextLine();
		return "jdbc:odbc:" + name;
	}	
	else if (num==MYSQLLOCAL)
	{
		System.out.println("What is the name of the database?");
		String name= scan.nextLine();
		return "jdbc:mysql://localhost:3306/"+name;
	}
	else if (num==MYSQLREMOTE)
	{
		System.out.println("What is the name of the database?");
		String name= scan.nextLine();
		return "jdbc:mysql://164.106.3.22:3098/"+name;
	}
    else if (num==ORACLELOCAL)
	{
		System.out.println("What is the name of the database?");
		String name= scan.nextLine();
		return "jdbc:oracle:thin:@localhost:1521:"+name;
	}
	else  	
		return "error";	
}


}
