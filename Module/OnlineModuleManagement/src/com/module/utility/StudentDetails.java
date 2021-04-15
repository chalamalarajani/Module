package com.module.utility;

import java.util.HashMap;
import java.util.Map;

public class StudentDetails {

	private Integer id;
	private String rollNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String selectedModuleList;
	private String paymentStatus;
	private String allModules;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getSelectedModuleList() {
		return selectedModuleList;
	}

	public void setSelectedModuleList(String selectedModuleList) {
		String selectedList = "";
		if (null != selectedModuleList) {
			String[] moduleList = selectedModuleList.split(",");

			Map<String, String> moduleMap = getModuleList();
			for (int i = 0; i < moduleList.length; i++) {
				if (i == 0) {
					selectedList = moduleMap.get(moduleList[i]);
				} else {
					selectedList = selectedList + "," + moduleMap.get(moduleList[i]);
				}
			}

		}
		this.selectedModuleList = selectedList;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(String rollNumber) {
		this.rollNumber = rollNumber;
	}

	public Map<String, String> getModuleList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mod1", "IT Security");
		map.put("mod2", "English For Academic Course");
		map.put("mod3", "Cyber Law");
		map.put("mod4", "IT Fundamentals");
		map.put("mod5", "Content Delivery Networks");
		map.put("mod6", "Business Data Analytics");
		map.put("mod7", "Network Engineering");
		map.put("mod8", "Programming Environment");
		return map;
	}

	public String getAllModules() {
		return allModules;
	}

	public void setAllModules(String allModules) {
		this.allModules = allModules;
	}

}
