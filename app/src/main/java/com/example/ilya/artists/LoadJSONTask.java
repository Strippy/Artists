package com.example.ilya.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Ilya on 23.04.2016.
 */
//Загрузка данных асинхронным запросом
public class LoadJSONTask extends AsyncTask<Void, Void, List<Artist>> {

    private List<Artist> artists;
    private String _url;

    public LoadJSONTask(String url){
        _url = url;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected List<Artist> doInBackground(Void... params) {
        try {
            JSONParser jParser = new JSONParser(_url);
            artists = jParser.GetArtistsFromJSON();
            return artists;
        }
        catch (Exception exc){
            exc.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(List<Artist> result) {
        super.onPostExecute(result);
    }

}