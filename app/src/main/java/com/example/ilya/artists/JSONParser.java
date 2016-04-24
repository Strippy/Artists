package com.example.ilya.artists;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Ilya on 23.04.2016.
 */
//класс работы с JSON
public class JSONParser {

    String _url;

    public JSONParser(String url){
        _url = url;
    }

    public List<Artist> GetArtistsFromJSON(){
        try {
            String JSONString = readUrl(_url);
            JSONArray ja = new JSONArray(JSONString);
            return GetArtistsFromJSON(ja);
        }catch (Exception exc){
            exc.printStackTrace();
            return null;
        }
    }

    public static Artist GetArtistFromJSON(String jString) {
        try {
            JSONObject jo = new JSONObject(jString);
            return GetArtistFromJSONObj(jo);
        } catch (Exception exc) {
            exc.printStackTrace();
            return  null;
        }
    }

    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null)
                buffer.append(line + "\n");
            String ans = buffer.toString();
            return ans;
        }
        catch (Exception exc){
            exc.printStackTrace();
            return  null;
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }

    private List<Artist> GetArtistsFromJSON(JSONArray jArray){
        List<Artist> _artists = new ArrayList<>();
        Artist atr;
        for(int i= 0; i < jArray.length(); i++) {
            atr = new Artist();
            try{
                JSONObject jo = jArray.getJSONObject(i);
                atr = GetArtistFromJSONObj(jo);
            }
            catch(Exception exc){
                exc.printStackTrace();
                continue;
            }
            _artists.add(atr);
        }
        return _artists;
    }

    private static Artist GetArtistFromJSONObj(JSONObject obj){
        //  JSONObject obj = jArray.getJSONObject(i);
        try {
            Artist atr = new Artist();
            atr.setId(obj.getString("id"));
            atr.setName(obj.optString("name"));
            atr.setAlbums(obj.optString("albums"));
            atr.setTracks(obj.optString("tracks"));
            atr.setDescription(obj.optString("description"));
            atr.setLink(obj.optString("link"));
            Cover cover = new Cover();
            cover.setBig(obj.getJSONObject("cover").optString("big"));
            cover.setSmall(obj.getJSONObject("cover").optString("small"));
            atr.setCover(cover);
            JSONArray ja = obj.getJSONArray("genres");
            String[] genres = new String[ja.length()];
            for (int j = 0; j < ja.length(); j++) {
                genres[j] = ja.getString(j);
            }
            atr.setGenres(genres);
            return atr;
        }
        catch (Exception exc){exc.printStackTrace();return null;}
    }
}
