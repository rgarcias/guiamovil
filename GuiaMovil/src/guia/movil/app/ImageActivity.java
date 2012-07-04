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
        image.setImageBitmap(item.getImage());
        /*ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());*/
	}
        
	public static void setImageItem(ImageItem imageItem) {
		// TODO Auto-generated method stub
		item = imageItem;
	}
	
	public static void setImages(MyAdapter a, int position){
		adapter = a;
		pos = position;
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			startX = event.getX();
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			endX = event.getX(event.getPointerCount()-1);
			if(endX < startX){
				float offsetX = endX - startX;
				Log.e("asd","left, offset " + offsetX);
				if(offsetX > -80){
					if(pos <= 0){
						pos = adapter.getCount()-1;
					}
					else{
						pos -=1;
					}
				}
			}
			if(endX > startX){
				float offsetX = endX - startX;
				Log.e("asd","right, offset " + offsetX);
				if(offsetX > 80){
					if(pos >= adapter.getCount()-1){
						pos = 0;
					}
					else{
						pos +=1;		
					}
				}
			}
		}
		ImageView image = (ImageView) this.findViewById(R.id.image);
		Log.e("pos", ""+pos);
		ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());
		return true;
	}*/
}
