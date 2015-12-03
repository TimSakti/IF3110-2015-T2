package com.ws.db;

import java.util.*;
import java.sql.*;

public class DBConnection {
	// JDBC driver name dan database URL
	final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
	final String DB_URL="jdbc:mysql://localhost/mydb";
		
	//  Username dan password
	final String USER = "root";
	final String PASS = "";//sesuaikan dengan password database
	
	public Connection conn;
	public PreparedStatement stmt;
	
	public DBConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			
		} catch (SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
	}
	
	public Connection getConn(){
		return conn;
	}
	
	public PreparedStatement getDBStmt(){
		return stmt;
	}
	
}
