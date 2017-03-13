package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import constants.AppConstants;
import models.LoginResponse;
import models.Messages;
import models.PrivateChannel;
import models.Users;



@ServerEndpoint("/chat/{username}")
public class ChatEndPoint{
	
	//tracks all active chat users
    private static Map<Session,String> chatUsers = Collections.synchronizedMap(new HashMap<Session,String>()); 
    
    /**
     * Joins a new client to the chat
     * @param session 
     * 			client end point session
     * @throws IOException
     */
    @OnOpen
    public void joinChat(Session session, @PathParam("username") String username) throws IOException{
    	if (session.isOpen()) {
			//add new client to managed chat sessions
			chatUsers.put(session,username);
			//notify everyone that a new client has join the chat
			//doNotify(null,"User <span class='username'>"+username+"</span> has joined the chat...", null);
			//welcome the new client
//    			session.getBasicRemote().sendText("Welcome <span class='username'>"+username +
//        			                          "</span>. There are currently "+chatUsers.size()+" participants in this chat.");
		}
    }

    /**
     * Message delivery between chat participants
     * @param session
     * 			client end point session
     * @param msg
     * 			message to deliver		
     * @throws IOException
     */
    @OnMessage
    public void deliverChatMessege(Session session, String msg) throws IOException{
        try {
            if (session.isOpen()) {
               //deliver message
               String user = chatUsers.get(session);
               doNotify(user, msg, null);
            }
        } catch (IOException e) {
                session.close();
        }
    }
    
    /**
     * Removes a client from the chat
     * @param session
     * 			client end point session
     * @throws IOException
     */
    @OnClose
    public void leaveChat(Session session) throws IOException{
    	String user = chatUsers.remove(session);//fake user just for removal
		//let other participants know that client has left the chat
		//doNotify(null,"User <span class='username'>"+user+"</span> has left the chat...",session);
    }

    /*
     * Helper method for message delivery to chat participants. skip parameter is used to avoid delivering a message 
     * to a certain client (e.g., one that has just left) 
     */
    private void doNotify(String author, String message, Session skip) throws IOException{
    	Gson gson = new GsonBuilder().create();
    	JsonParser parser = new JsonParser();
    	JsonElement obj = parser.parse(message);
    	System.out.println(obj.toString());
    	Messages temp = gson.fromJson(obj.toString(),Messages.class);
    	String kind=temp.getID();
    	AddToDB(temp);
    	temp= GetMsg(temp);
    	message= gson.toJson(temp, Messages.class);
    	String comment=GetComment(temp.getMessage());
    	if(kind.equals("Public")){
    		ArrayList<String> NickName= GetNickName(temp.getChannel());
    		AddUnreadPublic(NickName,temp.getChannel(),temp.getSender(),comment);
    		for (Entry<Session,String> user : chatUsers.entrySet()){
        		Session session = user.getKey();
        		if (!session.equals(skip) && session.isOpen()){
        			for(int i=0;i<NickName.size();i++){
        				
        				if((chatUsers.get(session)).equals(NickName.get(i)) ){
            				
                			session.getBasicRemote().sendText(message);

            			}
        				
        			}
        			
        		}
        	}
    	}
    	else if(kind.equals("Private")){
    		Messages temp2= getSecondMessage(temp);
    		String message2= gson.toJson(temp2, Messages.class);
    		String NickName= temp.getChannel();
    		String sender= temp.getSender();
    		AddUnreadPrivate(NickName,temp.getSender(), comment);
    		for (Entry<Session,String> user : chatUsers.entrySet()){
        		Session session = user.getKey();
        		if (!session.equals(skip) && session.isOpen()){
        			if((chatUsers.get(session)).equals(NickName) ){
            			session.getBasicRemote().sendText(message2);

        			}
        			if((chatUsers.get(session)).equals(sender)){
        				session.getBasicRemote().sendText(message);
        			}
        			
        		}
        	}
    	}
    	
    }

    
    
    public static void SendPrivateChannel(String jsonChannel,String NickName) throws IOException{
    	
    	for (Entry<Session,String> user : chatUsers.entrySet()){
    		Session session = user.getKey();
    		if (session.isOpen()){
    			if((chatUsers.get(session)).equals(NickName) ){
        			session.getBasicRemote().sendText(jsonChannel);

    			}
    		}
    	}
    	
    }
    

	private Messages getSecondMessage(Messages temp) {
		String ID= temp.getID();
		String Channel= temp.getSender();
		String Sender=temp.getSender();
		String Message = temp.getMessage();
		String Parent = temp.getParent();
		String Photo=temp.getPhoto();
		Timestamp time = temp.getDate_Time();
		Messages message=new Messages(ID, Sender, Channel, Message, time, Parent, Photo);
		return message;
		
	}

	private String GetComment(String message) {
		String comment=null;
		String[] splited = message.split("\\s+");
		if(splited[0].charAt(0)=='@'){
			comment=splited[0].substring(1);
		}

		return comment;
	}

	private void AddUnreadPrivate(String nickName, String sender,String comment) {
		
		ServletContext c=AppConstants.generalcntx;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt,stmt;
			
			try {
				int unread=0;
				int replies=0;
				stmt = conn.prepareStatement("SELECT * FROM PRIVATE_INFO WHERE NICKNAME1=? AND NICKNAME2=?");
				stmt.setString(1, nickName);
				stmt.setString(2, sender);
				
				ResultSet rs=stmt.executeQuery();
				while(rs.next()){
					unread=rs.getInt("UNREAD");
					replies=rs.getInt("UNREAD");
				}
				
				//release rs object close select statement and close connection with DB
				stmt.close();
				
				unread++;
				replies++;
				
				if(nickName.equals(comment)){
					pstmt = conn.prepareStatement("UPDATE PRIVATE_INFO SET UNREAD=?, REPLIES=? WHERE NICKNAME1=? AND NICKNAME2=?");
					pstmt.setInt(1, unread);
					pstmt.setInt(2, replies);
					pstmt.setString(3, nickName);
					pstmt.setString(4, sender);
					pstmt.executeUpdate();
					pstmt.close();
					
					
				}
				
				else{
					pstmt = conn.prepareStatement("UPDATE PRIVATE_INFO SET UNREAD=? WHERE NICKNAME1=? AND NICKNAME2=?");
					pstmt.setInt(1, unread);
					pstmt.setString(2, nickName);
					pstmt.setString(3, sender);
					
					pstmt.executeUpdate();
					pstmt.close();
									
					
				}
					
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			conn.close();

			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage()); // print error to error stream
    	}
    	catch(NamingException e)
    	{
    		System.err.println(e.getMessage()); // print error to error stream
    	}	
		

		
		
	}

	private void AddUnreadPublic(ArrayList<String> nickName, String channel, String sender, String comment) {
		
		ServletContext c=AppConstants.generalcntx;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt,stmt;
			
			for(int i=0;i<nickName.size();i++){
				if(nickName.get(i)!=sender){
					try {
						int unread=0;
						int replies=0;
						stmt = conn.prepareStatement("SELECT * FROM PUBLIC_INFO WHERE NICKNAME=? AND CHANNEL=?");
						stmt.setString(1, nickName.get(i));
						stmt.setString(2, channel);
						
						ResultSet rs=stmt.executeQuery();
						while(rs.next()){
							unread=rs.getInt("UNREAD");
							replies=rs.getInt("UNREAD");
						}
						
						//release rs object close select statement and close connection with DB
						stmt.close();
						
						unread++;
						replies++;
						
						if(nickName.get(i).equals(comment)){
							pstmt = conn.prepareStatement("UPDATE PUBLIC_INFO SET UNREAD=?, REPLIES=? WHERE NICKNAME=? AND CHANNEL=?");
							pstmt.setInt(1, unread);
							pstmt.setInt(2, replies);
							pstmt.setString(3, nickName.get(i));
							pstmt.setString(4, channel);
							pstmt.executeUpdate();
							pstmt.close();
							
							
						}
						
						else{
							pstmt = conn.prepareStatement("UPDATE PUBLIC_INFO SET UNREAD=? WHERE NICKNAME=? AND CHANNEL=?");
							pstmt.setInt(1, unread);
							pstmt.setString(2, nickName.get(i));
							pstmt.setString(3, channel);
							
							pstmt.executeUpdate();
							pstmt.close();
											
							
						}
							
					}
					catch(SQLException e)
					{
						System.err.println(e.getMessage()); // print error to error stream
					}
					
				}
				
			}
			conn.close();

			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage()); // print error to error stream
    	}
    	catch(NamingException e)
    	{
    		System.err.println(e.getMessage()); // print error to error stream
    	}	
		

		
		
	}

	
	private ArrayList<String> GetNickName(String channel) {
		
		ArrayList<String> NickName= new ArrayList<String>();
		
		ServletContext c=AppConstants.generalcntx;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
				stmt = conn.prepareStatement("SELECT * FROM SUBSCRIPTIONS "+ "WHERE CHANNEL=? ");
				stmt.setString(1,channel);
				

				ResultSet rs = stmt.executeQuery();

				while(rs.next()){
					
					NickName.add(rs.getString("USERNAME"));
					
				}
				conn.commit();
				stmt.close();
									
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			conn.close();
			
		}
		catch (SQLException e) {
			System.err.println(e.getMessage()); // print error to error stream
    	}
    	catch(NamingException e)
    	{
    		System.err.println(e.getMessage()); // print error to error stream
    	}	
		
		
		
		
		
		return NickName;
	}

	private Messages GetMsg(Messages temp) {
		
		
		Messages Message=temp;
		String Id="";
		String Photo="";
		
		ServletContext c=AppConstants.generalcntx;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt, stmt1;
			
			String kind = temp.getID();
			
			if(kind.equals("Public")){
				try {
					
					stmt = conn.prepareStatement("SELECT * FROM MESSAGES "+ "WHERE SENDER=? AND CHANNEL=? AND MESSAGE=? AND DATE_TIME=? AND PARENT=?");
					stmt.setString(1,temp.getSender());
					stmt.setString(2, temp.getChannel());
					stmt.setString(3, temp.getMessage());
					stmt.setTimestamp(4, temp.getDate_Time());
					stmt.setString(5, temp.getParent());

					ResultSet rs = stmt.executeQuery();

					while(rs.next()){
						Id=Integer.toString(rs.getInt("ID"));
					}
					Message.setID(Id);
					stmt.close();
					
					
					stmt1 = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE NICKNAME=?");
					stmt1.setString(1,temp.getSender());


					ResultSet rs1 = stmt1.executeQuery();

					while(rs1.next()){
						//Id=Integer.toString(rs1.getInt("ID"));
						Photo=rs1.getString("PHOTO");
					}
					Message.setPhoto(Photo);
					stmt1.close();
										
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
				}		
			
			else if(kind.equals("Private")){
				try {
					int ChatId=GetPrivateChatId(temp.getSender(),temp.getChannel());
					
					stmt = conn.prepareStatement("SELECT * FROM PRIVATEMESSAGES "+ "WHERE PRIVATECHAT=? AND SENDER=? AND MESSAGE=? AND DATE_TIME=? AND PARENT=?");
					stmt.setInt(1,ChatId);
					stmt.setString(2, temp.getSender() );
					stmt.setString(3, temp.getMessage());
					stmt.setTimestamp(4, temp.getDate_Time());
					stmt.setString(5, temp.getParent());

					ResultSet rs = stmt.executeQuery();

					while(rs.next()){
						Id=Integer.toString(rs.getInt("ID"));
					}
					Message.setID(Id);
					stmt.close();
					
					
					stmt1 = conn.prepareStatement("SELECT * FROM USERS "+ "WHERE NICKNAME=?");
					stmt1.setString(1,temp.getSender());


					ResultSet rs1 = stmt1.executeQuery();

					while(rs1.next()){
						//Id=Integer.toString(rs1.getInt("ID"));
						Photo=rs1.getString("PHOTO");
					}
					Message.setPhoto(Photo);
					stmt1.close();
					
					
					
				}
				catch(SQLException e)
				{
					System.err.println(e.getMessage()); // print error to error stream
				}
				
				}		
			conn.close();
			
			}

			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		
		return Message;
	}

	private void AddToDB(Messages temp) {

		ServletContext c=AppConstants.generalcntx;
		Timestamp x=new Timestamp(System.currentTimeMillis());
		temp.setDate_Time(x);
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			String kind = temp.getID();
			
			if(kind.equals("Public")){
				try {
					
					
					stmt = conn.prepareStatement("INSERT INTO MESSAGES(SENDER, CHANNEL, MESSAGE, DATE_TIME, PARENT)"
							+ "VALUES(?,?,?,?,?)");
					stmt.setString(1,temp.getSender());
					stmt.setString(2, temp.getChannel());
					stmt.setString(3, temp.getMessage());
					stmt.setTimestamp(4, temp.getDate_Time());
					stmt.setString(5, temp.getParent());

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
			
			else if(kind.equals("Private")){
				try {
					int ChatId=GetPrivateChatId(temp.getSender(),temp.getChannel());
					
					stmt = conn.prepareStatement("INSERT INTO PRIVATEMESSAGES(PRIVATECHAT, SENDER, MESSAGE, DATE_TIME, PARENT)"
							+ "VALUES(?,?,?,?,?)");
					stmt.setInt(1,ChatId);
					stmt.setString(2, temp.getSender() );
					stmt.setString(3, temp.getMessage());
					stmt.setTimestamp(4, temp.getDate_Time());
					stmt.setString(5, temp.getParent());

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
			conn.close();
			
			}

			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
		
	}

	private int GetPrivateChatId(String user1, String user2) {
		int ret=0;
		
		ServletContext c=AppConstants.generalcntx;
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			BasicDataSource ds = (BasicDataSource)context.lookup(
					c.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
			Connection conn = ds.getConnection();
			
			ResultSet rs;
			try {
				
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? OR USER2=? ");
				pstmt.setString(1,user1);
				pstmt.setString(2,user2);
				
				rs = pstmt.executeQuery();
				
				// loop over ResultSet 
				while(rs.next()){
					ret=rs.getInt("ID");
				}
				//release rs object close select statement and close connection with DB
				rs.close();
				pstmt.close();
				
				if(ret==0){
					PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM PRIVATECHAT "+ "WHERE USER1=? OR USER2=? ");
					pstmt1.setString(1,user2);
					pstmt1.setString(2,user1);
					
					ResultSet rs1 = pstmt1.executeQuery();
					
					// loop over ResultSet 
					while(rs1.next()){
						ret=rs1.getInt("ID");
					}
					//release rs object close select statement and close connection with DB
					rs1.close();
					pstmt1.close();

				}
				
				
				conn.close();
				
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

		return ret;
	
	
	}

	

}
