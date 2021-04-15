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

public class ModuleManagementServlet extends HttpServlet{

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
		Integer studentId = 0;
		String action=null;
		String paymentstatus="Unpaid";
		String selectQuery="Select * from student_details";
		String selectedModules="mod1"+","+"mod2"+","+"mod3"+","+"mod4";
		
		if (!StringUtils.isNullOrEmpty(req.getParameter("userId"))) {
			studentId = Integer.parseInt(req.getParameter("userId"));
		}
		if (!StringUtils.isNullOrEmpty(req.getParameter("action"))) {
			action = req.getParameter("action");
		}
		if (!StringUtils.isNullOrEmpty(req.getParameter("paymentStatus"))) {
			paymentstatus = req.getParameter("paymentStatus");
			if(paymentstatus.equalsIgnoreCase("Unpaid")) {
				paymentstatus="Paid";
			}else {
				paymentstatus="Unpaid";
			}
		}
		if (!StringUtils.isNullOrEmpty(req.getParameter("optionalModuleOne"))) {
			selectedModules =selectedModules+","+ req.getParameter("optionalModuleOne");
		}
		if (!StringUtils.isNullOrEmpty(req.getParameter("optionalModuleTwo"))) {
			selectedModules =selectedModules+","+ req.getParameter("optionalModuleTwo");
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt1 = null;
		ResultSet rs = null;
		PrintWriter out=null;
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		try {
			out = resp.getWriter();
			conn = ConnectionFactory.getConnection();
			
			if(action != null) {
				String sql = "Update student_details set paymentstatus =? where id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, paymentstatus);
				stmt.setInt(2, studentId);
				
			}else if(!StringUtils.isNullOrEmpty(req.getParameter("optionalModuleTwo"))) {
				String sql = "Update student_details set paymentstatus =?,selected_modules=? where id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, paymentstatus);
				stmt.setString(2, selectedModules);
				stmt.setInt(3, studentId);
				
			}
			if(null != stmt) {
				stmt.executeUpdate();
			}
			
			if(!StringUtils.isNullOrEmpty(req.getParameter("searchWord")) && !req.getParameter("searchColumn").equalsIgnoreCase("0")) {
				selectQuery = selectQuery+ " where "+req.getParameter("searchColumn")+"=?";
				stmt1 = conn.prepareStatement(selectQuery);
				stmt1.setString(1, req.getParameter("searchWord"));
			}else {
				stmt1 = conn.prepareStatement(selectQuery);
			}
			rs = stmt1.executeQuery();
			List<StudentDetails> studentList= getData(rs);
			myObj.addProperty("studentDetails", gson.toJson(studentList));
			myObj.addProperty("success", true);
			out.println(myObj.toString());

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ConnectionFactory.close(rs);
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
