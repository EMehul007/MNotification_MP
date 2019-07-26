package com.example.mnotification;

import android.graphics.Bitmap;

/**
 * Created by mukesh on 18/5/15.
 */
public class Model {
    String name;
    String text;
    Bitmap imaBitmap;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return imaBitmap;
    }

    public void setImage(Bitmap imaBitmap) {
        this.imaBitmap = imaBitmap;
    }
}
