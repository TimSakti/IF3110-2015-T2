package com.ws.resource;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.ws.db.DBConnection;
import com.ws.model.Token;
import com.ws.resource.TokenValidity.AccessValidity;
import com.ws.resource.TokenValidity.Identity;



@Path("/token")
public class TokenResource {
	
	public static boolean isTokenUnique(Token token){
		boolean unique = true;
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		try{
			String sql = "SELECT access_token FROM token "
					+ "WHERE access_token = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, token.access_token);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()){
				unique = false;
			}
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
	    	try{
	    		if(stmt!=null)
		            stmt.close();
			} catch(SQLException se2){
		      
			}
			try {
				if(conn!=null)
		            conn.close();
			} catch(SQLException se){
		    	se.printStackTrace();
			}
		}
		return unique;
	}
	
	public static String getRandomToken(){
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		Random rnd = new Random();

		final int length = 50;
		StringBuilder sb = new StringBuilder( 50 );
		for( int i = 0; i < 50; i++ ) 
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		String random = sb.toString();
		
		return random;
	}
	
	
	public static Token generateToken(String email, String password){
		Token token = new Token();
		//Check if email and password match
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		try{
			String sql = "SELECT * FROM user "
					+ "WHERE email = ? AND password = MD5(?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				int user_id = rs.getInt("id_user");
				token.access_token=getRandomToken();

				java.util.Date dt = new java.util.Date();
				java.text.SimpleDateFormat sdf = 
				     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(dt);
				
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(currentTime));
				c.add(Calendar.DATE, 2);
				
				dt.setTime (dt.getTime() + (2 * 24 * 3600 * 1000));//2 hari
				
				String cookieExpire = "expires=" + dt.toGMTString();
				token.expire = cookieExpire;
				System.out.println("expired on: "+token.expire);
				while(!(isTokenUnique(token))){
					token.access_token=getRandomToken();
				}
				sql = "DELETE FROM token WHERE id_user = "+user_id;
				stmt.executeUpdate(sql);
				
				sql = "INSERT INTO token(access_token,id_user,timestamp) " +
						"VALUES('"+token.access_token+"',"+user_id+",'"+currentTime+"');";
				stmt.executeUpdate(sql);
				
			}
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
	    	try{
	    		if(stmt!=null)
		            stmt.close();
			} catch(SQLException se2){
		      
			}
			try {
				if(conn!=null)
		            conn.close();
			} catch(SQLException se){
		    	se.printStackTrace();
			}
		}
		return token;
	}
	
	public static Token generateToken(String access_token){
		Token token = new Token();
		//Check if email and password match
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		try{
			String sql = "SELECT * FROM token "
					+ "WHERE access_token = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, access_token);
			System.out.println(stmt);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				
				int user_id = rs.getInt("id_user");
				
				token.access_token=getRandomToken();		
				
				java.util.Date dt = new java.util.Date();
				java.text.SimpleDateFormat sdf = 
				     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String currentTime = sdf.format(dt);
				
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(currentTime));
				c.add(Calendar.DATE, 2);
				
				dt.setTime (dt.getTime() + (2 * 60 * 1000));//2 hari
				
				String cookieExpire = "expires=" + dt.toGMTString();
				token.expire = cookieExpire;
				System.out.println(token.expire);
				while(!(isTokenUnique(token))){
					token = generateToken(access_token);
				}
				sql = "UPDATE token SET access_token = ?, timestamp = ? WHERE access_token = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, token.access_token);
				stmt.setString(2, currentTime);
				stmt.setString(3, access_token);
				stmt.executeUpdate();
				System.out.println("OK");
				
			}
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
	    	try{
	    		if(stmt!=null)
		            stmt.close();
			} catch(SQLException se2){
		      
			}
			try {
				if(conn!=null)
		            conn.close();
			} catch(SQLException se){
		    	se.printStackTrace();
			}
		}
		return token;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Token post(@FormParam("email") String email,
	@FormParam("password") String password) {
		Token token = generateToken(email,password);
		System.out.println(token.access_token);
		return token;
	}
	
	
	@POST
	@Path("/signout")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void signOut(@FormParam("access_token") String access_token) {
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		
		try{
			String sql = "DELETE FROM token WHERE access_token = ? ";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, access_token);
			stmt.executeUpdate();
			stmt.close();
			conn.close();
		} catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		} finally {
	    	try{
	    		if(stmt!=null)
		            stmt.close();
			} catch(SQLException se2){
		      
			}
			try {
				if(conn!=null)
		            conn.close();
			} catch(SQLException se){
		    	se.printStackTrace();
			}
		}
	}
	
	@POST
	@Path("/regenerateToken")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Token regenerateToken(@FormParam("access_token") String access_token) {
		System.out.println("call regenerateToken");
		Token token = generateToken(access_token);
		System.out.println(token.access_token);
		return token;
	}
	
} 