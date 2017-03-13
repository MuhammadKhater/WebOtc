package constants;

import java.lang.reflect.Type;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.reflect.TypeToken;
import models.*;
 

/**
 * A simple place to hold global application constants
 */
public class AppConstants {
	
	
	//derby constants
	public static ServletContext generalcntx;
	public final String DB_NAME = "DB_NAME";
	public final static String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final static String OPEN = "Open";
	public final static String SHUTDOWN = "Shutdown";
	
	//sql statements
	public final String CREATE_USERS_TABLE = "CREATE TABLE USERS ("
			+ 		"USERNAME 		VARCHAR(10) NOT NULL PRIMARY KEY,"
			+ 		"PASSWORD 		VARCHAR(8) NOT NULL,"
			+ 		"NICKNAME 		VARCHAR(20) UNIQUE,"
			+		"DESCRIPTION 	VARCHAR(50),"
			+ 		"PHOTO 			VARCHAR(150),"
			+ 		"STATUS 		VARCHAR(6) NOT NULL,"
			+ 		"LAST_SEEN 		TIMESTAMP NOT NULL"
			+   ")";
	
	public final String CREATE_CHANELS_TABLE = "CREATE TABLE CHANELS ("
 			+ 		"NAME 			VARCHAR(30) PRIMARY KEY,"
 			+ 		"DESCRIPTION 	VARCHAR(500),"
 			+ 		"CREATED_BY 	VARCHAR(10) NOT NULL,"
 			+ 	")";
	
	public final String CREATE_MESSAGES_TABLE = "CREATE TABLE MESSAGES ("
			+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
			+ 		"SENDER 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
			+ 		"CHANEL_NAME 	VARCHAR(30) NOT NULL,"
			+ 		"TEXT 			VARCHAR(500) NOT NULL,"
			+ 		"DATE_TIME 		TIMESTAMP NOT NULL"
			+ 		"PARENT 		INTEGER DEFAULT 0,"

			+ 	")";
	
	public final String CREATE_SUBSCRIBERS_TABLE = "CREATE TABLE SUBSCRIPTIONS ("
			+ 		"ID 			INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,"
			+ 		"NICKNAME 		VARCHAR(20) NOT NULL REFERENCES USERS(NICKNAME) ON DELETE CASCADE,"
			+ 		"CHANNEL 		VARCHAR(30) NOT NULL REFERENCES CHANNELS(NAME) ON DELETE CASCADE"
 			+ 	")";
	

	/*
	public final String INSERT_CUSTOMER_STMT = "INSERT INTO CUSTOMER VALUES(?,?,?)";
	public final String SELECT_ALL_CUSTOMERS_STMT = "SELECT * FROM CUSTOMER";
	public final String SELECT_CUSTOMER_BY_NAME_STMT = "SELECT * FROM CUSTOMER "
			+ "WHERE Name=?";
			
	*/
}
