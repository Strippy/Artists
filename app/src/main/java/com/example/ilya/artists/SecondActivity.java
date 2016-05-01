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
            LoadImg db = new LoadImg(art, this);
            db.execute();
            try {
                Bitmap _bmp = db.get();

                ImageView iv = (ImageView) findViewById(R.id.imageView2);
                if (_bmp == null)
                    iv.setImageDrawable(getDrawable(R.drawable.nointernet));
                else
                    iv.setImageBitmap(_bmp);
            }
        catch (Exception e){e.printStackTrace();}

        }catch ( NullPointerException exc)
        {
            exc.printStackTrace();
            this.finish();
        }

    }

}
