package com.example.ilya.artists;

import java.io.Serializable;

/**
 * Created by Ilya on 23.04.2016.
 */
// Инфо о картинках
// Serializable - для сохранения вкэш
public class Cover implements Serializable
{
    private String big;

    private String small;

    public String getBig ()
    {
        return big;
    }

    public void setBig (String big)
    {
        this.big = big;
    }

    public String getSmall ()
    {
        return small;
    }

    public void setSmall (String small)
    {
        this.small = small;
    }

    @Override
    public String toString()
    {
       return "{big: \""+big+"\", small: \""+small+"\"}";
    }//для удобной передаче между активити
}