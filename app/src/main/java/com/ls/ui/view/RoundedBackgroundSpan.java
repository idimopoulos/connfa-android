package com.ls.ui.view;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import org.jetbrains.annotations.NotNull;

public class RoundedBackgroundSpan extends ReplacementSpan {

    private final int textColor;
    private final int backgroundColor;

    public RoundedBackgroundSpan(int textColor, int backgroundColor) {
        super();
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void draw(@NotNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NotNull Paint paint) {
        float fontBottom = paint.getFontMetrics().bottom;
        float fontTop = paint.getFontMetrics().top;

        RectF rect = new RectF(x, y + fontTop, x + measureText(paint, text, start, end),  y + fontBottom);
        paint.setColor(backgroundColor);
        int CORNER_RADIUS = 30;
        canvas.drawRoundRect(rect, CORNER_RADIUS, CORNER_RADIUS, paint);
        paint.setColor(textColor);
        canvas.drawText(text, start, end, x, y, paint);

    }

    @Override
    public int getSize(@NotNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    private float measureText(Paint paint, CharSequence text, int start, int end) {
        return paint.measureText(text, start, end);
    }


}