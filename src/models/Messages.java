package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import constants.AppConstants;

public class Messages {
	private String ID;
	private String Sender;
	private String Channel;
	private String Message;
	private Timestamp Date_Time;
	private String Parent;
	private String Photo;

	
	public Messages(String ID,String Sender, String Channel, String Message, Timestamp Date_Time, String Parent){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Date_Time=Date_Time;
		this.Parent=Parent;
	}
	
	public Messages(String ID,String Sender, String Channel, String Message, String Parent,String Photo){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Parent=Parent;
		this.Photo=Photo;
	}
	
	public Messages(String ID,String Sender, String Channel, String Message, String Parent){
		this.ID=ID;
		this.Sender=Sender;
		this.Channel=Channel;
		this.Message=Message;
		this.Parent=Parent;
	}
	
	


	
	public Messages(String iD, String sender, String channel, String message, Timestamp Date_Time, String parent,
			String photo) {
		this.ID=iD;
		this.Sender=sender;
		this.Channel=channel;
		this.Message=message;
		this.Date_Time=Date_Time;
		this.Parent=parent;	
		this.Photo=photo;
		}

	public String getPhoto() {
		return Photo;
	}



	public void setPhoto(String photo) {
		Photo = photo;
	}



	public String getID() {
		return ID;
	}


	public void setID(String iD) {
		ID = iD;
	}


	public String getSender() {
		return Sender;
	}


	public void setSender(String sender) {
		Sender = sender;
	}


	public String getChannel() {
		return Channel;
	}


	public void setChannel(String channel) {
		Channel = channel;
	}


	public String getMessage() {
		return Message;
	}


	public void setMessage(String message) {
		Message = message;
	}


	public Timestamp getDate_Time() {
		return Date_Time;
	}


	public void setDate_Time(Timestamp date_Time) {
		Date_Time = date_Time;
	}


	public String getParent() {
		return Parent;
	}


	public void setParent(String parent) {
		Parent = parent;
	}
	
	public static void storemsg(ServletContext c) {
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

			PreparedStatement stmt,pstmt;//,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6;
			
			try {
				
				Timestamp x=new Timestamp(System.currentTimeMillis());
				stmt = conn.prepareStatement("INSERT INTO MESSAGES(SENDER, CHANNEL, MESSAGE, DATE_TIME,PARENT)"
						+ "VALUES(?,?,?,?,?)");
				stmt.setString(1,"jony");
				stmt.setString(2, "channel1");
				stmt.setString(3, "hello");
				stmt.setTimestamp(4, x);
				stmt.setString(5, "0");
				stmt.executeUpdate();
				
				
				stmt = conn.prepareStatement("INSERT INTO MESSAGES(SENDER, CHANNEL, MESSAGE, DATE_TIME,PARENT)"
						+ "VALUES(?,?,?,?,?)");
				stmt.setString(1,"khater");
				stmt.setString(2, "channel1");
				stmt.setString(3, "hiiiiii");
				stmt.setTimestamp(4, x);
				stmt.setString(5, "0");
				
				
				//execute query commit changes and close insert statement
				stmt.executeUpdate();
				conn.commit();
				stmt.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
	}
	
	public static void storemsg1(ServletContext c) {
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();

			PreparedStatement stmt,pstmt;//,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6;
			
			try {
				
				Timestamp x=new Timestamp(System.currentTimeMillis());
				stmt = conn.prepareStatement("INSERT INTO PRIVATEMESSAGES(PRIVATECHAT, SENDER, MESSAGE, DATE_TIME, PARENT)"
						+ "VALUES(?,?,?,?,?)");
				stmt.setInt(1,1);
				stmt.setString(2, "jony");
				stmt.setString(3, "hello");
				stmt.setTimestamp(4, x);
				stmt.setString(5, "0");
				stmt.executeUpdate();
				
				
				stmt = conn.prepareStatement("INSERT INTO PRIVATEMESSAGES(PRIVATECHAT, SENDER, MESSAGE, DATE_TIME, PARENT)"
						+ "VALUES(?,?,?,?,?)");
				stmt.setInt(1,1);
				stmt.setString(2, "khater");
				stmt.setString(3, "hiiiiii");
				stmt.setTimestamp(4, x);
				stmt.setString(5, "0");
				
				
				//execute query commit changes and close insert statement
				stmt.executeUpdate();
				conn.commit();
				stmt.close();
				
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
	}
	
	
}
