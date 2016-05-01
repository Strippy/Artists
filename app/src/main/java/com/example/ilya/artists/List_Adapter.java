package com.example.ilya.artists;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya on 26.04.2016.
 */
public class List_Adapter extends ArrayAdapter<Artist> {

    List<Artist> _artists;
    Activity _context;
    List<Bitmap> _images;
    public  List<Bitmap> getImages()
    {
        return  _images;
    }
    public void SetImages(List<Bitmap> images)
    {
        _images= images;
    }

    public List_Adapter(Activity context, List<Artist> artists, List<Bitmap> images) {
        super(context, R.layout.activity_actvity_list_view, artists);
        _artists = artists;
        _context = context;
        _images = images;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = _context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.activity_actvity_list_view, null, true);
            ImageView img = (ImageView) rowView.findViewById(R.id.icon);
            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView albums = (TextView) rowView.findViewById(R.id.albums);
            TextView tracks = (TextView) rowView.findViewById(R.id.tracks);
            Bitmap image ;
            if (position < _images.size()) {
                image = _images.get(position);
                img.setImageBitmap(image);
            }
            else {
                LoadImg limg = new LoadImg(_artists.get(position), _context);
                limg.execute();
                try {
                    image = limg.get();//TODO переделать загрузку изображений, для фоновой загрузки, освободить UI
                    _images.add(image);
                    img.setImageBitmap(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            name.setText(_artists.get(position).getName());
            albums.setText(_artists.get(position).getAlbums());
            tracks.setText(_artists.get(position).getTracks());
            return rowView;

    }
}
