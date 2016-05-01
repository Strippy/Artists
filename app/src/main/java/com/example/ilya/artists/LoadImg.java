package com.example.ilya.artists;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ilya on 26.04.2016.
 */

    //асинхронная загрузка картинки
    public class LoadImg extends AsyncTask<Void, Void, Bitmap> {
    private Bitmap _bmp;
    private Artist _artist;
    private Activity _activity;

    public LoadImg(Artist artist, Activity activity) {
        _artist = artist;
        _activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            LoadImage();
            return _bmp;
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    private void LoadImage() throws IOException {
        Cover cov = _artist.getCover();
        String big = cov.getBig();
        URL url = new URL(big);

        FileCache fCache = new FileCache(_activity);
        File f = fCache.GetFile(big);
        try {
            Bitmap bitmap = decodeFile(f);

            if (bitmap != null) {
                _bmp = bitmap;
            } else {
                _bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                fCache.CacheBitmap(_bmp, big);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException exc) {
            exc.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        super.onPostExecute(result);
    }
}
