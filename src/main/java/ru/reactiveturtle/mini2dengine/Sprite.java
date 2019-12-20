package ru.reactiveturtle.mini2dengine;

import android.graphics.Bitmap;

public class Sprite extends PhysicalObject{
    private float width, height;
    private TextureAnimator animator;
    private Type type = Type.SOLID;
    private int refWidth, refHeight;
    private boolean isFlip = false;

    public Sprite(float width, float height) {
        init(width, height);
    }

    private void init(float width, float height) {
        this.width = width;
        this.height = height;
        setTexture(new Texture(Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565)));
    }

    public void makeElastic(int refWidth, int refHeight) {
        type = Type.ELASTIC;
        this.refWidth = refWidth;
        this.refHeight = refHeight;
    }

    public void makeSolid() {
        type = Type.SOLID;
        refWidth = 0;
        refHeight = 0;
    }

    public Type getType() {
        return type;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setTexture(Texture texture) {
        animator = new TextureAnimator(texture);
    }

    public void setAnimator(TextureAnimator animator) {
        this.animator = animator;
    }

    public void setFlip(boolean flip) {
        isFlip = flip;
    }

    public boolean isFlip() {
        return isFlip;
    }

    Bitmap getFrame() {
        return animator.getFrame();
    }

    int getRefWidth() {
        return refWidth;
    }

    int getRefHeight() {
        return refHeight;
    }

    void newFrame(long lastTime) {
        animator.newFrame(lastTime);
    }

    public enum Type {
        SOLID, ELASTIC
    }
}
