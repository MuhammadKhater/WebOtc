package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import constants.AppConstants;
import models.LoginResponse;
import models.PublicChannel;
import models.SearchResponse;
import models.Users;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchChannelServlet")
public class SearchChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchChannelServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ArrayList<PublicChannel> ChannelNames = new ArrayList<PublicChannel>();
		Gson gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		String channel = gson.fromJson(jsonFileContent.toString(),String.class);
		ServletContext c=getServletContext();
		ChannelNames=PublicChannel.GetChannelSearchByChannel(c, channel);
		boolean result=false;
		if(!(ChannelNames.isEmpty())){
			result=true;
		}
			
		SearchResponse ret =new SearchResponse(result, ChannelNames);
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	out.println(gson.toJson(ret, SearchResponse.class));
        	out.close();
           
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }


	}

}
