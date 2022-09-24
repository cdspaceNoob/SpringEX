package gntp.lesson.guestbook.controller;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gntp.lesson.guestbook.dao.GuestbookDAO;
import gntp.lesson.guestbook.vo.GuestbookVO;
import gntp.lesson.guestbook.vo.ReplyVO;

@Controller("guestbookController")
@RequestMapping("/guestbook")
public class GuestbookController {
	
	@Autowired
	private GuestbookDAO guestbookDAO;
	
	/////////////////////    기본 메소드 형    ////////////////////////////////
	public ModelAndView basic(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String viewName = this.getViewName(request);
		
		mav.setViewName(viewName);
		return mav;
	}
	//////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="/test.do", method=RequestMethod.GET)
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		ModelAndView mav = new ModelAndView();
		//String viewName = this.getViewName(request);
		
		mav.setViewName("welcome");
		return mav;
	}
	
	@RequestMapping(value="/list.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		ModelAndView mav = new ModelAndView();
		//String viewName = this.getViewName(request);
		try {
			ArrayList<GuestbookVO> list = guestbookDAO.selectAll();
			mav.addObject("list",list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("listBook");
		return mav;
	}
	
	@RequestMapping(value="/viewWriteBook.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewWriteBook(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		//String viewName = this.getViewName(request);
		
		mav.setViewName("writeBook");
		return mav;
	}
	
	@RequestMapping(value="/create.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView create(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		//String viewName = this.getViewName(request);
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String userId = request.getParameter("userId");
		GuestbookVO book = new GuestbookVO();
		book.setTitle(title);
		book.setContent(content);
		book.setUserId(userId);
		
		try {
			boolean flag = guestbookDAO.insertOne(book);
			if(flag) {
				System.out.println("새글이 등록되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("redirect:./list.do");
		return mav;
	}
	
	@RequestMapping(value="/delete.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView delete(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String seq= request.getParameter("seq");
		try {
			boolean flag = guestbookDAO.deleteOne(seq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.setViewName("redirect:./list.do");
		return mav;
	}
	
	@RequestMapping(value="/read.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView read(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		//String viewName = this.getViewName(request);
		String seq = request.getParameter("seq");
		String token = request.getParameter("token");
		GuestbookVO book = null;
		try {
			book = guestbookDAO.selectOne(seq,token);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.addObject("book",book);
		mav.setViewName("read");
		return mav;
	}
	
	@RequestMapping(value="/writeReply.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView writeReply(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String seq = request.getParameter("seq");
		String content = request.getParameter("reply");
		ReplyVO vo = new ReplyVO();
		vo.setGbSeq(Integer.parseInt(seq));
		vo.setReplyContent(content);
		try {
			boolean flag = guestbookDAO.insertReply(vo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:./read.do?seq="+seq);
		return mav;
	}
	
	@RequestMapping(value="/viewUpdateBook.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewUpdateBook(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String seq = request.getParameter("seq");
		GuestbookVO book = null;
		try {
			book = guestbookDAO.selectOneForUpdate(seq);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mav.addObject("book",book);
		
		mav.setViewName("viewUpdateBook");
		return mav;
	}
	
	@RequestMapping(value="/update.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView update(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		int seq = Integer.parseInt(request.getParameter("seq"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		//System.out.println("update "+content);
		int readCount = Integer.parseInt(request.getParameter("readCount"));
		GuestbookVO book = new GuestbookVO();
		book.setTitle(title);
		book.setContent(content);
		book.setReadCount(readCount);
		book.setSeq(seq);
		
		
		try {
			boolean flag = guestbookDAO.updateOne(book);
			if(flag) {
				System.out.println("글이 수정되었습니다.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mav.setViewName("redirect:./list.do");
		return mav;
	}
	////////////////////////////////// getView  /////////////////////////////////////
	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
		}

		
		int begin = 0; //
		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length(); 
		}

		int end;
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?"); 
		} else {
			end = uri.length();
		}


		String fileName = uri.substring(begin, end);
		if (fileName.indexOf(".") != -1) {
			fileName = fileName.substring(0, fileName.lastIndexOf(".")); 
		}
		if (fileName.lastIndexOf("/") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/"), fileName.length()); 
		}
		return fileName;
	}
}
