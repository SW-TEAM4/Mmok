package com.example.mmok.Rangking;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class RangkingServlet
 */
@WebServlet("/rangking")
public class RangkingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8"); // MIME-TYPE 설정

        RangkingDAO dao = new RangkingDAO();
        List<RangkingVO> list = dao.listMembers();
        
        for(RangkingVO member: list) {
        	System.out.println("member: " + member.getUserId() + "name: " + member.getUserName()
        	+ "win: " + member.getWinCount() + "lose: " + member.getLoseCount() + "rank: " + member.getRanking());
        }
        
        // request 저장소에 저장
        request.setAttribute("list", list);

        // 포워딩
        request.getRequestDispatcher("/homepage.jsp").forward(request, response);
    }

}
