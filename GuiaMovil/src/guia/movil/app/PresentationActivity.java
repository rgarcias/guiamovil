package guia.movil.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PresentationActivity extends Activity implements OnClickListener {
    @Override
/** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.presentation);
        
	    ImageView sv = (ImageView) this.findViewById(R.id.spanishButton);
	    ImageView ev = (ImageView) this.findViewById(R.id.englishButton);
	    sv.setOnClickListener(this);
	    ev.setOnClickListener(this);
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PresentationActivity.this, MainActivity.class);
		if(view.getId() == R.id.spanishButton){
			intent.putExtra("english", false);
		}
		if(view.getId() == R.id.englishButton){
			intent.putExtra("english", true);
		}
		this.startActivity(intent);
        this.finish();
	}
}