package com.example.omok_chaechae.Login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private PreparedStatement pstmt;
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

        try  {

            String query = "SELECT * FROM USERS WHERE user_id = ? AND user_pwd = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, user_id);
            pstmt.setString(2, user_pwd);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                response.getWriter().println("로그인 성공");
            } else {
                response.getWriter().println("아이디 또는 비밀번호가 잘못되었습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
