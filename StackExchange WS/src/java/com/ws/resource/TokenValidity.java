package com.ws.resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jdk.nashorn.internal.parser.JSONParser;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import com.ws.db.DBConnection;
import com.ws.model.Token;

@Path("/token-validity")
public class TokenValidity {
	
	public static class Identity{
		public int valid=-1;
		public int id_user=0;
	}
	
	public static class AccessValidity{
		public int valid = -1;
	}
	
	public static Identity getIdentity(String access_token){
		Identity identity = new Identity();
		
		//Check database
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		try{
			System.out.println(access_token);
			String sql = "SELECT * FROM token WHERE access_token = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, access_token);
	
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				//cek tanngal expire
				java.util.Date date = rs.getTimestamp("timestamp");
				Date expire = new Date(date.getTime() + TimeUnit.MINUTES.toMillis( 1 ));//2 minutes validity
				long expire_ms = expire.getTime();
				Date cur_date = new Date();
				long cur_date_ms = cur_date.getTime();
				if(expire_ms>cur_date_ms){
					identity.valid = 1;
				} else {
					identity.valid = 0;
				}
				identity.id_user = rs.getInt("id_user");
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
		return identity;
	}
	
	public static int getQuestionUserId(int id_question){
		int id_user = 0;
		DBConnection dbc = new DBConnection();
		PreparedStatement stmt = dbc.getDBStmt();
		Connection conn = dbc.getConn();
		try{
			
			String sql = "SELECT * FROM question WHERE id_question = ?";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id_question);
	
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				id_user = rs.getInt("id_user");
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
		
		return id_user;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Identity post(@FormParam("access_token") String access_token) {
		//Input: FORM
		//Output: JSON
		Identity identity = getIdentity(access_token);
		System.out.println("REST - " + identity.valid);
		return identity;
	}
	
	@POST
	@Path("/getUserID")
	@Consumes("application/json")
	@Produces("application/json")
	public Identity getInJSON(String access_token) throws ParseException {
		//Input: JSON
		//Output: JSON
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(access_token);
		String token = (String) jsonObject.get("access_token");
		
		System.out.println("a_t = " + token);
		Identity identity = getIdentity(token);
		System.out.println("id_user = " + identity.id_user);
		return identity;
	}
	
	@POST
	@Path("/getQuestionAccessValidity")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public AccessValidity isHasRightForThisQuestion(@FormParam("access_token") String access_token,
		@FormParam("id_question") int id_question) {
		AccessValidity a = new AccessValidity();
		Identity identity = getIdentity(access_token);
		if (identity.valid == 1){
			if(getQuestionUserId(id_question) == identity.id_user){
				a.valid = 1;
			}
		}
		else if(identity.valid ==0){
			a.valid = 0;
			System.out.println("Masuk 0");
		}
		else a.valid = -1;
		return a;
	}
	
	/*@POST
	@Path("/validateAnswerRight")
	@Consumes("application/json")
	@Produces("application/json")
	public AccessValidity isHasRightForThisQuestion(String access_token, int id_question) throws ParseException{
		AccessValidity a = new AccessValidity();
		a.valid = false;
		//Input: JSON
		//Output: JSON
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(access_token);
		String token = (String) jsonObject.get("access_token");
		
		System.out.println("a_t = " + token);
		Identity identity = getIdentity(token);
		System.out.println("id_user = " + identity.id_user);
		if (identity.valid){
			if(getQuestionUserId(id_question) == identity.id_user){
				a.valid = true;
			}
		}
		return a;
	}*/
}
