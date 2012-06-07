package guia.movil.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class CommentWriteActivity extends Activity implements OnClickListener {
	
	private ImageButton send;
	private ImageButton cancel;
	private EditText nick;
	private EditText comment;
	@Override
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	     this.setContentView(R.layout.commentnew);
	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	     send =(ImageButton) this.findViewById(R.id.comment_button);
	     cancel =(ImageButton) this.findViewById(R.id.cancel_button);
	     nick= (EditText) findViewById(R.id.nickText);
	     comment= (EditText) findViewById(R.id.commentText);
	     cancel.setOnClickListener(this);
	     send.setOnClickListener(this);
	 }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.cancel_button)
		{
			Intent intent = new Intent(CommentWriteActivity.this,CommentsActivity.class);
			finish();
			this.startActivity(intent);
		}
		else if(v.getId()== R.id.comment_button)
		{
			
			 String methodname = "addComment";
		     String soap = "http://turismo/" + methodname;
		     Services.addComments(methodname, soap, comment.getText().toString(), nick.getText().toString(), CommentsActivity.placeID);
			
			
			
			Intent intent = new Intent(CommentWriteActivity.this,CommentsActivity.class);
			finish();
			this.startActivity(intent);
	    }
			
		
		
	}

}
