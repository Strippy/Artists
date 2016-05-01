package com.example.ilya.artists;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya on 23.04.2016.
 */
//класс работы с кешем: сохранение и загрузка картинок, последних загруженных артистов
public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "ArtistImages");
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.mkdirs()) {
            cacheDir.mkdirs();
        }
    }

    public File GetFile(String url){
        String filename = String.valueOf(url.hashCode());//
        File f = new File(cacheDir, filename);
        return f;
    }
//кеширование изображения
    public void CacheBitmap(Bitmap bmp, String url){
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String fileName = String.valueOf(url.hashCode());
            File cacheFile = new File(cacheDir, fileName );
            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            outputStream.write(byteArray);
            outputStream.close();
        }
        catch (IOException exc)
        {
            exc.printStackTrace();
        }
    }
//Кеширование данных артиста
    public void CacheArtist(Artist artist) {
        try {
            String fileName = "artist:"+artist.getId();
            File cacheFile = new File(cacheDir, fileName );
            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            ObjectOutputStream objectOutputStream= new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(artist);
            outputStream.close();
            objectOutputStream.close();
        }
        catch (IOException exc){exc.printStackTrace();}
    }

    public  List<Artist> GetArtistsCache() {
        File[] files = getArtistsFiles();
        List<Artist> artists= new ArrayList<>();
        for (File file:files
             ) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
               Artist a= (Artist) ois.readObject();
               artists.add(a);
            }catch (Exception exc){
                exc.printStackTrace();
            }
        }
        return  artists;

    }

    public void ClearArtistsCache() {
        File[] files = getArtistsFiles();
        if(files.length>0)
            for (File fi:files
                 ) {
                fi.delete();
            }
    }

    private File[] getArtistsFiles() {
        FilenameFilter fnf= new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if(filename.contains( "artist"))
                    return  true;
                return false;
            }
        };
        fnf.accept(cacheDir,"artist");
        return cacheDir.listFiles(fnf);
    }

    public void Clear()  {
        File[] files = cacheDir.listFiles();
        if(files ==null)
        {
            return;
        }
        for(File f:files)
        {
            f.delete();
        }
    }
}
