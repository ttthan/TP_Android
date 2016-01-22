package com.example.ttth.chronometre;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * Created by TTTH on 1/7/2016.
 */
public class ClockHandDrawable extends Drawable
{
    private final float handRadius;
    private final Paint paint;
    private final long timeUnit;

    private long time ; /* in nanoseconds */

    public ClockHandDrawable(long timeUnit /* in nanoseconds */, float radius, int thickness)
    {
        this.handRadius = radius;
        this.timeUnit = timeUnit;
        // Set the Paint to draw the clock hand
        this.paint = new Paint();
        this.paint.setStrokeWidth(thickness);
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float radiusX = canvas.getWidth() / 2, radiusY = canvas.getHeight() / 2;
        final double angle = Math.PI * (0.5 - (double)(2 * (time % timeUnit)) / (double)timeUnit);
        canvas.drawLine(radiusX, radiusY,
                (float)(radiusX * (1.0 + Math.cos(angle) * handRadius)),
                (float)(radiusY * (1.0 - Math.sin(angle) * handRadius)),
                paint);
    }

    @Override
    public int getOpacity()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setAlpha(int alpha)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setColorFilter(ColorFilter cf)
    {
        // TODO Auto-generated method stub
    }
}
