package com.ls.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import org.jetbrains.annotations.NotNull;

public class MultiFontsTypefaceSpan extends TypefaceSpan {

    private Typeface newType;

    MultiFontsTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
    }

    @Override
    public void updateDrawState(@NotNull TextPaint ds) {
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(@NotNull TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        if(tf != null) {
            paint.setTypeface(tf);
        }
    }
}