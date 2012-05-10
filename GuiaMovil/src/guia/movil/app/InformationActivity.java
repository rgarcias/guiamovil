package guia.movil.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class InformationActivity extends FBConnectionActivity {
	private ImageButton btnShare;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.information);
        
        RatingBar rb = (RatingBar) this.findViewById(R.id.ratingBar1);
        
        rb.setRating(4.2f);
        
        btnShare= (ImageButton) findViewById(R.id.shareButton); 
        
        btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            		
            		setConnection();
            		getID();
            		String text= "Radal fhghfjfhg tazas";
            		postOnWall(text);
            	
            	
            	
            }
        });
    }
}
