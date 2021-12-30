<%@page import="model1.board.BoardDTO"%>
<%@page import="model1.board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 글쓰기 페이지 진입전 로그인 확인 -->
<%@ include file="./IsLoggedIn.jsp" %>
<%
String num = request.getParameter("num"); //게시물의 일련번호를 파라미터 값으로 반환한다.
BoardDAO dao = new BoardDAO(application); //DB연결
BoardDTO dto = dao.selectView(num);		  //게시물조회 (게시물을 조회해야 상세 내용을 볼 수 있으므로)
//세션영역에 저장된 회원아이디를 얻어와서 문자열의 형태로 변환 (session은 Object타입이므로)
String sessionId = session.getAttribute("UserId").toString();

/* 
본인이 작성한 글이 아니어도 URL패턴을 분석하면 수정페이지로 진입할 수
있으므로 페이지 진입전 본인확인을 추가로 하는 것이 안전하다.
*/
if(!sessionId.equals(dto.getId())){
	JSFunction.alertBack("작성자 본인만 수정할 수 있습니다.", out);
	return; //JSP코드에서 return쓴다는 것은 함수를 바로 멈춘다는 것
}
dao.close(); //DB 연결해제
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원제 게시판</title>

<script type="text/javascript">

function validateForm(form) { //폼 내용 검증
	if(form.title.value == ""){
		alert("제목을 입력하세요");
		form.title.focus();
		return false;
	}
	if(form.content.value == ""){
		alert("내용을 입력하세요");
		form.content.focus();
		return false;
	}
}
</script>
</head>
<body>

<jsp:include page="../Common/Link.jsp"/>

<h2>회원제 게시판 - 수정하기(Edit)</h2>
<!-- 작성하는게 아니라 수정해야하기 때문에 action="EditProcess.jsp" -->
<form name="writeFrm" method="post" action="EditProcess.jsp" onsubmit="return validateForm(this);">

	<!-- 만약에 하단의 input이 없다면 내용이랑 제목을 수정할때 몇번 num을 해야하는지 모름. hidden이 아니라면 num이 화면에 노출됨-->
	<!-- 
		게시물의 일련번호를 서버로 전송하기 위해 hidden타입의 입력상자가 필요하다. (중요)
		hidden으로 처리하지 않으면 일련번호가 노출되어 이상하다. 
	-->
	<input type="hidden" name="num" value="<%= dto.getNum() %>" >
	<table border="1" width="90%">
		<tr>
			<td>제목</td>
			<td>
				<input type="text" name="title" style="width: 90%;" value="<%= dto.getTitle()%>" >
			</td>
		</tr>
		<tr>
			<td>내용</td>
			<td>
				<textarea name="content" style="width: 90%; height: 100px;"><%= dto.getContent()%></textarea>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<button type ="submit">수정 완료</button>
				<button type ="reset">다시 입력</button> <!-- 내용은 이전에서 가져온거기 떄문에 다시입력하면 새로 입력한 값만 사라진다 -->
				<button type ="button" onclick="location.href='List.jsp';">목록보기</button>
			</td>
		</tr>
	</table>
</form>
</body>
</html>