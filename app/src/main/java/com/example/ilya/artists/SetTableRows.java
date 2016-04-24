package com.example.ilya.artists;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ilya on 23.04.2016.
 */
//асинхронное динамическое заполнение таблиц
public class SetTableRows extends AsyncTask<Void, Integer, Void> {
    private Bitmap _bmp ;
    private Artist _artist;
    int _max;
    int _now;
    private List<Artist> _artists;
    Activity _mainActivity ;
    public  SetTableRows(Artist artist, int max, int now, Activity mainActivity, List<Artist> artists){
        _artist = artist;
        _max = max;
        _now = now;
        _mainActivity = mainActivity;
        _artists = artists;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Void doInBackground(Void... params) {
        float percentage = (_now * 100) / _max;
        int perc = Math.round(percentage);
        publishProgress(perc);
        try {
            LoadImage();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    private void LoadImage() throws IOException {
        Cover cov = _artist.getCover();
        URL url = new URL(cov.getSmall());

        FileCache fCache= new FileCache(_mainActivity);
        File f = fCache.GetFile(cov.getSmall());
        fCache.CacheArtist(_artist);
        Bitmap bitmap = decodeFile(f);
        if(bitmap!=null)
        {
            _bmp = bitmap;
        }
        else
        {
            _bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            fCache.CacheBitmap(_bmp,cov.getSmall());
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
    protected void onPostExecute(Void result){
        TableLayout mainTable = (TableLayout)_mainActivity.findViewById(R.id.main_table);
        TableRow tr = new TableRow(_mainActivity);
        ImageView img = new ImageView(_mainActivity);
        img.setClickable(true);
        img.setId(_now);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mIntent = new Intent(_mainActivity,SecondActivity.class);
                Bundle b = new Bundle();
                b.putInt("key", v.getId());
                Artist a =  _artists.get(v.getId());
                b.putString("artistInfo", a.toString());
                mIntent.putExtras(b);
                Animation anim = AnimationUtils.loadAnimation(_mainActivity,R.anim.rotate);
                anim.setAnimationListener(new Animation.AnimationListener(){
                    public void onAnimationStart(Animation a){}
                    public void onAnimationRepeat(Animation a){}
                    public void onAnimationEnd(Animation a){
                        _mainActivity.startActivity(mIntent); }
                });
                v.startAnimation(anim);
            }
        });
        TableRow r = getTableRow(mainTable, tr, img);
        mainTable.addView(r);
    }

    private TableRow getTableRow(TableLayout mainTable, TableRow tr, ImageView img) {
        if(_bmp==null)
            img.setImageDrawable(_mainActivity.getDrawable(R.drawable.nointernet));
        else
            img.setImageBitmap(_bmp);
        img.setPadding(0,0,10,0);
        Artist a = _artists.get(_now);
        TextView name = new TextView(_mainActivity);
        name.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        name.setTextColor(Color.BLACK);
        name.setTextAppearance(_mainActivity, android.R.style.TextAppearance_Large);
        name.setText(a.getName());

        TextView ganers = new TextView(_mainActivity);
        ganers.setText(Arrays.toString(a.getGenres()).replace("[","").replace("]",""));
        TextView albums = new TextView(_mainActivity);
        albums.setText(a.getAlbums()+" albums, "+ a.getTracks()+" tracks");

        LinearLayout ll= new LinearLayout(_mainActivity);
        ll.setOrientation(LinearLayout.VERTICAL);

        ll.addView(name);
        ll.addView(ganers);
        ll.addView(albums);
        tr.addView(img);
        tr.addView(ll);
        tr.setPadding(5,5,5,5);
        mainTable.addView(tr, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow r= new TableRow(_mainActivity);
        View v= new View(_mainActivity);
        v.setBackgroundColor(Color.GRAY);

        r.addView(v, TableLayout.LayoutParams.MATCH_PARENT);
        return r;
    }
}