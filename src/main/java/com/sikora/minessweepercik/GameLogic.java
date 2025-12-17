package com.sikora.minessweepercik;

import java.util.Random;

public class GameLogic {

    private int rows;
    private int cols;
    private int minesCount;

    private boolean[][] mines;
    private boolean[][] revealed;
    private boolean[][] flagged;
    private int[][] numbers;

    private boolean gameOver = false;
    private boolean firstClick = true;
    private int revealedCount = 0;
    private int flagsUsed = 0;

    public GameLogic(int rows, int cols, int minesCount) {
        this.rows = rows;
        this.cols = cols;
        this.minesCount = minesCount;

        mines = new boolean[rows][cols];
        revealed = new boolean[rows][cols];
        flagged = new boolean[rows][cols];
        numbers = new int[rows][cols];
    }

    public void reveal(int r, int c) {
        if (gameOver) return;
        if (revealed[r][c] || flagged[r][c]) return;

        if (firstClick) {
            placeMines(r, c);
            countNumbers();
            firstClick = false;
        }

        revealed[r][c] = true;
        revealedCount++;

        if (mines[r][c]) {
            gameOver = true;
            return;
        }

        if (numbers[r][c] == 0) {
            revealAround(r, c);
        }
    }

    private void placeMines(int safeRow, int safeCol) {
        Random random = new Random();
        int placed = 0;

        while (placed < minesCount) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            if ((r == safeRow && c == safeCol) || mines[r][c]) continue;

            mines[r][c] = true;
            placed++;
        }
    }

    private void countNumbers() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int count = 0;

                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;

                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                            if (mines[nr][nc]) count++;
                        }
                    }
                }
                numbers[r][c] = count;
            }
        }
    }

    private void revealAround(int r, int c) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = r + dr;
                int nc = c + dc;

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (!revealed[nr][nc]) {
                        reveal(nr, nc);
                    }
                }
            }
        }
    }

    public void toggleFlag(int r, int c) {
        if (revealed[r][c]) return;

        if (flagged[r][c]) {
            flagged[r][c] = false;
            flagsUsed--;
        } else {
            if (flagsUsed < minesCount) {
                flagged[r][c] = true;
                flagsUsed++;
            }
        }
    }

    public boolean isWin() {
        return revealedCount == rows * cols - minesCount;
    }

    public void revealAllMines() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mines[r][c]) {
                    revealed[r][c] = true;
                }
            }
        }
    }

    public boolean hasMine(int r, int c) {
        return mines[r][c];
    }

    public boolean isRevealed(int r, int c) {
        return revealed[r][c];
    }

    public boolean isFlagged(int r, int c) {
        return flagged[r][c];
    }

    public int getNumber(int r, int c) {
        return numbers[r][c];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public int getFlagsUsed() {
        return flagsUsed;
    }
}