package guia.movil.app;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashActivity extends Activity{
	private long splashDelay = 4000;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.intro);
        
        ImageView intro = new ImageView(this);
        intro.setImageResource(R.drawable.ic_presentation_intro);
        
        TimerTask task = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				finish();
				Intent intent = new Intent(SplashActivity.this, PresentationActivity.class);
				startActivity(intent);
			}
        	
        };
        
        Timer timer = new Timer();
        timer.schedule(task, splashDelay);
    }
}
