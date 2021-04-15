package com.module.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.module.utility.ConnectionFactory;
import com.module.utility.StudentDetails;
import com.mysql.jdbc.StringUtils;

public class LoginServlet extends HttpServlet {

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
		String userName= null;
		String password = null;
		String sql= null;
		
		if(!StringUtils.isNullOrEmpty(isAdmin)) {
			if(isAdmin.equalsIgnoreCase("true")) {
				 userName = req.getParameter("adminLogin");
				 password = req.getParameter("adminPassword");
				 sql = "Select * from admin_details where email=? and password=?";
			}else {
				userName = req.getParameter("studentlogin");
				 password = req.getParameter("studentPassword");
				 sql = "Select * from student_details where email=? and password=?";
			}
			
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PrintWriter out=null;
		Gson gson = new Gson();
		try {
			out = resp.getWriter();
			JsonObject myObj = new JsonObject();
			conn = ConnectionFactory.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userName);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			myObj.addProperty("success", false);
			while (rs.next()) {
				myObj.addProperty("userId",rs.getInt("id"));
				
				stmt1 = conn.prepareStatement("Select * from student_details");
				rs1 = stmt1.executeQuery();
				List<StudentDetails> studentList= getData(rs1);
				StudentDetails studentDetails = new StudentDetails();
				for(StudentDetails sDetails : studentList) {
					if(sDetails.getId() == rs.getInt("id")) {
						studentDetails = sDetails;
					}
				}
				if(isAdmin.equalsIgnoreCase("true")) {
					myObj.addProperty("userName", rs.getString("name"));
				}else {
					myObj.addProperty("userName", rs.getString("first_name")+" "+rs.getString("last_name"));
				}
				myObj.addProperty("studentDetailsObject", gson.toJson(studentDetails));
				myObj.addProperty("studentDetails", gson.toJson(studentList));
				
				myObj.addProperty("success", true);
			}
			out.println(myObj.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ConnectionFactory.close(rs);
				ConnectionFactory.close(rs1);
				ConnectionFactory.close(stmt);
				ConnectionFactory.close(stmt1);
				ConnectionFactory.close(conn);
				if(null != out) {
					out.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public List<StudentDetails> getData(ResultSet rs) throws SQLException {
		List<StudentDetails> roomList = new ArrayList<StudentDetails>();
		while (rs.next()) {
			StudentDetails bean = new StudentDetails();
			bean.setId(rs.getInt("id"));
			bean.setFirstName(rs.getString("first_name"));
			bean.setLastName(rs.getString("last_name"));
			bean.setPhoneNumber(rs.getString("phone_number"));
			bean.setEmail(rs.getString("email"));
			bean.setPaymentStatus(rs.getString("paymentstatus"));
			bean.setAllModules(rs.getString("selected_modules"));
			bean.setSelectedModuleList(rs.getString("selected_modules"));
			bean.setRollNumber(rs.getString("roll_number"));
			roomList.add(bean);
		}
		return roomList;
	}
	
}
