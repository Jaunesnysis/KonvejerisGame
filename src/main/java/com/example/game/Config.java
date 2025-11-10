package com.example.game;

public final class Config {
    public static final int WIDTH = 900, HEIGHT = 600;

    public static final int LIVES_START = 3;
    public static final int SCORE_TO_WIN = 20000;
    public static final int CONTAINERS = 3;

    public static final int BOX_SIZE = 36;

    public static final int LEVEL_UP_EVERY_CORRECT = 10;
    public static final int BASE_SCORE_PER_HIT = 100;
    public static final int COMBO_CAP = 10;
    public static final int ERROR_SERIES_PENALTY = 50;

    public static final double BASE_SPEED = 120;
    public static final double BASE_FALL_SPEED = 180;
    public static final double SPEED_INC_PER_LEVEL = 25;

    public static final double BASE_SPAWN_INTERVAL = 1.2;
    public static final double SPAWN_DEC_PER_LEVEL = 0.12;

    public static final double SPLITTER_X = WIDTH * 0.55;
    public static final double CONVEYOR_Y = HEIGHT * 0.25;

    // --- unified container layout (used by BOTH Renderer and GameState)
    public static final double LANE_GAP = 40.0;        // space between lanes
    public static final double START_MARGIN = 20.0;    // left margin after splitter
    public static final double RIGHT_MARGIN = 40.0;    // right margin at screen edge
    public static final double CONTAINERS_Y = HEIGHT * 0.85;

    /** total horizontal space available for lanes (to the right of splitter) */
    public static double lanesTotalWidth() {
        return WIDTH - SPLITTER_X - RIGHT_MARGIN;
    }

    /** width of a single lane rectangle */
    public static double laneWidth() {
        return (lanesTotalWidth() - LANE_GAP * (CONTAINERS - 1)) / CONTAINERS;
    }

    /** left X of a lane (1..CONTAINERS) */
    public static double laneX(int lane) {
        double w = laneWidth();
        return SPLITTER_X + START_MARGIN + (lane - 1) * (w + LANE_GAP);
    }

    /** center X of a lane (used for the falling target) */
    public static double laneCenterX(int lane) {
        return laneX(lane) + laneWidth() / 2.0;
    }

    private Config() {}
}
