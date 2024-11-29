package com.example.mmok.Users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private PreparedStatement pstmt;
    private Connection con;


    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "testuser";
    private String pwd = "test1234";


    public boolean userExist(String userId) {
        boolean userExists = false;
        try {
            connDB();
            String query = "SELECT 1 FROM USER_TABLE WHERE USER_ID = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userExists = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userExists;

    }


    public boolean userCheck(String userId,String userPwd) {
        boolean userExists = false;
        try {
            connDB();
            String query = "SELECT 1 FROM USER_TABLE WHERE USER_ID = ? and user_pwd=? ";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, userId);
            pstmt.setString(2, userPwd);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userExists = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userExists;

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