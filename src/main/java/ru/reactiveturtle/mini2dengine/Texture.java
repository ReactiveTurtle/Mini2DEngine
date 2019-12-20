package ru.reactiveturtle.mini2dengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import static ru.reactiveturtle.mini2dengine.Loader.getAssetBitmap;

public class Texture {
    private Bitmap mBitmap;

    Texture(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public Texture(int width, int height) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public Texture(int width, int height, Texture texture, LoopType type) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        switch (type) {
            case SCALE:
                mBitmap = Bitmap.createScaledBitmap(texture.getBitmap(), width, height, false);
                break;
            case LOOP_WIDTH:
                for (int i = 0; i < width / texture.getWidth() + 1; i++) {
                    canvas.drawBitmap(texture.getBitmap(), i * texture.getWidth(), 0, paint);
                }
                break;
            case LOOP_HEIGHT:
                for (int i = 0; i < height / texture.getHeight() + 1; i++) {
                    canvas.drawBitmap(texture.getBitmap(), 0, height - i * texture.getHeight(), paint);
                }
                break;
            case LOOP_WIDTH_HEIGHT:
                for (int i = 0; i < height / texture.getHeight() + 1; i++) {
                    for (int j = 0; j < width / texture.getWidth() + 1; j++) {
                        canvas.drawBitmap(texture.getBitmap(), i * texture.getWidth(),
                                height - i * texture.getHeight(), paint);
                    }
                }
                break;
        }
    }

    public Texture(AssetManager assetManager, String asset) {
        mBitmap = getAssetBitmap(assetManager, asset);
    }

    public Texture(AssetManager assetManager, String asset, float degrees) {
        Bitmap buffer = getAssetBitmap(assetManager, asset);
        mBitmap = Bitmap.createBitmap(buffer.getWidth(), buffer.getHeight(), buffer.getConfig());
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, buffer.getWidth() / 2, buffer.getHeight() / 2);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(buffer, matrix, new Paint(Paint.ANTI_ALIAS_FLAG));
        buffer.recycle();
    }

    public void addTexture(AssetManager assetManager, String asset, int left, int top) {
        addTexture(getAssetBitmap(assetManager, asset), left, top);
    }

    public void addTexture(Texture texture, int left, int top) {
        addTexture(texture.getBitmap(), left, top);
    }

    private void addTexture(Bitmap bitmap, int left, int top) {
        Canvas canvas = new Canvas(mBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(bitmap, left, top, paint);
    }

    public void setTexture(AssetManager assetManager, String asset, int left, int top, int width, int height) {
        Bitmap buffer = getAssetBitmap(assetManager, asset);
        mBitmap = Bitmap.createBitmap(buffer, left, top, width, height);
        buffer.recycle();
    }

    public Texture getTexturePart(int left, int top, int width, int height) {
        return new Texture(Bitmap.createBitmap(mBitmap, left, top, width, height));
    }

    public int getWidth() {
        return mBitmap.getWidth();
    }

    public int getHeight() {
        return mBitmap.getHeight();
    }

    public void scale(float scale) {
        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (mBitmap.getWidth() * scale), (int) (mBitmap.getHeight() * scale), false);
    }

    Bitmap getBitmap() {
        return mBitmap;
    }

    public enum LoopType {
        SCALE, LOOP_WIDTH, LOOP_HEIGHT, LOOP_WIDTH_HEIGHT
    }
}
