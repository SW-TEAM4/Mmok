package com.example.mmok.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.mmok.Rangking.RangkingDAO;
import com.example.mmok.Rangking.RangkingVO;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private PreparedStatement pstmt;
	private Connection con;

	private String driver = "oracle.jdbc.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521/xe";
	private String user = "c##testuser";
	private String pwd = "test1234";
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		connDB();

		String user_id = (String) request.getParameter("user_id").trim();
		String user_pwd = (String) request.getParameter("user_pwd").trim();
		System.out.println("id:" + user_id + "pw:" + user_pwd);
		if (con != null) {
			try {
				String query = " select user_id, user_name from user_table where user_id=? and user_pwd=?";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, user_id);
				pstmt.setString(2, user_pwd);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					// 유저 정보에 추가로 홈페이지에 표시할 랭킹 정보 가져와서 저장
					// 원래라면 db에 데이터 없을 때 null 처리 해줘야 함
					RangkingDAO dao = new RangkingDAO();
					List<RangkingVO> list = dao.listMembers();
					// 세션 저장소에 저장
					HttpSession session = request.getSession();
					session.setAttribute("list", list); // 랭커들 정보
					session.setAttribute("user_id", user_id); // 유저 id 정보
					session.setAttribute("user_name", rs.getString("user_name")); // 유저 닉네임 정보
					// 유저의 승률 데이터 가져오기
					int win = 0;
					int lose = 0;
					String winLoseQuery = "SELECT " + "COUNT(CASE WHEN p.status = 'W' THEN 1 END) AS win, "
							+ "COUNT(CASE WHEN p.status = 'L' THEN 1 END) AS lose " + "FROM partner p "
							+ "JOIN user_table u ON p.user_id = u.user_id " + "WHERE u.user_id = ?";
					pstmt = con.prepareStatement(winLoseQuery);
					pstmt.setString(1, user_id);
					ResultSet winLoseRs = pstmt.executeQuery();
					if (winLoseRs.next()) {
						win = winLoseRs.getInt("win");
						lose = winLoseRs.getInt("lose");
					}
					session.setAttribute("win", win);
					session.setAttribute("lose", lose);
					// 서블릿에서 -> jsp 리다이렉트시 컨텍스트 path 필요
					request.getRequestDispatcher("/homePage.jsp").forward(request, response);
				} else {
					request.setAttribute("errorMessage", "없는 계정 또는 비밀번호가 틀렸습니다.");
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				}
//        
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}else {
	        System.out.println("Database connection is not established.");
	    }
	}

	private void connDB() {
		try {
			Class.forName(driver);
			System.out.println("드라이버 성공");
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("연결성공");
			// stmt = con.createStatement();
			System.out.println("stmt 생성 성공");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
