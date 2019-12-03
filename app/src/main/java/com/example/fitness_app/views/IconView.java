package com.example.fitness_app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.example.fitness_app.R;

import static com.example.fitness_app.constrants.Styles.ICON_TYPE_BRANDS;
import static com.example.fitness_app.constrants.Styles.ICON_TYPE_REGULAR;
import static com.example.fitness_app.constrants.Styles.ICON_TYPE_SOLID;

public class IconView extends androidx.appcompat.widget.AppCompatTextView {
    public IconView(Context context) {
        super(context);
        this.context = context;
    }
    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttributeSet(attrs);
    }
    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttributeSet(attrs);
    }

    private Context context;
    public void setIconColor(Integer color){
        if (color != null){
            setTextColor(color);
        }
    }

    private void getAttributeSet(AttributeSet attrSet) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrSet,
                R.styleable.IconView,
                0, 0);
        try {
            setIconType(context, ta.getInt(R.styleable.IconView_iconType, ICON_TYPE_SOLID));
        } finally {
            ta.recycle();
        }
    }

    private void setIconType(Context context, int iconType){
        switch (iconType){
            case ICON_TYPE_BRANDS:
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fa-brands-400.ttf"));
                break;
            case ICON_TYPE_REGULAR:
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fa-regular-400.ttf"));
                break;
            case ICON_TYPE_SOLID:
                this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fa-solid-900.ttf"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + iconType);
        }
    }
}
