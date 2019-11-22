<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "MariaDB.ConnectDB" %>


<%
   request.setCharacterEncoding("UTF-8");
   String id = request.getParameter("id");
   String passwd = request.getParameter("passwd");
   String nickname = request.getParameter("nick");
   String birth = request.getParameter("birth");
   String job = request.getParameter("job");
   String interest = request.getParameter("interest");
   
   System.out.println("출력: "+id+passwd+ nickname+ birth+ job+ interest);
   //싱글톤 방식으로 자바 클래스를 불러옵니다.
   ConnectDB connectDB = ConnectDB.getInstance();
   String returns = connectDB.memberInsert(id, passwd, nickname, birth, job, interest);
   System.out.println("join: "+returns);
   out.print(returns);
%></html>