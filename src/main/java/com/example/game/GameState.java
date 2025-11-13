package com.example.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.game.Config.*;

public class GameState {
    public final Random rng = new Random();
    public final List<Box> boxes = new ArrayList<>();

    public int lives = LIVES_START, score = 0, level = 1;
    public int splitterPosition = 1;    // 1..3
    public int correctInRow = 0, errorInRow = 0, totalCorrect = 0, maxComboShown = 0;

    public boolean gameOver = false, win = false;

    private double timeSinceSpawn = 0;

    public void reset() {
        boxes.clear();
        timeSinceSpawn = 0;
        lives = LIVES_START;
        score = 0;
        level = 1;
        splitterPosition = 1;
        correctInRow = 0;
        errorInRow = 0;
        totalCorrect = 0;
        maxComboShown = 0;
        gameOver = false;
        win = false;
    }

    public void update(double dt) {
        // spawn timer
        timeSinceSpawn += dt;
        double spawnInterval = Math.max(0.35, BASE_SPAWN_INTERVAL - (level - 1) * SPAWN_DEC_PER_LEVEL);
        if (timeSinceSpawn >= spawnInterval) {
            spawnBox();
            timeSinceSpawn = 0;
        }

        // speeds
        double hSpeed = BASE_SPEED + (level - 1) * SPEED_INC_PER_LEVEL;
        double vSpeed = BASE_FALL_SPEED + (level - 1) * SPEED_INC_PER_LEVEL;

        // move & resolve
        List<Box> remove = new ArrayList<>();
        for (Box b : boxes) {
            if (b.state == BoxState.TO_SPLITTER) {
                b.x += hSpeed * dt;
                if (b.x >= SPLITTER_X) {
                    b.x = SPLITTER_X;
                    
                    b.assignedLane = splitterPosition;
                    b.targetX = Config.laneCenterX(b.assignedLane);
                    // record fall start to compute a perfect straight line
                    b.fallStartX = b.x;
                    b.fallStartY = b.y;
                    b.state = BoxState.FALLING;
                }
            } else { // cia krentant jau
                double targetY = Config.CONTAINERS_Y;

                //cia zemyn
                b.y += vSpeed * dt;

                // splitter iki boxes
                double totalFall = targetY - b.fallStartY;
                double t = totalFall <= 0 ? 1.0 : (b.y - b.fallStartY) / totalFall;
                if (t < 0) t = 0;
                if (t > 1) t = 1;

                // staraight linija
                b.x = b.fallStartX + (b.targetX - b.fallStartX) * t;

                // landed?
                if (b.y + BOX_SIZE / 2.0 >= targetY) {
                    boolean ok = (b.type == ('A' + (b.assignedLane - 1)));
                    if (ok) {
                        correctInRow = Math.min(COMBO_CAP, correctInRow + 1);
                        maxComboShown = Math.max(maxComboShown, correctInRow);
                        errorInRow = 0;
                        int gain = (int) Math.round(BASE_SCORE_PER_HIT * (1.0 + 0.1 * (correctInRow - 1)));
                        score += gain;
                        totalCorrect++;
                        if (totalCorrect % LEVEL_UP_EVERY_CORRECT == 0) level++;
                    } else {
                        lives--;
                        errorInRow++;
                        correctInRow = 0;
                        score = Math.max(0, score - ERROR_SERIES_PENALTY * errorInRow);
                    }
                    remove.add(b);
                }
            }
        }
        boxes.removeAll(remove);

        // end conditions
        if (lives <= 0) { gameOver = true; win = false; }
        else if (score >= SCORE_TO_WIN) { gameOver = true; win = true; }
    }

    // spawn boxes
    private void spawnBox() {
        boxes.add(new Box(-BOX_SIZE / 2.0, CONVEYOR_Y, randomType()));
    }

    private char randomType() {
        return (char) ('A' + rng.nextInt(CONTAINERS)); // A/B/C
    }
}
