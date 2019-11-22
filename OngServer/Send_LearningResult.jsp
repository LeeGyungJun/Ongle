<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.ConnectDB" %>

<%
	request.setCharacterEncoding("UTF-8");
	String mem_id = request.getParameter("cookieId");
	System.out.println("learning 서버 접속");
	
	//싱글톤 방식으로 자바 클래스를 불러옵니다.
	ConnectDB connectDB = ConnectDB.getInstance();
	String returns = connectDB.learning_Result(mem_id);
	System.out.println("learning_result: "+returns);
	out.print(returns);
%>