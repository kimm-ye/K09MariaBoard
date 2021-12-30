<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>파일 첨부형 게시판</title>
</head>
<body>
	<h2>파일 첨부형 게시판</h2>
	<a href="../mvcboard/list.do">게시판 목록 바로가기</a>
	<!-- 
	여기서 실행하지 않고 바로 list.jsp실행하면 데이터가 보이지 않는 이유가
	서블릿은 jsp와 달리 이클립스에서 바로 실행할 수 없기 때문이다.
	서블릿 파일이 아닌 개발자가 정한 요청명(예를들어 .do나 .choice같은) 으로 요청을 보내야
	이에 매핑(web.xml이나 이노테이션을 통해)된 서블릿이 실행되는 구조이기 때문이다.
	그래서 편의를 위해 목록 바로가기 링크가 포함된 진입페이지를 만들어 이를 실행해야 한다.
	-->
</body>
</html>