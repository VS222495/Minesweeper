package com.sikora.minessweepercik;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private static final int ROWS = 15;
    private static final int COLS = 15;
    private static final int MINES = 40;

    private GameLogic game;
    private Button[][] buttons;

    private Label minesLabel;
    private Label flagsLabel;

    @Override
    public void start(Stage stage) {
        game = new GameLogic(ROWS, COLS, MINES);
        buttons = new Button[ROWS][COLS];

        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #bdbdbd;");

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Button btn = createButton(r, c);
                buttons[r][c] = btn;
                grid.add(btn, c, r);
            }
        }

        VBox root = new VBox(10, createTopBar(), grid);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #e0e0e0;");

        stage.setScene(new Scene(root));
        stage.setTitle("Minesweeper 15×15");
        stage.show();

        updateTopBar();
    }

    private HBox createTopBar() {
        minesLabel = new Label();
        flagsLabel = new Label();

        minesLabel.setFont(Font.font(16));
        flagsLabel.setFont(Font.font(16));

        Button reset = new Button("Start New");
        reset.setFont(Font.font(20));
        reset.setOnAction(e -> restart());

        HBox bar = new HBox(30, minesLabel, reset, flagsLabel);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(8));
        bar.setStyle(
                "-fx-background-color: #cfcfcf;" +
                        "-fx-border-color: #9e9e9e;" +
                        "-fx-border-width: 2;"
        );

        return bar;
    }

    private void updateTopBar() {
        minesLabel.setText("💣 Mines: " + game.getMinesCount());
        flagsLabel.setText("🚩 Flags: " +
                (game.getMinesCount() - game.getFlagsUsed()));
    }

    private Button createButton(int r, int c) {
        Button btn = new Button();
        btn.setPrefSize(40, 40);
        btn.setFont(Font.font(15));
        setHiddenStyle(btn);

        btn.setOnMouseClicked(e -> {
            switch (e.getButton()) {
                case PRIMARY -> game.reveal(r, c);
                case SECONDARY -> game.toggleFlag(r, c);
            }
            updateBoard();
            updateTopBar();
            checkEnd();
        });

        return btn;
    }

    private void updateBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Button btn = buttons[r][c];

                if (game.isRevealed(r, c)) {
                    revealStyle(btn, r, c);
                } else {
                    setHiddenStyle(btn);
                    if (game.isFlagged(r, c)) {
                        btn.setText("🚩");
                    }
                }
            }
        }
    }

    private void setHiddenStyle(Button btn) {
        btn.setDisable(false);
        btn.setText("");
        btn.setStyle(
                "-fx-background-color: linear-gradient(#f5f5f5, #cfcfcf);" +
                        "-fx-border-color: #9e9e9e;" +
                        "-fx-border-width: 2;"
        );
    }

    private void revealStyle(Button btn, int r, int c) {
        btn.setDisable(true);
        btn.setStyle(
                "-fx-background-color: #eeeeee;" +
                        "-fx-border-color: #b0b0b0;"
        );

        if (game.hasMine(r, c)) {
            btn.setText("💣");
        } else {
            int n = game.getNumber(r, c);
            if (n > 0) {
                btn.setText(String.valueOf(n));
                btn.setTextFill(getNumberColor(n));
            }
        }
    }

    private Color getNumberColor(int n) {
        return switch (n) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.DARKBLUE;
            case 5 -> Color.DARKRED;
            case 6 -> Color.TEAL;
            default -> Color.BLACK;
        };
    }

    private void checkEnd() {
        if (game.isGameOver()) {
            game.revealAllMines();
            updateBoard();
            showAlert("Game Over", "💥 Šlápl jsi na minu!");
        } else if (game.isWin()) {
            showAlert("Výhra", "🎉 Vyhrál jsi!");
        }
    }

    private void restart() {
        game = new GameLogic(ROWS, COLS, MINES);
        updateBoard();
        updateTopBar();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }
}