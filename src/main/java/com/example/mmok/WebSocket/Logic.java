package com.example.mmok.WebSocket;

public class Logic {
    private static final int SIZE = 15; // 오목 보드 크기 (15x15)
    private String[][] board; // 2D 배열로 보드 상태를 관리
    private static final String EMPTY = "empty"; // 빈칸
    private static final String BLACK = "black"; // 흑 돌
    private static final String WHITE = "white"; // 백 돌

    public Logic() {
        // 보드 초기화
        board = new String[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    // 돌을 놓는 메서드
    public boolean makeMove(int row, int col, String color) {
        // 유효한 위치인지 확인
        if (row < 0 || col < 0 || row >= SIZE || col >= SIZE || !board[row][col].equals(EMPTY)) {
            return false; // 이미 돌이 놓여있거나 범위를 벗어나면 착수 불가
        }

        // 돌 놓기
        board[row][col] = color;

        // 승리 조건 체크
        return checkWin(row, col, color);
    }

    // 승리 조건 확인 메서드
    private boolean checkWin(int row, int col, String color) {
        return checkDirection(row, col, color, 1, 0) ||  // 가로
               checkDirection(row, col, color, 0, 1) ||  // 세로
               checkDirection(row, col, color, 1, 1) ||  // 대각선 (왼쪽 위 -> 오른쪽 아래)
               checkDirection(row, col, color, 1, -1);   // 대각선 (왼쪽 아래 -> 오른쪽 위)
    }

    // 특정 방향(가로, 세로, 대각선)으로 5개의 돌이 연속인지 확인
    private boolean checkDirection(int row, int col, String color, int rowDir, int colDir) {
        int count = 1; // 놓은 돌 자체를 포함해서 시작

        // 한 방향으로 연속 돌 체크
        for (int i = 1; i < 5; i++) {
            int r = row + rowDir * i;
            int c = col + colDir * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !board[r][c].equals(color)) {
                break;
            }
            count++;
        }

        // 반대 방향으로 연속 돌 체크
        for (int i = 1; i < 5; i++) {
            int r = row - rowDir * i;
            int c = col - colDir * i;
            if (r < 0 || c < 0 || r >= SIZE || c >= SIZE || !board[r][c].equals(color)) {
                break;
            }
            count++;
        }

        return count >= 5; // 연속해서 5개의 돌이 놓였으면 승리
    }

    // 보드 상태를 출력하는 메서드 (디버그용)
    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j].substring(0, 1) + " "); // "black", "white"에서 첫 문자만 출력
            }
            System.out.println();
        }
    }

    // 보드 상태 가져오기
    public String[][] getBoard() {
        return board;
    }
}

