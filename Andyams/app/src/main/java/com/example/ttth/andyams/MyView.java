package com.example.ttth.andyams;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by TTTH on 1/11/2016.
 */
public class MyView extends View {
    int i;
    public MyView(Context context, int i) {
        super(context);
        this.i = i;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        canvas.drawText(String.valueOf(i), 10, 25, paint);
    }
}
