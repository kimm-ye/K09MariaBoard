package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import fileupload.FileUtil;
import utils.JSFunction;

@WebServlet("/mvcboard/edit.do")
public class EditController extends HttpServlet{

	//수정페이지 진입
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//일련번호 파라미터 받기
		String idx = req.getParameter("idx"); //일련번호를 가져와서
		MVCBoardDAO dao = new MVCBoardDAO();
		//게시물가져오기
		MVCBoardDTO dto = dao.selectView(idx); //dao의 selectview를 가서 게시물을 가져온다.
		//DTO객체를 리퀘스트 영역에 저장
		req.setAttribute("dto", dto); //그리고 그걸 dto로 set한다.
		req.getRequestDispatcher("/14MVCBoard/Edit.jsp").forward(req, resp);
	}
	
	//수정처리
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//물리적경로 얻어오기
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		//업로드 제한 용량 얻어오기
		ServletContext application = this.getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		//파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize);
		
		//파일업로드에 실패한 경우
		if(mr == null) { //여기서 null값이 나오는 건 파일이 없어서가 아니라 파일을 올렸는데 실패한 경우이다.
			JSFunction.alertBack(resp, "첨부파일이 제한 용량을 초과합니다.");
			return;
		}
		
		//폼값 저장
		String idx = mr.getParameter("idx"); //일련번호
		//새롭게 등록된 파일이 없는 경우 Query문에 사용함.
		String prevOfile = mr.getParameter("prevOfile"); //기존 DB에 등록된 원본파일명
		String prevSfile = mr.getParameter("prevSfile"); //기존 DB에 등록된 저장된 파일명
		
		//수정페이지에서 새롭게 입력한 폼값
		String name = mr.getParameter("name"); 
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		
		//패스워드 검증시 session영역에 저장된 속성값 가져와서 DTO에 저장 (이러면 패스워드 두번 안받아도 되니까)
		HttpSession session = req.getSession();
		String pass = (String)session.getAttribute("pass");
		
		//DTO에 데이터 세틱하기
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setIdx(idx);
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		
		//새롭게 저장된 파일이 있는지 확인하기 위한 파일명 얻어옴
		String fileName = mr.getFilesystemName("ofile");
		if(fileName != null) { //새롭게 등록된 파일이 있으면...
			//저장할 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = now + ext;
			
			//파일명 변경
			File oldFile = new File(saveDirectory + File.separator + fileName); 
			File newFile = new File(saveDirectory + File.separator + newFileName); 
			oldFile.renameTo(newFile);
			
			//DTO에 기존파일명과 변경된 파일명 저장
			dto.setOfile(fileName);
			dto.setSfile(newFileName);
			//새롭게 파일이 등록되었기 떄문에 기존에 등록한 파일은 삭제
			FileUtil.deleteFile(req, "/Uploads", prevSfile);
		}
		else { //새롭게 등록한 파일이 없는 경우...
			//기존에 등록한 파일명을 유지한다.
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		//DB 업데이트 처리
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.updatePost(dto);
		dao.close();
		
		if(result == 1) { //수정이 완료되었다면 
			//세션영역에 저장된 패스워드는 삭제
			session.removeAttribute("pass");
			//상세보기 페이지로 이동
			resp.sendRedirect("../mvcboard/view.do?idx=" + idx);
		}
		else { //수정에 실패한 경우
			//상세보기 페이지로 이동하여 비밀번호 검증을 다시 하도록 한다.
			JSFunction.alertLocation(resp, "비밀번호 검증을 다시 진행해주세요", "../mvcboard/view.do?idx=" + idx);
		}
	}
}
