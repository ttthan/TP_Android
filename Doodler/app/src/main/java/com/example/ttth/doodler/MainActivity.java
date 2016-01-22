package com.example.ttth.doodler;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.io.Serializable;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    public static int VIBRATION_DURATION = 500;
    public static class Line implements Serializable{
        public int finger;
        public int x1,x2,y1,y2;

        public Line(int finger, int x1, int y1, int x2, int y2){
            this.finger = finger;
            this.x1 = x1; this.y1 = y1;
            this.x2 = x2; this.y2 = y2;
        }

        @Override
        public String toString() {
            return String.format("finger = %d, from = %d,%d, to %d,%d ", finger,x1, y1, x2, y2);
        }
    }

    public static class LineContainer implements Serializable{
        public static final int[] COLORS = new int[]{Color.BLACK, Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN};
        public SortedMap<Long,Line> lines = new TreeMap<Long, Line>();

        public void draw(Canvas canvas, long fromTimestamp){
            Paint p = new Paint();
            p.setStrokeWidth(5);
            p.setAntiAlias(true);
            p.setDither(true);
            p.setStyle(Paint.Style.STROKE);
            Rect bounds = canvas.getClipBounds();
            for (Line line: lines.tailMap(fromTimestamp).values()){
                p.setColor(COLORS[line.finger % COLORS.length]);
                if (bounds.contains(line.x1,line.y1) || bounds.contains(line.x2,line.y2));
                canvas.drawLine(line.x1,line.y1,line.x2,line.y2,p);
            }
        }

        public void addLine(int finger, int x1, int y1, int x2, int y2){
            lines.put(System.nanoTime(), new Line(finger,x1,y1, x2, y2));
        }

        public void clear(){
            this.lines.clear();
        }

        public long getLastUpdate(){
            return (this.lines.isEmpty()?-1:this.lines.lastKey());
        }

    }

    private LineContainer lineContainer = null;
    private View lineView = null;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("lineContainer", lineContainer);
    }


    private final View.OnTouchListener touchListener = new View.OnTouchListener() {
        private SparseArray<MotionEvent.PointerCoords> previousPoints = new SparseArray<MotionEvent.PointerCoords>();

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()){
                case MotionEvent.ACTION_DOWN:case  MotionEvent.ACTION_POINTER_DOWN:
                    int finger = event.getPointerId(event.getActionIndex());
                    MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
                    event.getPointerCoords(event.getActionIndex(), coords);

                    previousPoints.put(finger,coords);
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int i=0; i<event.getPointerCount();i++){
                        finger = event.getPointerId(i);
                        coords = previousPoints.get(finger);
                        int left = Math.min((int) coords.x, (int) event.getX(i));
                        int top = Math.min((int) coords.y, (int) event.getY(i));
                        int right = Math.max((int)coords.x, (int)event.getX(i));
                        int bottom = Math.max((int)coords.y, (int)event.getY(i));
                        lineContainer.addLine(finger,(int)coords.x,(int)coords.y,(int)event.getX(i),(int)event.getY(i));
                        event.getPointerCoords(i, coords);
                        lineView.invalidate(new Rect(left,top,right,bottom));

                    }
                    break;
            }
            return true;
        }
    };

    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private Rect tmpRect = new Rect();
    private long lastCanvasUpdate = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LineContainer container = (savedInstanceState!=null)?(LineContainer)savedInstanceState.getSerializable("lineContainer"):null;
        this.lineContainer = (container!=null)?container:new LineContainer();
        lineView = new View(this){
            @Override
            protected void onDraw(Canvas canvas) {
                if (bufferBitmap==null){
                    bufferBitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
                    bufferCanvas = new Canvas(bufferBitmap);
                }
                lineContainer.draw(bufferCanvas,lastCanvasUpdate);
                lastCanvasUpdate=System.nanoTime();
                canvas.getClipBounds(tmpRect);
                canvas.drawBitmap(bufferBitmap,tmpRect,tmpRect,null);
            }
        };
        lineView.setOnTouchListener(touchListener);
        setContentView(lineView);
    }


}
