package com.splo2t.alchol.model;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RedButton extends View {
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;
    public int n = 1;
    public int m = 3;

    float lastX = 0;
    float lastY = 0;
    float rectLeftX;
    float rectRightX;
    float rectTopY;
    float rectBottomY;

    public Rect[] pastrects = new Rect[4];
    //Rect pastrect;

    public int whoIsTouched(int x, int y){
        for(int i = 0; i < 4; i++){
            if(pastrects[i].contains(x,y)){
               //pastrect = pastrects[i];
               return i;
            }
        }
        return -1;
    }

    public RedButton(Context context) {
        super(context);
        init(context);
    }

    public RedButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        Bitmap img = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();

        canvas.setBitmap(img);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //canvas.drawColor(Color.WHITE);
        mBitmap = img;
        mCanvas = canvas;
        if(oldh == 0){
            reset();
        }
        else{
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            if(h > oldh){
                DrawAllRect((float) (h * 1.0 / oldh * 1.0));
                DrawLine();
            }
            else {
                if(pastrects[3].centerY() < 1015){
                    DrawAllRect((float)(1));
                    DrawLine();
                }
                else{
                    DrawAllRect((float) (h * 1.0 / oldh * 1.0));
                    DrawLine();
                }


            }

        }

       drawGuide();

    }

    public void drawGuide(){

        float alpha = Math.abs((pastrects[1].centerY()-pastrects[3].centerY()));
        float a = Math.abs(pastrects[0].centerX()-pastrects[1].centerX());
        float b = Math.abs(pastrects[2].centerX()-pastrects[3].centerX());
        float c = (float) Math.cbrt(n*1.0/(m+n)*a*a*a + m*1.0/(m+n)*b*b*b);
        float myH = (a-c)/(a-b)*alpha;
        float a_m = Math.abs(pastrects[0].centerX()-pastrects[1].centerX());
        float b_m = Math.abs(pastrects[2].centerX()-pastrects[3].centerX());
        a_m = 0.262f * myH * (c * c + c * a + a * a);
        b_m = 0.262f * (alpha - myH) * (c * c + c * b + b * b);
        Log.d("b: ", String.valueOf(b));
        Log.d("a: ", String.valueOf(a));
        Log.d("alpha: ", String.valueOf(alpha));
        Log.d("h: ", String.valueOf(myH));
        Log.d("c: ", String.valueOf(c));
        Log.d("a_m: ", String.valueOf(a_m));
        Log.d("b)m: ", String.valueOf(b_m));
        if (a == b){
            Log.d("yyyy",  String.valueOf(pastrects[0].centerY()+(n*1.0f/m+n)));
            drawMyLine((540.0f)-c/2,  (pastrects[0].centerY()+(n*1.0f/m+n)), (540.0f)+c/2, (pastrects[0].centerY()+(n*1.0f/m+n)));
        }
        else{
            drawMyLine((540.0f)-c/2, pastrects[0].centerY()+myH, (540.0f)+c/2, pastrects[0].centerY()+myH);

        }
        Log.d("last x, last y = ", String.valueOf(lastX)+" # " + String.valueOf(lastY) );
    }
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        Rect rect;
        float x = event.getX();
        float y = event.getY();
        int tempi = whoIsTouched((int)x,(int)y);
        if(tempi != -1){
        //if(x > rectLeftX && x < rectRightX && y < rectBottomY && y > rectTopY){
            Log.d("RedButton" , rectRightX + " " + rectLeftX + " " + rectTopY + " " + rectBottomY);
            switch (action){
                case MotionEvent.ACTION_UP : {
                    rect = touchUp(event,false);
                    if(rect!= null){
                        invalidate(rect);
                    }
                    return true;
                }

                case MotionEvent.ACTION_DOWN :{
                    rect = touchDown(event);
                    if(rect!= null){
                        invalidate(rect);
                    }
                    return true;
                }
                case MotionEvent.ACTION_MOVE : {
                    rect = touchMove(event);
                    if(rect!= null){
                        invalidate(rect);
                    }
                    return true;
                }
            }
        }
        return true;
    }
    public void reset(){
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        pastrects[0] = DrawRect(540-440,200, 1, 30);
        pastrects[1] = DrawRect(540+440,200, 1, 30);
        pastrects[3] = DrawRect(540-300,1000, 1, 30);
        pastrects[2] = DrawRect(540+300,1000, 1, 30);
        DrawLine();
        drawGuide();
    }
    public void refresh(){
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        DrawAllRect((float)(1));
        DrawLine();
        drawGuide();
    }

    public void drawMyLine(float a, float b, float c, float d){
        mPaint.setColor(Color.RED);
        mCanvas.drawLine(a,b,c,d, mPaint);

    }
    private Rect touchDown(MotionEvent event){
        Rect rect = processMove(event);
        return rect;
    }
    private Rect touchMove(MotionEvent event){
        Rect rect = processMove(event);
        return rect;
    }
    private Rect touchUp(MotionEvent event, boolean cancel){
        Rect rect = processMove(event);
        return rect;
    }
    private Rect processMove(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();
        int tempi = whoIsTouched((int)x,(int)y);
        lastX = x;
        lastY = y;
        //DeleteRect(pastrects[tempi]);
        //DeleteLine();
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        pastrects[tempi] = DrawRect(x,y, 1,30);
        float delta = (pastrects[tempi].centerX()-x);
        switch(tempi){
            case 0:
                pastrects[1] = DrawRect(1080-pastrects[tempi].centerX(),y, 1,30);
                break;
            case 1:
                pastrects[0] = DrawRect(1080-pastrects[tempi].centerX(),y, 1,30);
                break;
            case 2:
                pastrects[3] = DrawRect(1080-pastrects[tempi].centerX(),y, 1,30);
                break;
            case 3:
                pastrects[2] = DrawRect(1080-pastrects[tempi].centerX(),y, 1,30);
                break;
        }
        DrawAllRect(1);
        DrawLine();
        drawGuide();


        return pastrects[tempi];
    }

    private void DrawAllRect(float v){
        for(int i = 0; i < 4; i++){
            pastrects[i] = DrawRect(pastrects[i].centerX(),pastrects[i].centerY()*v,1,30);
        }

    }
    private void DrawLine(){
        for(int i = 0; i < 4; i++){
            mPaint.setColor(Color.RED);
            mCanvas.drawLine(pastrects[i%4].centerX(), pastrects[i%4].centerY(), pastrects[(i+1)%4].centerX(), pastrects[(i+1)%4].centerY(), mPaint);
        }

    }

    private void DeleteLine(){
        for(int i = 0; i < 4; i++){
            mPaint.setColor(Color.WHITE);
            mCanvas.drawRect(pastrects[i%4].centerX(), pastrects[i%4].centerY(), pastrects[(i+1)%4].centerX(), pastrects[(i+1)%4].centerY(), mPaint);
        }

    }
    protected void DeleteRect(Rect rect){
        //Log.d("ratio = ", String.valueOf(ratio));
        if(rect != null){ //이전 사각형을 하얀색으로 색칠하고 (없애버림)
            mPaint.setColor(Color.WHITE);
            mCanvas.drawRect(rect,mPaint);
        }

    }

    protected Rect DrawRect(float x, float y, float ratio, int size){
        Log.d("ratio = ", String.valueOf(ratio));
        /*if(pastrect != null){ //이전 사각형을 하얀색으로 색칠하고 (없애버림)
            mPaint.setColor(Color.WHITE);
            mCanvas.drawRect(pastrect,mPaint);
        }*/


        mPaint.setColor(Color.RED);
        rectTopY = y-size;
        rectBottomY = y+size;
        rectLeftX = x-size;
        rectRightX = x+size;

        Rect rect = new Rect(); //터치 지점을 가운데로하여 새 사각형을 그리기
        rect.set((int)rectLeftX, (int)rectTopY, (int)rectRightX, (int)rectBottomY);
        mCanvas.drawRect(rect , mPaint);
        return rect;
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap,0,0,null);
        }
    }
}
