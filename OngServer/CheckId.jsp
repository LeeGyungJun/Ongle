<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.ConnectDB" %>

<%
   request.setCharacterEncoding("UTF-8");
   String id = request.getParameter("id");
   
   //싱글톤 방식으로 자바 클래스를 불러옵니다.
   ConnectDB connectDB = ConnectDB.getInstance();
   String returns = connectDB.memberSelect(id);
   System.out.println("id check: "+returns);
   out.print(returns);
%>