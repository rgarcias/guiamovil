package guia.movil.app;

import guia.movil.app.InformationActivity.ImageItem;
import guia.movil.app.InformationActivity.MyAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageActivity extends Activity implements OnGestureListener {
	private static final int SWIPE_MIN_DISTANCE = 60;
    private static final int SWIPE_THRESHOLD_VELOCITY = 800;
	
	private static MyAdapter adapter;
	private static int pos;
	private GestureDetector gesture;

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.image);
        ImageView image = (ImageView) this.findViewById(R.id.image);
        ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());
        
        gesture = new GestureDetector(this);
	}
	
	public static void setImages(MyAdapter a, int position){
		adapter = a;
		pos = position;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		return gesture.onTouchEvent(event);
}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {	
		if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			float offsetX = e1.getX() - e2.getX();
			Log.e("asd","left, offset: " + offsetX + ", velocity: "+velocityX);
			if(pos <= 0){
				pos = adapter.getCount()-1;
			}
			else{
				pos -=1;
			}
	    } 
		else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
			float offsetX = e2.getX() - e1.getX();
			Log.e("asd","right, offset: " + offsetX + ", velocity: "+velocityX);
			if(pos >= adapter.getCount()-1){
				pos = 0;
			}
			else{
				pos +=1;		
			}
	    }
		ImageView image = (ImageView) this.findViewById(R.id.image);
		ImageItem im = (ImageItem) adapter.getItem(pos);
        image.setImageBitmap(im.getImage());
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
