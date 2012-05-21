package guia.movil.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CommentWriteActivity extends Activity implements OnClickListener {
	
	private Button send;
	private Button cancel;
	@Override
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	     this.setContentView(R.layout.commentnew);
	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	     send =(Button) findViewById(R.id.sendComment);
	     cancel =(Button) findViewById(R.id.cancelComment);
	     
	     cancel.setOnClickListener(this);
	     send.setOnClickListener(this);
	     
	     
	 }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.cancelComment)
		{
			Intent intent = new Intent(CommentWriteActivity.this,CommentsActivity.class);
			finish();
			this.startActivity(intent);
		}
		else if(v.getId()== R.id.sendComment)
		{
			Intent intent = new Intent(CommentWriteActivity.this,CommentsActivity.class);
			finish();
			this.startActivity(intent);
	    }
			
		
		
	}

}
