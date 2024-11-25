package com.example.omok_chaechae.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    //private PreparedStatement pstmt;
    private Connection con;

    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "testuser";
    private String pwd = "test1234";
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = response.getWriter();

        String user_id = request.getParameter("user_id");
        String user_pwd = request.getParameter("user_pwd");
        String user_name = request.getParameter("user_name");

        System.out.println("회원가입 시도: user_id=" + user_id + ", user_name=" + user_name);

        try {
            // Oracle JDBC 드라이버 로드
            connDB();

                String checkSql = "SELECT COUNT(*) FROM USERS WHERE user_id = ?";
                try (PreparedStatement checkStmt = con.prepareStatement(checkSql)) {
                    checkStmt.setString(1, user_id);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            // 아이디 중복 시 처리
                            System.out.println("아이디 중복됨");
                            response.getWriter().println("<script>alert('이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.'); window.history.back();</script>");
                            return; // 중단
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

            // 중복이 아니라면 회원정보 삽입
                String insertSql = "INSERT INTO USERS (user_id, user_pwd, user_name) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
                    stmt.setString(1, user_id);
                    stmt.setString(2, user_pwd);
                    stmt.setString(3, user_name);
                    stmt.executeUpdate();
                    System.out.println("회원정보 삽입 성공");
                }
                response.sendRedirect("login.jsp");
            } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    private void connDB() {
        try {
            Class.forName(driver);
            System.out.println("드라이버 성공");
            con = DriverManager.getConnection(url, user, pwd);
            System.out.println("연결성공");
            //stmt = con.createStatement();
            System.out.println("stmt 생성 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
