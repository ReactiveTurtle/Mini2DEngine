package ru.reactiveturtle.mini2dengine;

import android.graphics.Bitmap;

public class Button extends GameObject {
    private String id = "button";
    private OnTouchListener onTouchListener;
    private Bitmap bitmap;
    private int width, height;

    public Button(int width, int height) {
        init(width, height, 0, 0);
    }

    public Button(int width, int height, float x, float y) {
        init(width, height, x, y);
        setPosition(x, y);
    }

    public Button(int width, int height, float x, float y, Texture texture) {
        init(width, height, x, y);
        setTexture(texture);
    }

    private void init(int width, int height, float x, float y) {
        this.width = width;
        this.height = height;
        setPosition(x, y);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTexture(Texture texture) {
        bitmap = Bitmap.createScaledBitmap(texture.getBitmap(), width, height, false);
    }

    Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    void touch(TouchEvent event) {
        if (onTouchListener != null) {
            onTouchListener.onTouch(event);
        }
    }

    public interface OnTouchListener {
        void onTouch(TouchEvent event);
    }
}
