package ru.reactiveturtle.mini2dengine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    private Game game;
    private boolean render = true;
    private DisplayMetrics dm;

    public GameView(Context activityContext, Game game) {
        super(activityContext);
        this.game = game;
        game.setGameView(this);

        Activity activity = ((Activity) activityContext);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        setLayoutParams(new WindowManager.LayoutParams(dm.widthPixels, dm.heightPixels));
        game.start();
        game.isRender = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (render) {
            game.setCanvas(canvas);
            try {
                game.renderStart();
                game.render();
                game.renderEnd();
                invalidate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.onTouch(event);
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (game != null) game.destroy();
        render = false;
    }

    public void setOrientation(int orientation) {
        ((Activity) getContext()).setRequestedOrientation(orientation);
    }

    int width() {
        return dm.widthPixels;
    }

    int height() {
        return dm.heightPixels;
    }
}
