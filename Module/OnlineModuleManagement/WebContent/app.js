function handleLogin(isAdmin){
	if(isAdmin){
		dataString = $("#adminForm").serialize();
	}else{
		dataString = $("#studentForm").serialize();
	}
    document.getElementById("loginErrorMsg").innerHTML = "";
    
    $.ajax({
        type: "POST",
        url: "LoginServlet",
        data: dataString,
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
            if (data.success) {
                localStorage.setItem("userId",data.userId);
                localStorage.setItem("userName",data.userName);
                var originUrl = window.location.origin;
                if (isAdmin) {
                	 localStorage.setItem("studentDetailsList",data.studentDetails);
                    window.location.replace(originUrl+"/OnlineModuleManagement/admindashboard.html");
                } else {
                	localStorage.setItem("loggedInstudentDetails",data.studentDetailsObject);
                    window.location.replace(originUrl+"/OnlineModuleManagement/studentdashboard.html");
                }
            } else {
                document.getElementById("loginErrorMsg").innerHTML = "Invalid credentials.Please try again";
            }
        },
        error: function(jqXHR,textStatus,errorThrown) {
            document.getElementById("loginErrorMsg").innerHTML = "Something went wrong.Please try again";
        },
        beforeSend: function(jqXHR, settings) {
        	if(isAdmin){
        		validateAdminLoginForm();
        	}else {
        		validateStudentLoginForm();
        	}
            
        },
    });
	
}

function setUserData(isAdmin){
    var originUrl = window.location.origin;
    document.getElementById("userName").innerHTML = localStorage.getItem("userName");
    document.getElementById("userId").innerHTML = localStorage.getItem("userId");
    var studentList = localStorage.getItem("studentDetailsList");
    var student = $.parseJSON(localStorage.getItem("loggedInstudentDetails"));
    if (userId == null) {
    	if (isAdmin) {
            window.location.replace(originUrl+"/OnlineModuleManagement/admin.html");
        } else {
            window.location.replace(originUrl+"/OnlineModuleManagement/student.html");
        }
    }else{
    	if (isAdmin) {
    		buildDetailsTable($.parseJSON(studentList));
    		searchDetails();
        } else {
        	var modules = student.allModules.split(",");
        		$("#optionalModuleOne").val(modules[4]);
        		$("#optionalModuleTwo").val(modules[5]);
        		$("#paymentStatus").val(student.paymentStatus);
        }
    	
    }
    
}

function validateStudentLoginForm(){
	if ($("#studentlogin").val() == "") {
        document.getElementById("loginErrorMsg").innerHTML = "Please enter User Name";
        return false;
    } else if ($("#studentPassword").val() == "") {
        document.getElementById("loginErrorMsg").innerHTML = "Please enter Password";
        return false;
    }
}
function validateAdminLoginForm(){
	if ($("#adminLogin").val() == "") {
        document.getElementById("loginErrorMsg").innerHTML = "Please enter User Name";
        return false;
    } else if ($("#adminPassword").val() == "") {
        document.getElementById("loginErrorMsg").innerHTML = "Please enter Password";
        return false;
    }
}

function handleRegistration(isAdmin) {
	
	if(isAdmin){
		dataString = $("#adminForm").serialize();
	}else{
		dataString = $("#studentForm").serialize();
	}
    
    $.ajax({
            type: "POST",
            url: "RegistrationServlet",
            data: dataString,
            dataType: "json",
            success: function(data, textStatus, jqXHR) {
                if (data.success) {
                    document.getElementById("regSuccessMsg").innerHTML = "Data saved Successfully.Please Login";
                } else {
                    document.getElementById("regErrorMsg").innerHTML = "Something went wrong.Please try again";
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                document.getElementById("regErrorMsg").innerHTML = "Something went wrong.Please try again";
            },
            beforeSend: function(jqXHR, settings) {
            	if(isAdmin){
            		validateAdminRegForm();
            	}else {
            		validatestudentRegForm();
            	}
                
            },
        });

}

function submitSelectedModules(){
	var userId = localStorage.getItem("userId");
	var optionalModuleOne = $("#optionalModuleOne").val();
	var optionalModuleTwo = $("#optionalModuleTwo").val();
	dataString = $("#moduleForm").serialize();
	$.ajax({
        type: "POST",
        url: "ModuleManagementServlet",
        data: {userId:userId,optionalModuleOne:optionalModuleOne,optionalModuleTwo:optionalModuleTwo},
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
        	document.getElementById("dashBoardSuccessMsg").innerHTML = "Data saved Successfully.";
        },
        error: function(jqXHR, textStatus, errorThrown) {
            document.getElementById("dashBoardErrorMsg").innerHTML = "Something went wrong.Please try again";
        },
        beforeSend: function(jqXHR, settings) {
            
        },
    });
	
}

function validateAdminRegForm(){
	if ($("#adminRegLogin").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter First Name";
        return false;
    }else if ($("#adminRegPassword").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Last Name";
        return false;
    }else if ($("#adminConfirmPassword").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Password";
        return false;
    } else if ($("#adminRegPassword").val() != $("#adminConfirmPassword").val()) {
        document.getElementById("regErrorMsg").innerHTML = "Password and Confirm Password must be same";
        return false;
    } else if ($("#adminMobileNumber").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Mobile Numer";
        return false;
    } else if ($("#adminemail").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Email";
        return false;
    }else if ($("#adminSecurityQuestion").val() == 0) {
        document.getElementById("regErrorMsg").innerHTML = "Please Select Security Question";
        return false;
    } else if ($("#adminAnswer").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Answer";
        return false;
    }
	
}
function validatestudentRegForm(){
	
	if ($("#firstName").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter First Name";
        return false;
    }else if ($("#lastName").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Last Name";
        return false;
    }else if ($("#regPassword").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Password";
        return false;
    } else if ($("#confirmPassword").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Confirm Password";
        return false;
    } else if ($("#regPassword").val() != $("#confirmPassword").val()) {
        document.getElementById("regErrorMsg").innerHTML = "Password and Confirm Password must be same";
        return false;
    } else if ($("#mobileNumber").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Mobile Numer";
        return false;
    } else if ($("#email").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Email";
        return false;
    }else if ($("#address1").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Address 1";
        return false;
    } else if ($("#address2").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Address 2";
        return false;
    }else if ($("#rollNumber").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Roll Numer";
        return false;
    } else if ($("#pinCode").val() == "") {
        document.getElementById("regErrorMsg").innerHTML = "Please enter Pin Code";
        return false;
    } 
}

function enableRegistrationDiv(){
	$("#studentLoginDiv").hide();
	$("#studentRegistrationDiv").show();
}
function enableLoginDiv(){
	$("#studentLoginDiv").show();
	$("#studentRegistrationDiv").hide();
}
function adminEnableRegistrationDiv(){
	$("#adminLoginDiv").hide();
	$("#adminRegistrationDiv").show();
}
function adminEnableLoginDiv(){
	$("#adminLoginDiv").show();
	$("#adminRegistrationDiv").hide();	
}
function handleLogOut(isAdmin) {
    localStorage.clear();
    var originUrl = window.location.origin;
    if (isAdmin) {
        window.location.replace(originUrl+"/OnlineModuleManagement/admin.html");
    } else {
        window.location.replace(originUrl+"/OnlineModuleManagement/student.html");
    }

}

function handleEditRoom(item) {
	document.getElementById("SelStudentId").innerHTML=item.rollNumber;
	document.getElementById("selStudentName").innerHTML=item.firstName+" "+item.lastName;
	document.getElementById("stdselectedModulelist").innerHTML=item.selectedModuleList;
	document.getElementById("SelPaymentStatus").innerHTML=item.paymentStatus;
	document.getElementById("selStudenEmail").innerHTML=item.email;
    
}
function buildDetailsTable(myArr) {
    document.getElementById("tbody").innerHTML = "";
    $.each( myArr,function(index, item) {
                var sNo = parseInt(index) + 1;
                var myJSON = JSON.stringify(item);
                var eachrow = "<tr>" + "<td>" +
                    sNo +
                    "</td>" +
                    "<td>" +
                    item.rollNumber +
                    "</td>" +
                    "<td>" +
                    item.firstName+"  "+item.lastName +
                    "</td>" +
                    "<td>" +
                    item.email +
                    "</td>" +
                    "<td>" +
                    "<input  type='button' class='toogle-btn'  value='Toggle' onclick='updatePaymentStatus(" + myJSON +")'/>" + item.paymentStatus + 
                    "</td>" +
                    "<td> <input type='button' class='toogle-btn' value='Show' onclick='handleEditRoom(" +
                    myJSON +
                    ")'/></td>" +
                    "</tr>";

                $('#tbody').append(eachrow);
            });
}

function updatePaymentStatus(item){
	var userId = item.id;
	var paymentStatus = item.paymentStatus;
	$.ajax({
        type: "POST",
        url: "ModuleManagementServlet",
        data: {userId:userId,paymentStatus:paymentStatus,action:'updatePayment'},
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
         if (data.success) {
        	localStorage.setItem("studentDetailsList",data.studentDetails);
        	buildDetailsTable($.parseJSON(data.studentDetails));
         }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            document.getElementById("regErrorMsg").innerHTML = "Something went wrong.Please try again";
        },
        beforeSend: function(jqXHR, settings) {
            
        },
    });
	
}
function searchDetails(){
	
	
	var searchWord = document.getElementById("searchWord").value;
	var searchColumn = document.getElementById("searchColumn").value;
	$.ajax({
        type: "POST",
        url: "ModuleManagementServlet",
        data: {searchWord:searchWord,searchColumn:searchColumn},
        dataType: "json",
        success: function(data, textStatus, jqXHR) {
         if (data.success) {
        	localStorage.setItem("studentDetailsList",data.studentDetails);
        	buildDetailsTable($.parseJSON(data.studentDetails));
         }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            document.getElementById("regErrorMsg").innerHTML = "Something went wrong.Please try again";
        },
        beforeSend: function(jqXHR, settings) {
            
        },
    });
	
}