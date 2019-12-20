package ru.reactiveturtle.mini2dengine;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class Loader {
    public static Bitmap getAssetBitmap(AssetManager assetManager, String asset) {
        try {
            InputStream inputStream = assetManager.open(asset);
            Bitmap buffer = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
    }
}
