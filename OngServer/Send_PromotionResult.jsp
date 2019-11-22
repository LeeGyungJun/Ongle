<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.*" %>

<%
	request.setCharacterEncoding("UTF-8");
	String location = request.getParameter("location");
	System.out.println("promotion 서버 접속");
	
	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	ConnectDB connectDB = ConnectDB.getInstance();
	String returns = connectDB.promotion_Result(location);
	System.out.println("promotion_result: "+returns);
	out.print(returns);
%>