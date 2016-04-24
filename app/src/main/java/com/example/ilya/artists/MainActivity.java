package com.example.ilya.artists;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<Artist> artists;
    private int lastLoadedId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artists_activity);
        Button btn = (Button)findViewById(R.id.btn_get_more);
        String url = this.getString(R.string.JSON);

        try {
            LoadArtists(url);
            TableFill(5);
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        View.OnClickListener BtnGetMore = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableFill(lastLoadedId +10);
            }
        };
        btn.setOnClickListener(BtnGetMore);
    }

    private void LoadArtists(String url) throws InterruptedException, java.util.concurrent.ExecutionException {
        LoadJSONTask mt = new LoadJSONTask(url);
        mt.execute();
        List<Artist> arn = mt.get();
        FileCache fc = new FileCache(this);
        if(arn != null) {
            artists = arn;
            fc.ClearArtistsCache();
        }
        else
        {
            artists = fc.GetArtistsCache();
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
    }

    private void  SortByABC(){
        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        TableLayout tb = (TableLayout) findViewById(R.id.main_table);
        tb.removeAllViews();
        int old = lastLoadedId;
        lastLoadedId =0;
        TableFill(old);
    }

    private void  SortByID(){
        Collections.sort(artists, new Comparator<Artist>() {
            @Override
            public int compare(Artist lhs, Artist rhs) {
                return lhs.getId().compareTo(rhs.getId());
                // return 0;
            }
        });
        TableLayout tb = (TableLayout) findViewById(R.id.main_table);
        tb.removeAllViews();
        int old = lastLoadedId;
        lastLoadedId =0;
        TableFill(old);
    }

    private void TableFill(int maxId) {
        try {
            if(maxId>artists.size())
            {
                String url = this.getString(R.string.JSON);
                LoadArtists(url);
            }
            int iMax = Math.min(maxId, artists.size());
            for (int i = lastLoadedId; i < iMax; i++) {
                SetTableRows str = new SetTableRows(artists.get(i), artists.size(), i, MainActivity.this, artists);
                str.execute();
            }
            lastLoadedId = iMax;
        }
        catch (Exception exc){exc.printStackTrace();}
    }

}

