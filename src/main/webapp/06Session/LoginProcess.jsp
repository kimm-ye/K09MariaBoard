<%@page import="membership.MemberDTO"%>
<%@page import="membership.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

//로그인 폼에서 사용자가 입력한 아이디, 패스워드 받기
String userId = request.getParameter("user_id"); 
String userPwd = request.getParameter("user_pw");

//application 내장객체를 통해 web.xml에 저장된 오라클 접속정보를 읽어옴
String mariaDBDriver = application.getInitParameter("MariaDBDriver");
String mariaDBURL= application.getInitParameter("MariaDBURL");
String mariaDBId = application.getInitParameter("MariaDBId");
String mariaDBPwd = application.getInitParameter("MariaDBPwd");

//JDBC를 통해 오라클 접속
MemberDAO dao = new MemberDAO(mariaDBDriver, mariaDBURL, mariaDBId, mariaDBPwd);
//받아온 아이디, 패스워드를 매개변수로 getMemberDTO()호출. 회원인증 시도함.
MemberDTO memberDTO = dao.getMemberDTO(userId, userPwd);
//자원해제
dao.close();


if(memberDTO.getId() != null){ //private이니까 getter로 접근한다.
	//회원인증(로그인)에 성공한 경우 : session 영역에 저장할거다. -페이지 이동하더라도 로그인 정보가 남아있어야하므로
	/*
		세션영역에 아이디와 이름을 저장한다.
		세션영역은 페이지를 이동하더라도 웹브라우저를 닫을때까지는
		영역이 공유되어 접근할 수 있다.
	*/
	session.setAttribute("UserId", memberDTO.getId());
	session.setAttribute("UserName", memberDTO.getName());
	//로그인 페이지로 단순히 이동한다.
	response.sendRedirect("LoginForm.jsp");
}
else{
	//회원인증(로그인)에 실패한 경우
	/*
		리퀘스트 영역에 실패 메세지를 저장한다.
		리퀘스트 영역은 하나의 요청이 완료될때까지 영역을 공유한다.
		따라서 포워드 된 페이지까지 데이터를 공유할 수 있다.
	*/
	request.setAttribute("LoginErrMsg", "로그인 오류입니다.");
	//로그인 페이지로 포워드(페이지 전달)한다.
	request.getRequestDispatcher("LoginForm.jsp").forward(request, response); //포워드를 걸 페이지를 넣고, 포워드 명령어 넣기 requset와 response를 넘긴다.
}
%>