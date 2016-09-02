package com.tamn.app2uproject;

import android.graphics.drawable.Drawable;

/**
 * Created by Tamn on 14/08/2016.
 */
public class PictureHelper {

    private Drawable drawable;
    private static PictureHelper ourInstance = new PictureHelper();

    public static PictureHelper getInstance() {
        return ourInstance;
    }

    private PictureHelper() {
    }

    public Drawable getDrawable() {

        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }


}
