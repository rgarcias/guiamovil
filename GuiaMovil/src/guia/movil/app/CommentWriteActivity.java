package guia.movil.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


public class CommentWriteActivity extends Activity implements OnClickListener {
	
	private ImageButton enviar;
	private ImageButton cancelar;
	private EditText nick;
	private EditText comment;
	@Override
	 public void onCreate(Bundle icicle) {
		 super.onCreate(icicle);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	     this.setContentView(R.layout.commentnew);
	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	     
	     enviar =(ImageButton) this.findViewById(R.id.comments_button);
	     cancelar =(ImageButton) this.findViewById(R.id.cancel_button);
	     
	     if(PresentationActivity.english){
	        	enviar.setImageResource(R.drawable.comment_button2);
	        	cancelar.setImageResource(R.drawable.cancel_button2);
	        }
	        else{
	        	enviar.setImageResource(R.drawable.comment_button);
	        	cancelar.setImageResource(R.drawable.cancel_button);
	        }
	     
	     nick= (EditText) findViewById(R.id.nickText);
	     comment= (EditText) findViewById(R.id.commentText);
	     cancelar.setOnClickListener(this);
	     enviar.setOnClickListener(this);
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
		else if(v.getId()== R.id.comments_button && isOnline())
		{
			
			 String methodname = "addComment";
		     String soap = "http://turismo/" + methodname;
		     Services.addComments(methodname, soap, comment.getText().toString(), nick.getText().toString(), CommentsActivity.placeID);

			Intent intent = new Intent(CommentWriteActivity.this,CommentsActivity.class);
			finish();
			this.startActivity(intent);
	    }	
	}
	
	public boolean isOnline() {
		Context context = getApplicationContext();
	    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    if (wifi.isConnected()) {
	        return true;
	    } else if (mobile.isConnected()) {
	        return true;
	    }
	    Dialog exitDialog = new Dialog(CommentWriteActivity.this, R.style.FullHeightDialog);
        exitDialog.setContentView(R.layout.exitdialog);
		if(PresentationActivity.english){
	        ImageButton exit = (ImageButton) exitDialog.findViewById(R.id.exitButton);
	        exit.setImageResource(R.drawable.quit_button2);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogING);
	        exit.setOnClickListener(this);
		}
		else{
			ImageButton exit = (ImageButton) exitDialog.findViewById(R.id.exitButton);
	        exit.setImageResource(R.drawable.quit_button);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogESP);
	        exit.setOnClickListener(this);
		}
		exitDialog.show(); 
	    return false;
	}

}
