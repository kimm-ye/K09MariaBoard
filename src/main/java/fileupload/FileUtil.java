package fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

public class FileUtil {
	
	//파일 업로드 처리
	public static MultipartRequest uploadFile(HttpServletRequest req, String saveDirectory, int maxPostSize) {
		
		try {
			/*
			MultipartRequest(request내장객체, 디렉토리의 물리적경로, 파일제한용량, 인코딩방식)
			객체를 생성함과 동시에 파일은 업로드가 완료된다.
			업로드가 성공하면 객체의 참조값을 반환한다.
			*/
			return new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8");
		}
		catch (Exception e) {
			/*
			업로드에 실패하는 경우는 용량초과, 디렉토리 경로 오류등이 대부분이다.
			이때 null값을 반환한다.
			 */
			e.printStackTrace();
			return null;
		}
	}
	
	//명시한 파일을 찾아 다운로드 한다.
	public static void download(HttpServletRequest req, HttpServletResponse resp ,String directory, String sfileName, String ofileName) {
		//디렉토리의 물리적 경로 얻어오기
		String sDirectory = req.getServletContext().getRealPath(directory);
		
		try {
		    //서버에 저장된 파일을 찾아 File객체 생성
		    File file = new File(sDirectory, sfileName);  
		    //파일의 내용을 읽어오기 위한 입력스트림생성
		    InputStream iStream = new FileInputStream(file);
		    
		    //웹브라우저에 따른 한글 파일명 깨짐 처리
		    String client = req.getHeader("User-Agent");
		    if (client.indexOf("WOW64") == -1) {
		    	//인터넷 익스플로러가 아닌 경우 한글파일명 인코딩
		    	ofileName = new String(ofileName.getBytes("UTF-8"), "ISO-8859-1");
		    }
		    else {
		    	//인터넷 익스플로러 일때 한글파일명 인코딩
		    	ofileName = new String(ofileName.getBytes("KSC5601"), "ISO-8859-1");
		    }
		    /*
				위 처리를 통해 원본파일명이 한글인 경우 깨짐을 방지할 수 있다.
				getBytes() : String객체를 byte형의 배열로 반환해준다.
		    */
		   
		    /*
		    파일 다운로드를 위한 응답헤더 설정
		    1. 응답헤더 초기화
		    2. 웹브라우저가 인식하지 못하는 컨텐츠 타입을 설정하여 다운로드 창을
		    	강제로 띄우게 함.
		    3. 파일명을 변경하기 위한 응답헤더를 설정하고 파일사이즈도 설정한다.
		    */
		    //다운로드를 위한 response 헤더 설정
		    resp.reset();
		    resp.setContentType("application/octet-stream");
		    resp.setHeader("Content-Disposition", 
		                   "attachment; filename=\"" + ofileName + "\"");
		    resp.setHeader("Content-Length", "" + file.length() );
		    
		    //out.clear();  얘는 새로운 출력스트림을 생성하기 위한 초기화  값인데. 지금 서블릿이니까 JSP를 열지도 않아서 출력스트림을 초기화할 필요가 없음
		    
		    //새로운 출력스트림을 생성해서 파일을 내보낸다.
		    //response 내장 객체를 통해 새로운 출력 스트림을 생성
		    OutputStream oStream = resp.getOutputStream();  
		    
		    //출력 스트림에 파일 내용 출력 (read => write)
		    byte b[] = new byte[(int)file.length()];
		    int readBuffer = 0;    
		    while ( (readBuffer = iStream.read(b)) > 0 ) {
		    	oStream.write(b, 0, readBuffer);
		    }
		    
		    //입출력 스트림을 닫아준다.(자원해제)
		    iStream.close(); 
		    oStream.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다.");
			e.printStackTrace();
		}
		catch (Exception e) {
		    System.out.println("예외가 발생하였습니다.");
		    e.printStackTrace();
		}
	}
	
	//서버에 저장된 파일 존재여부 확인 후 삭제처리
	public static void deleteFile(HttpServletRequest req, String directory, String filename) {
		//물리적경로 가져오기
		String sDirectory = req.getServletContext().getRealPath(directory);
		//파일 객체 생성
		File file = new File(sDirectory + File.separator + filename);
		if(file.exists()) {
			file.delete(); //존재한다면 삭제
		}
	}
	
}
