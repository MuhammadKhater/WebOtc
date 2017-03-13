package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import constants.AppConstants;
import models.LoginResponse;
import models.Messages;
import models.PrivateChannel;
import models.PublicChannel;
import models.Subscription;
import models.Users;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Gson gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		Users u = gson.fromJson(jsonFileContent.toString(),Users.class);
		ServletContext c=getServletContext();
		//AppConstants.generalcntx=c;
		/////////////////////////////////////////////////
		//Messages.storemsg(c);
//		Messages.storemsg1(c);
		////////////////////////////////////////////////
		boolean Login =u.LoginUser(u,c);
		try {
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
            if(Login){
            	Users user=u.GetUser(u, c);
    			ArrayList<PublicChannel> PublicChannels=PublicChannel.getAllChanels(user.getNickName(),c);
    			ArrayList<PrivateChannel> PrivateChannels=PrivateChannel.getAllChanels(user.getNickName(),c);
    			//System.out.println("outside1: "+PrivateChannels.get(0).getNickName());
            	LoginResponse tempres= new LoginResponse(Login, user, PublicChannels,PrivateChannels);
            	out.println(gson.toJson(tempres, LoginResponse.class));
            	out.close();
            }
            else{
            	LoginResponse tempres= new LoginResponse(Login, null, null,null);
            	out.println(gson.toJson(tempres, LoginResponse.class));
            	out.close();
            	
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }
	}

}
