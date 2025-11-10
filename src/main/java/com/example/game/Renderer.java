package com.example.game;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import static com.example.game.Config.*;

public class Renderer {
    private final GraphicsContext g;
    public Renderer(GraphicsContext g) { this.g = g; }

    public void render(GameState s) {
        // background
        g.setFill(Color.rgb(18, 22, 28));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        drawHUD(s);
        drawConveyor();
        drawSplitter(s.splitterPosition);
        drawContainers();

        for (Box b : s.boxes) drawBox(b);

        if (s.gameOver) drawGameOver(s);
    }

    private void drawHUD(GameState s) {
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 18));
        g.setTextAlign(TextAlignment.LEFT);
        g.setTextBaseline(VPos.TOP);
        String comboTxt = s.correctInRow > 1 ? " x" + String.format("%.1f", (1.0 + 0.1 * (s.correctInRow - 1))) : " x1.0";
        g.fillText("Taškai: " + s.score + "   Gyvybės: " + s.lives + "   Lygis: " + s.level + "   Kombo: " + s.correctInRow + comboTxt, 16, 12);
        g.setTextAlign(TextAlignment.RIGHT);
        g.fillText("Valdymas: [1][2][3] – sklendė • Tikslas: " + SCORE_TO_WIN + " taškų", WIDTH - 16, 12);
    }

    private void drawConveyor() {
        g.setStroke(Color.GRAY);
        g.setLineWidth(8);
        g.strokeLine(0, CONVEYOR_Y, SPLITTER_X, CONVEYOR_Y);
    }

    private void drawSplitter(int pos) {
        g.setFill(Color.DARKSLATEGRAY);
        g.fillRect(SPLITTER_X - 8, CONVEYOR_Y - 50, 16, 100);

        double angle = pos == 1 ? Math.toRadians(120) : (pos == 2 ? Math.toRadians(90) : Math.toRadians(60));
        double len = 60;
        double x2 = SPLITTER_X + Math.cos(angle) * len;
        double y2 = CONVEYOR_Y + Math.sin(angle) * len;

        g.setStroke(Color.YELLOW);
        g.setLineWidth(4);
        g.strokeLine(SPLITTER_X, CONVEYOR_Y, x2, y2);
    }

    private void drawContainers() {
        double y = CONTAINERS_Y;
        double w = Config.laneWidth();

        for (int i = 1; i <= CONTAINERS; i++) {
            double x = Config.laneX(i);
            Color c = laneColor(i);

            g.setFill(c.deriveColor(0, 1, 1, 0.15));
            g.fillRoundRect(x, y - 60, w, 80, 12, 12);

            g.setStroke(c);
            g.setLineWidth(3);
            g.strokeRoundRect(x, y - 60, w, 80, 12, 12);

            g.setFill(Color.WHITE);
            g.setFont(Font.font("Arial", 20));
            g.setTextAlign(TextAlignment.CENTER);
            g.setTextBaseline(VPos.CENTER);
            g.fillText(String.valueOf((char) ('A' + (i - 1))), x + w / 2, y - 20);
        }
    }

    private void drawBox(Box b) {
        g.setFill(typeColor(b.type));
        g.fillRoundRect(b.x - BOX_SIZE / 2.0, b.y - BOX_SIZE / 2.0, BOX_SIZE, BOX_SIZE, 8, 8);
        g.setStroke(Color.BLACK);
        g.setLineWidth(2);
        g.strokeRoundRect(b.x - BOX_SIZE / 2.0, b.y - BOX_SIZE / 2.0, BOX_SIZE, BOX_SIZE, 8, 8);
        g.setFill(Color.BLACK);
        g.setFont(Font.font("Arial", 18));
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.CENTER);
        g.fillText(String.valueOf(b.type), b.x, b.y + 1);
    }

    private void drawGameOver(GameState s) {
        g.setFill(new Color(0, 0, 0, 0.55));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setTextAlign(TextAlignment.CENTER);
        g.setTextBaseline(VPos.CENTER);
        g.setFill(s.win ? Color.LIME : Color.ORANGERED);
        g.setFont(Font.font("Arial", 48));
        g.fillText(s.win ? "PERGALĖ!" : "PRALAIMĖTA", WIDTH / 2.0, HEIGHT / 2.0 - 80);
        g.setFill(Color.WHITE);
        g.setFont(Font.font("Arial", 22));
        g.fillText("Rezultatų santrauka", WIDTH / 2.0, HEIGHT / 2.0 - 30);
        String summary = String.format("Taškai: %d • Teisingai: %d • Kombo: %d • Lygis: %d", s.score, s.totalCorrect, s.maxComboShown, s.level);
        g.fillText(summary, WIDTH / 2.0, HEIGHT / 2.0 + 10);
        g.setFont(Font.font("Arial", 18));
        g.fillText("Spausk SPACE, kad pradėtum iš naujo", WIDTH / 2.0, HEIGHT / 2.0 + 60);
    }

    private static Color typeColor(char t) {
        return t == 'A' ? Color.CORNFLOWERBLUE : t == 'B' ? Color.PALEVIOLETRED : Color.MEDIUMSEAGREEN;
    }

    private static Color laneColor(int lane) {
        return lane == 1 ? Color.CORNFLOWERBLUE : lane == 2 ? Color.PALEVIOLETRED : Color.MEDIUMSEAGREEN;
    }
}
