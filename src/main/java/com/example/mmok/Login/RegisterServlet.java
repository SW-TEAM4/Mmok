package com.example.mmok.Login;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	// private PreparedStatement pstmt;
	private Connection con;

	private String driver = "oracle.jdbc.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521/xe";
	private String user = "c##testuser";
	private String pwd = "test1234";
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// register 구현
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		String user_id = request.getParameter("user_id");
		String user_pwd = request.getParameter("user_pwd");
		String user_name = request.getParameter("user_name");

		System.out.println("회원가입 시도: user_id=" + user_id + ", user_name=" + user_name);
		 // 입력값이 null이거나 비어있는지 확인
	    if (user_id == null || user_pwd == null || user_name == null ||
	        user_id.trim().isEmpty() || user_pwd.trim().isEmpty() || user_name.trim().isEmpty()) {
	        
	        // 값이 없으면 에러 메시지와 함께 회원가입 페이지로 이동
	        request.setAttribute("errorMessage", "아이디, 비밀번호, 닉네임을 모두 입력해주세요.");
	        request.getRequestDispatcher("register.jsp").forward(request, response);
	        return;
	    }
	    
		try {
			// Oracle JDBC 드라이버 로드
			connDB();

			String checkSql = "SELECT COUNT(*) FROM USER_TABLE WHERE user_id = ?";
			try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
				checkStmt.setString(1, user_id);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next() && rs.getInt(1) > 0) {
						// 아이디 중복 시 처리
						System.out.println("아이디 중복됨");
						request.setAttribute("errorMessage", "이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
						request.getRequestDispatcher("/register.jsp").forward(request, response);
						return; // 중단
					}
				}
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}

			// 중복이 아니라면 회원정보 삽입
			String insertSql = "INSERT INTO USER_TABLE (user_id, user_pwd, user_name) VALUES (?, ?, ?)";
			try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
				stmt.setString(1, user_id);
				stmt.setString(2, user_pwd);
				stmt.setString(3, user_name);
				stmt.executeUpdate();
				System.out.println("회원정보 삽입 성공");
			}
			request.setAttribute("success", "true");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
