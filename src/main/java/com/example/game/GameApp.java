package com.example.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static com.example.game.Config.*;

public class GameApp extends Application {
    private final GameState state = new GameState();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext g = canvas.getGraphicsContext2D();
        Renderer renderer = new Renderer(g);

        Scene scene = new Scene(new javafx.scene.Group(canvas));
        stage.setTitle("Rūšiuotojas");
        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(e -> {
            if (state.gameOver && e.getCode() == KeyCode.SPACE) { state.reset(); return; }
            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1) state.splitterPosition = 1;
            else if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2) state.splitterPosition = 2;
            else if (e.getCode() == KeyCode.DIGIT3 || e.getCode() == KeyCode.NUMPAD3) state.splitterPosition = 3;
        });

        final long[] last = {System.nanoTime()};
        new AnimationTimer() {
            @Override public void handle(long now) {
                double dt = (now - last[0]) / 1_000_000_000.0;
                last[0] = now;
                if (!state.gameOver) state.update(dt);
                renderer.render(state);
            }
        }.start();
    }

    public static void main(String[] args) { launch(args); }
}
