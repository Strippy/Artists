package com.example.ilya.artists;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<Artist> _artists;
    private int lastLoadedId = 0;
    private ListView lvMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artists_activity);
        Button btn = (Button) findViewById(R.id.btn_get_more);
        lvMain = (ListView) findViewById(R.id.list);
        String url = this.getString(R.string.JSON);

        try {
            LoadArtists(url);
            //    TableFill(5);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        View.OnClickListener BtnGetMore = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastLoadedId += 5;

                ListViewLoad(lvMain,0,lastLoadedId);
            }
        };
        btn.setOnClickListener(BtnGetMore);
        lastLoadedId =5;
        ListViewLoad(lvMain,0,lastLoadedId);
    }
    private ArrayAdapter<Artist> ladapter;
    public List<Bitmap> images;
    private void ListViewLoad(ListView lvMain, int from,int to) {
        String g ="";
        if(images==null)images = new ArrayList<>();//getImages(from,to);
        if(ladapter == null)ladapter= new List_Adapter(this, _artists.subList(0, to),images);
        else{
            images =((List_Adapter) ladapter).getImages();
            ladapter = new List_Adapter(this,_artists.subList(0,to),images);
        }
        lvMain.setAdapter(ladapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent mIntent = new Intent(MainActivity.this ,SecondActivity.class);
                Bundle b = new Bundle();
              //  b.putInt("key", v.getId());
                Artist a =  _artists.get(position);
                b.putString("artistInfo", a.toString());
                mIntent.putExtras(b);
                MainActivity.this.startActivity(mIntent);
            }
        });
    }

    private List<Bitmap> getImages(int from,int to) {
        List<Bitmap> images=  new ArrayList<>();
        for(int i=from;i<to;i++) {
            LoadImg limg = new LoadImg(_artists.get(i), this);
            limg.execute();
            try {
                Bitmap bm = limg.get();
                images.add(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return images;
    }

    private void LoadArtists(String url) throws InterruptedException, java.util.concurrent.ExecutionException {
        LoadJSONTask mt = new LoadJSONTask(url);
        mt.execute();
        List<Artist> arn = mt.get();
        FileCache fc = new FileCache(this);
        if(arn != null) {
            _artists = arn;
            fc.ClearArtistsCache();
        }
        else
        {
            _artists = fc.GetArtistsCache();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.SortByABC: {
                Toast.makeText(this, R.string.SortByABC, Toast.LENGTH_SHORT).show();
                SortByABC();
                return true;
            }
            case R.id.SortByID: {
                Toast.makeText(this, R.string.SortByID, Toast.LENGTH_SHORT).show();
                SortByID();
                return true;
            }
            case R.id.ClearCache: {
                Toast.makeText(this, R.string.ClearCache, Toast.LENGTH_SHORT).show();
                ClearCache();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void ClearCache(){
        FileCache fc= new FileCache(this);
        fc.Clear();
        List<Bitmap> images = new ArrayList<>();//getImages(from,to);
        lastLoadedId=5;
        ladapter= new List_Adapter(this, _artists.subList(0, lastLoadedId),images);
    }

    private void  SortByABC(){
        Collections.sort(_artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        lastLoadedId=5;
        List<Bitmap> images = new ArrayList<>();//getImages(from,to);
        ladapter= new List_Adapter(this, _artists.subList(0, lastLoadedId),images);
        ListViewLoad(lvMain,0,lastLoadedId);
       // TableFill(old);
    }

    private void  SortByID(){
        Collections.sort(_artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return lhs.getId().compareTo(rhs.getId());
                // return 0;
            }
        });
        lastLoadedId=5;
        List<Bitmap> images = new ArrayList<>();//getImages(from,to);
        ladapter= new List_Adapter(this, _artists.subList(0, lastLoadedId),images);
        ListViewLoad(lvMain,0,lastLoadedId);
    }


}

