<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.ConnectDB" %>

<%
   request.setCharacterEncoding("UTF-8");
   String gps = request.getParameter("gpsData");
   String mem_id = request.getParameter("cookieId");
   System.out.println("gps 서버 접속");
   
   //싱글톤 방식으로 자바 클래스를 불러옵니다.
   ConnectDB connectDB = ConnectDB.getInstance();
   String returns = connectDB.gpsSave(gps, mem_id);
   System.out.println("gpsData: "+returns);
   out.print(returns);
%>