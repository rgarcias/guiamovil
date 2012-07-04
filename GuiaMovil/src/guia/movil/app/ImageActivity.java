package guia.movil.app;

import guia.movil.app.InformationActivity.ImageItem;
import guia.movil.app.InformationActivity.MyAdapter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private static ImageItem item;
	private static MyAdapter adapter;
	private static int pos;
	private float startX;
	private 	float endX;
	private GestureDetector gesture;

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.image);
        startX = 0;
        endX = 0;
        ImageView image = (ImageView) this.findViewById(R.id.image);
        /*image.setImageBitmap(item.getImage());*/
        ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());
        
        gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
        	
        	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        		int dx = (int) (e2.getX() - e1.getX());
                // don't accept the fling if it's too short
                // as it may conflict with a button push
                if (Math.abs(dx) > 100 && Math.abs(velocityX) > Math.abs(velocityY)) {
                    if (velocityX > 0) {
                        moveRight();
                    } else {
                        moveLeft();
                    }
                    return true;
                } else {
                    return false;
                }
        	}
        });
    }

	protected void moveLeft() {
		// TODO Auto-generated method stub
		if(pos > 1){
			pos -=1;
		}
		else{
			pos = adapter.getCount();
		}
	}

	protected void moveRight() {
		// TODO Auto-generated method stub
		if(pos < adapter.getCount()){
			pos +=1;		
		}
		else{
			pos = 1;
		}
	}

	public static void setImageItem(ImageItem imageItem) {
		// TODO Auto-generated method stub
		item = imageItem;
	}
	
	public static void setImages(MyAdapter a, int position){
		adapter = a;
		pos = position;
	}

	/*
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			startX = event.getX();
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			endX = event.getX(event.getPointerCount()-1);
			Log.e("startX", ""+startX);
			Log.e("endX", ""+endX);
			if(endX < startX){
				if(pos > 1){
					pos -=1;
				}
				else{
					pos = adapter.getCount();
				}
			}
			if(endX > startX){
				if(pos < adapter.getCount()){
					pos +=1;		
				}
				else{
					pos = 1;
				}
			}
		}
		ImageView image = (ImageView) this.findViewById(R.id.image);
		ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());
		return false;
	}*/
}
