package com.androidquery.simplefeed.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        
        int w = source.getWidth();                                          
        int h = source.getHeight();  
 
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
 
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
 
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
 
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);
        
        /*
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
        canvas.drawBitmap(bitmap, 4, 4, paint); 
        paint.setXfermode(null); 
        paint.setStyle(Style.STROKE);                                           
        paint.setColor(Color.WHITE);                                            
        paint.setStrokeWidth(7);    
        */                                            
       
        float r = size/2f;
        //canvas.drawCircle((w / 2) + 4, (h / 2) + 4, r, paint); 
        canvas.drawCircle(r, r, r, paint);
 
        squaredBitmap.recycle();
        return bitmap;
    }
 
    @Override
    public String key() {
        return "circle";
    }
}
