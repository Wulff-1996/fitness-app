package com.example.fitness_app.util;

import android.graphics.drawable.Drawable;

import androidx.core.graphics.drawable.DrawableCompat;

public class DrawableUtil {

    public static Drawable setTint(Drawable d, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(d);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }
}
