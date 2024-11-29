package com.example.mmok.Game;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GameDao {


    //DB연결
    private PreparedStatement pstmt;
    private Connection con;

    private GameVo vo = new GameVo();


    private String driver = "oracle.jdbc.OracleDriver";
    private String url = "jdbc:oracle:thin:@localhost:1521/xe";
    private String user = "c##testuser";
    private String pwd = "test1234";


    //방생성하기
    public void createGameRoom(GameVo gameVo) {
        try {
            connDB();
            String user_id1 = gameVo.getUser_id1();
            String game_code = gameVo.getGame_code();
            String query = "INSERT INTO GAME_ROOM(user_id1, game_code)values (?,?) ";

            System.out.println("pstsm : " + query);
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, user_id1);
            pstmt.setString(2, game_code);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //secondInsertRoom --> update로
    public void secondInsertGameRoom(String user_id2, String game_code) {
        try {
            connDB();
            String query = "UPDATE GAME_ROOM\n" +
                    "SET USER_ID2 = ?\n" +
                    "WHERE GAME_CODE = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, user_id2);
            pstmt.setString(2, game_code);

            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("유저2가 잘 들어갔음");
            } else {
                System.out.println("유저2 인서트 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //게임코드 일치 확인
    public boolean checkGameCode(String game_code) {
        boolean isExist = false;
        try {
            connDB();
            String query = "SELECT 1 FROM GAME_ROOM WHERE GAME_CODE = ?";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, game_code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExist;
    }



    //게임방 보여줌
    public List<GameVo> viewTotalGameRoom() {
        List<GameVo> list = new ArrayList<GameVo>();

        try {
            // 데이터베이스 연결
            connDB();

            // SQL 쿼리 작성
            String query = "SELECT GAME_IDX, USER_ID1, USER_ID2,GAME_CODE FROM GAME_ROOM";
            pstmt = con.prepareStatement(query);

            // 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            // 결과 처리
            while (rs.next()) {
                int gameIdx = rs.getInt("GAME_IDX");
                String userId1 = rs.getString("USER_ID1");
                String userId2 = rs.getString("USER_ID2");
                String gameCode = rs.getString("GAME_CODE");

                GameVo gameVo = new GameVo();

                // GameVo 객체에 데이터 설정
                gameVo.setGame_idx(gameIdx);
                gameVo.setUser_id1(userId1);
                gameVo.setUser_id2(userId2);
                gameVo.setGame_code(gameCode);

                list.add(gameVo);
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //1 = true면 다 참 --> 더 못들어오게
    public boolean isFull(String game_code){
        boolean isFull = false;
        try{
            connDB();
            String query ="SELECT COUNT(*) AS room_full\n" +
                    "FROM game_room\n" +
                    "WHERE GAME_CODE = ?\n" +
                    "AND (user_id1 IS NOT NULL and user_id2 IS NOT NULL) ";
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, game_code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int roomFull = rs.getInt("room_full"); // COUNT 결과
                isFull = (roomFull > 0); // COUNT가 1 이상이면 가득 찬 상태
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isFull;

    }





    //방 존재하는지 확인
    public void checkGameRoom(GameVo gameVo) {
        try {
            connDB();
            String game_code = gameVo.getGame_code();  // 게임 코드 가져오기

            String query = " SELECT CASE\n" +
                    "           WHEN EXISTS (SELECT 1 FROM GAME_ROOM WHERE GAME_CODE = ?) THEN '존재'\n" +
                    "           ELSE '존재하지 않음'\n" +
                    "       END AS result\n" +
                    "FROM dual;\n";

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, game_code);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String result = rs.getString("result");
                if ("true".equals(result)) {
                    System.out.println("게임 존재");
                } else {
                    System.out.println("게임 없음");
                }
            }
            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //게임 방idx 조회
    public void checkGameRoomIdx (String user_id1){
        try {
            connDB();  // DB 연결

            // USER_ID1, USER_ID2에 해당하는 게임 방 idx 조회
            String query = "SELECT GAME_IDX FROM GAME_ROOM WHERE USER_ID1 = ?";
            pstmt = con.prepareStatement(query);

            pstmt.setString(1, user_id1);  // USER_ID1을 첫 번째 파라미터로 설정


            ResultSet rs = pstmt.executeQuery();  // 데이터 조회

            if (rs.next()) {
                // 조회된 GAME_IDX 값 출력
                int gameIdx = rs.getInt("game_idx");
                System.out.println("게임 방 idx: " + gameIdx);
            } else {
                // 해당 USER_ID1, USER_ID2에 대한 데이터가 없다면 출력
                System.out.println("해당 게임 방이 존재하지 않음");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //생성 코드 보여주기 viewGameRoom
    public void viewGameRoom(int game_idx) {
        try {
            connDB();
            String query = "select GAME_CODE from GAME_ROOM where GAME_IDX=?";

            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, game_idx);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // 조회된 GAME_IDX 값 출력
                String game_code = rs.getString("game_code");
                System.out.println("게임코드: " + game_code);
            } else {
                // 해당 USER_ID1, USER_ID2에 대한 데이터가 없다면 출력
                System.out.println("해당 게임 방이 존재하지 않음");
            }

            rs.close();
            pstmt.close();
            con.close();

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