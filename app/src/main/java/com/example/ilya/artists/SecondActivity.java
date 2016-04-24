package com.example.ilya.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.artist_detales_activity);
        TextView tv = (TextView) findViewById(R.id.textView);
        TextView bio = (TextView) findViewById(R.id.biographyView);
        TextView link = (TextView) findViewById(R.id.link);
        Bundle b = getIntent().getExtras();
        String s = b.getString("artistInfo");
        try {
            Artist art = JSONParser.GetArtistFromJSON(s);
            bio.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            bio.setTextColor(Color.BLACK);
            bio.setText(getString(R.string.Biography));
            String value = art.getDescription();
            tv.setText(value);
            link.setText(art.getLink());
            this.setTitle(art.getName());
            DrawBig db = new DrawBig(art, this);
            db.execute();
        }catch ( NullPointerException exc)
        {
            exc.printStackTrace();
            this.finish();
        }
    }

    //асинхронная загрузка картинки
    private class DrawBig extends AsyncTask<Void, Integer, Void>{
        private URL _url;
        private Bitmap _bmp;
        private Artist _artist;
        private Context _activity;

        public  DrawBig(Artist artist,Context activity) {
            try {
                _artist = artist;
                _activity = activity;

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params){
            try {
                LoadImage();
            }
            catch (Exception exc){
                exc.printStackTrace();
            }
            return null;
        }

        private void LoadImage() throws IOException {
            Cover cov = _artist.getCover();
            String big = cov.getBig();
            URL url = new URL(big);

            FileCache fCache = new FileCache(SecondActivity.this);
            File f = fCache.GetFile(big);
            Bitmap bitmap = decodeFile(f);
            if (bitmap != null) {
                _bmp = bitmap;
            } else {
                _bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                fCache.CacheBitmap(_bmp, big);
            }
        }

        private Bitmap decodeFile(File f){
            try {
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException exc ){exc.printStackTrace();}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            ImageView iv = (ImageView) findViewById(R.id.imageView2);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SecondActivity.this.finish();
                }
            });
            if(_bmp==null)
                iv.setImageDrawable(_activity.getDrawable(R.drawable.nointernet));
            else
                iv.setImageBitmap(_bmp);
        }
    }
}
