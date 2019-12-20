package ru.reactiveturtle.mini2dengine;

abstract class GameObject implements Cloneable {
    private float x = 0, y = 0;
    private float scaleX = 1, scaleY = 1;
    private boolean isFloat = false;

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    public void setScale(float x, float y) {
        setScaleX(x);
        setScaleY(y);
    }

    public void setScaleX(float x) {
        scaleX = x;
    }

    public void setScaleY(float y) {
        scaleY = y;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void addPosition(float x, float y) {
        addX(x);
        addY(y);
    }

    public void addX(float x) {
        this.x += x;
    }

    public void addY(float y) {
        this.y += y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
