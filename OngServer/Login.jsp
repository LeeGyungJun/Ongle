<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.ConnectDB" %>

<%
   request.setCharacterEncoding("UTF-8");
   String mem_id = request.getParameter("id");
   String mem_passwd = request.getParameter("passwd");
   System.out.println("로그인시도: " + mem_id + mem_passwd);

   //싱글톤 방식으로 자바 클래스를 불러옵니다.
   ConnectDB connectDB = ConnectDB.getInstance();
   String returns = connectDB.memberLogin(mem_id, mem_passwd);
   System.out.println("login: "+returns);
   out.print(returns);
%>