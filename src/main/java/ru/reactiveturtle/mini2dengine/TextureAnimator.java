package ru.reactiveturtle.mini2dengine;

import android.graphics.Bitmap;

public class TextureAnimator {
    private Bitmap[] bitmaps = new Bitmap[0];
    private float frame = 0;
    private float frameRate = 1;

    private AnimationListener animationListener;

    public TextureAnimator(Texture... textures) {
        setFrames(textures);
    }

    public void setFrame(Texture texture, int position) {
        bitmaps[position].recycle();
        bitmaps[position] = texture.getBitmap();
    }

    public void setFrames(Texture... textures) {
        frame = 0;
        for (Bitmap bitmap : bitmaps) {
            bitmap.recycle();
        }
        bitmaps = new Bitmap[textures.length];
        for (int i = 0; i < textures.length; i++) {
            bitmaps[i] = textures[i].getBitmap();
        }
    }

    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
    }

    Bitmap getFrame() {
        return bitmaps[(int) frame];
    }

    void newFrame(long lastTime) {
        if (frame == 0) {
            if (animationListener != null) {
                animationListener.onStartAnimation();
            }
        }
        int lastFrame = (int) frame;
        frame += (System.currentTimeMillis() - lastTime) / 1000f * frameRate;
        if ((int) frame > lastFrame) {
            if (animationListener != null) {
                animationListener.onFrame((int) frame);
            }
        }
        if (frame >= bitmaps.length) {
            frame = frame % bitmaps.length < 0.00001f ? 0 : frame % bitmaps.length;
            if (animationListener != null) {
                animationListener.onEndAnimation();
            }
        }
    }

    public void destroy() {
        for (Bitmap bitmap : bitmaps) {
            bitmap.recycle();
        }
        bitmaps = null;
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public interface AnimationListener {
        void onStartAnimation();

        void onFrame(int frame);

        void onEndAnimation();
    }
}
