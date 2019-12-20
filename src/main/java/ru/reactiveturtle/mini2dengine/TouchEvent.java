package ru.reactiveturtle.mini2dengine;

public class TouchEvent {
    public static final int ACTION_DOWN             = 0;
    public static final int ACTION_UP               = 1;
    public static final int ACTION_MOVE             = 2;

    private int x, y, rawX, rawY, action;

    TouchEvent(int x, int y, int rawX, int rawY) {
        this.x = x;
        this.y = y;
        this.rawX = rawX;
        this.rawY = rawY;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRawX() {
        return rawX;
    }

    public int getRawY() {
        return rawY;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
