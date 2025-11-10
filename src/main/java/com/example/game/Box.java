package com.example.game;

public class Box {
    public double x, y;
    public double targetX;
    public double fallStartX, fallStartY;
    public char type;               // 'A', 'B', 'C'
    public BoxState state = BoxState.TO_SPLITTER;
    public int assignedLane = 0;    // 1..3

    public Box(double x, double y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
