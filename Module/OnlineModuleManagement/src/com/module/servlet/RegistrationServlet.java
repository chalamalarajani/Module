package com.module.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.module.utility.ConnectionFactory;
import com.mysql.jdbc.StringUtils;

public class RegistrationServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String isAdmin = req.getParameter("isAdmin");
		if(!StringUtils.isNullOrEmpty(isAdmin)) {
			if(isAdmin.equalsIgnoreCase("true")) {
				adminRegistration(req,resp);
			}else {
				studentRegistration(req,resp);
			}
			
		}
	}
	
	public void adminRegistration(HttpServletRequest req, HttpServletResponse resp) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PrintWriter out=null;
		try {
			out = resp.getWriter();
			JsonObject myObj = new JsonObject();
			conn = ConnectionFactory.getConnection();
			String sql = "Insert into admin_details(name,password,email,phone_number,security_question,answer) values(?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, req.getParameter("adminRegLogin"));
			stmt.setString(2, req.getParameter("adminRegPassword"));
			stmt.setString(3, req.getParameter("adminemail"));
			stmt.setString(4, req.getParameter("adminMobileNumber"));
			stmt.setString(5, req.getParameter("adminSecurityQuestion"));
			stmt.setString(6, req.getParameter("adminAnswer"));
			int insertRow = stmt.executeUpdate();
			myObj.addProperty("success", false);
			if (insertRow == 1) {
				myObj.addProperty("userName", req.getParameter("adminLogin"));
				myObj.addProperty("success", true);
			}
			out.println(myObj.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ConnectionFactory.close(rs);
				ConnectionFactory.close(stmt);
				ConnectionFactory.close(conn);
				if(null != out) {
					out.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	public void studentRegistration(HttpServletRequest req, HttpServletResponse resp) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PrintWriter out=null;
		try {
			out = resp.getWriter();
			JsonObject myObj = new JsonObject();
			conn = ConnectionFactory.getConnection();
			String sql = "Insert into student_details(roll_number,first_name,last_name,email,password,phone_number,address_1,address_2,pincode) values(?,?,?,?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, req.getParameter("rollNumber"));
			stmt.setString(2, req.getParameter("firstName"));
			stmt.setString(3, req.getParameter("lastName"));
			stmt.setString(4, req.getParameter("email"));
			stmt.setString(5, req.getParameter("regPassword"));
			stmt.setString(6, req.getParameter("mobileNumber"));
			stmt.setString(7, req.getParameter("address1"));
			stmt.setString(8, req.getParameter("address2"));
			stmt.setString(9, req.getParameter("pinCode"));
			
			int insertRow = stmt.executeUpdate();
			myObj.addProperty("success", false);
			if (insertRow == 1) {
				myObj.addProperty("userName", req.getParameter("firstName"));
				myObj.addProperty("success", true);
			}
			out.println(myObj.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ConnectionFactory.close(rs);
				ConnectionFactory.close(stmt);
				ConnectionFactory.close(conn);
				if(null != out) {
					out.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	

}
