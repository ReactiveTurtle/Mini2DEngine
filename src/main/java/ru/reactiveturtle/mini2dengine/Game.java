package ru.reactiveturtle.mini2dengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Game {
    private Paint paint;

    private GameView gameView;

    private Canvas canvas;

    private Camera camera;

    private List<Button> buttons = new ArrayList<>();
    private List<Integer> pressedButtons = new ArrayList<>();
    private Bitmap interfaceBitmap;
    private Canvas interfaceCanvas;
    private float height = 10f;

    private long lastRenderTime;
    private int time = 0;
    private int iterations = 0, last = 0;

    boolean isRender = false;

    public Game() {
        lastRenderTime = System.currentTimeMillis();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(64);
        paint.setColor(Color.WHITE);
        interfaceBitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
    }

    protected abstract void start();

    protected abstract void render() throws IOException;

    protected abstract void destroy();

    void renderStart() {
        canvas.drawColor(Color.BLACK);
    }

    void renderEnd() {
        canvas.drawBitmap(interfaceBitmap, 0, 0, paint);
        iterations++;
        time += System.currentTimeMillis() - lastRenderTime;
        lastRenderTime = System.currentTimeMillis();
        if (time >= 1000) {
            time = 0;
            last = iterations - 1;
            iterations = 0;
        }
        canvas.drawText(last + "", 30, 80, paint);
    }

    void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    void onTouch(MotionEvent event) {
        System.out.println("Pointer count: " + event.getPointerCount() + ", Action index: " + event.getActionIndex());
        for (int j = 0; j < buttons.size(); j++) {
            Button button = buttons.get(j);
            if (event.getX(event.getActionIndex()) >= button.getX() &&
                    event.getX(event.getActionIndex()) <= button.getX() + button.getWidth() &&
                    event.getY(event.getActionIndex()) >= button.getY() &&
                    event.getY(event.getActionIndex()) <= button.getY() + button.getHeight()) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        pressedButtons.add(j);
                        break;
                }
                break;
            }
        }
        for (int j = 0; j < pressedButtons.size(); j++) {
            Button button = buttons.get(pressedButtons.get(j));
            if (event.getActionIndex() == j) {
                TouchEvent touchEvent = new TouchEvent((int) (event.getX(event.getActionIndex()) - button.getX()),
                        (int) (event.getY(event.getActionIndex()) - button.getY()),
                        (int) event.getX(event.getActionIndex()), (int) event.getY(event.getActionIndex()));
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent.setAction(TouchEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvent.setAction(TouchEvent.ACTION_MOVE);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        pressedButtons.remove(event.getActionIndex());
                        touchEvent.setAction(TouchEvent.ACTION_UP);
                        break;
                }
                button.touch(touchEvent);
                break;
            }
        }
        System.out.println("---------------------------------------------");
    }

    protected void addControllers(Button... buttons) {
        for (Button button : buttons) {
            addController(button);
        }
    }

    protected void addController(Button button) {
        if (interfaceCanvas == null) {
            interfaceBitmap = Bitmap.createBitmap((int) screenWidth(), (int) screenHeight(), Bitmap.Config.ARGB_8888);
            interfaceCanvas = new Canvas(interfaceBitmap);
        }

        buttons.add(button);
        if (!isRender)
            interfaceCanvas.drawBitmap(button.getBitmap(), button.getX(), button.getY(), paint);
        else {
            interfaceBitmap.eraseColor(Color.TRANSPARENT);
            for (Button b : buttons) {
                interfaceCanvas.drawBitmap(b.getBitmap(), b.getX(), b.getY(), paint);
            }
        }
    }

    protected void drawBack(float red, float green, float blue) {
        canvas.drawColor(Color.rgb((int) (red * 255), (int) (green * 255), (int) (blue * 255)));
    }

    protected void draw(Sprite sprite) {
        if (camera != null) {
            Bitmap frame = sprite.getFrame();
            float dstWidth = (int) (getRealX(sprite.getWidth()) / camera.getScaleX() * sprite.getScaleX() *
                                (sprite.getType() == Sprite.Type.ELASTIC ? (float) frame.getWidth() / sprite.getRefWidth() : 1f));
            float dstHeight = (int) (getRealY(sprite.getHeight()) / camera.getScaleY() * sprite.getScaleY() *
                                (sprite.getType() == Sprite.Type.ELASTIC ? (float) frame.getHeight() / sprite.getRefHeight() : 1f));


            float screenX = (int) ((screenWidth() - screenWidth() / camera.getScaleX()) / 2f + (getRealX(sprite.getX()) - camera.getX()) / camera.getScaleX());
            float screenY = (int) (screenHeight() - dstHeight - (screenHeight() - screenHeight() / camera.getScaleY()) / 2f - (getRealY(sprite.getY()) - camera.getY()) / camera.getScaleY());
            int visibleStartX = (int) Math.max(0, screenX);
            int visibleEndX = (int) Math.min(screenWidth(), screenX + dstWidth);
            int visibleWidth = visibleEndX - visibleStartX;
            int visibleStartY = (int) Math.max(0, screenY);
            int visibleEndY = (int) Math.min(screenHeight(), screenY + dstHeight);
            int visibleHeight = visibleEndY - visibleStartY;
            if ((int) (frame.getWidth() * visibleWidth / dstWidth) > 0 &&
                    (int) (frame.getHeight() * visibleHeight / dstHeight) > 0) {
                System.out.println(visibleStartX + ", " + visibleEndX + ", " + visibleWidth);
                Bitmap buffer = Bitmap.createBitmap(frame, (int) (frame.getWidth() * (visibleStartX - screenX) / dstWidth),
                        (int) (frame.getHeight() * (visibleStartY - screenY) / dstHeight),
                        (int) (frame.getWidth() * visibleWidth / dstWidth),
                        (int) (frame.getHeight() * visibleHeight / dstHeight));
                Bitmap bitmap = Bitmap.createScaledBitmap(buffer, visibleWidth, visibleHeight, false);
                buffer.recycle();

                Matrix matrix = new Matrix();
                matrix.setTranslate(visibleStartX, visibleStartY);
                if (sprite.isFlip())
                    matrix.preScale(-1f, 1f, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
                canvas.drawBitmap(bitmap, matrix, paint);
            }
            sprite.newFrame(lastRenderTime);
        }
    }

    protected void setWidth(float meters) {
        if (meters <= 0) {
            throw new IllegalArgumentException("Argument must be > 0");
        }
        this.height = meters / aspectRatio();
    }

    protected void setHeight(float meters) {
        if (meters <= 0) {
            throw new IllegalArgumentException("Argument must be > 0");
        }
        this.height = meters;
    }

    protected float getWidth() {
        return height * aspectRatio();
    }

    protected float getHeight() {
        return height;
    }

    protected AssetManager getAssets() {
        return gameView.getContext().getAssets();
    }

    protected int screenWidth() {
        return gameView.width();
    }

    protected int screenHeight() {
        return gameView.height();
    }

    protected float aspectRatio() {
        return (float) screenWidth() / screenHeight();
    }

    protected void setCamera(Camera camera) {
        this.camera = camera;
    }

    public long getLastRenderTime() {
        return lastRenderTime;
    }

    private int getRealX(float x) {
        return (int) (x * screenWidth() / (getWidth()));
    }

    private int getRealY(float y) {
        return (int) (y * screenHeight() / height);
    }
}
