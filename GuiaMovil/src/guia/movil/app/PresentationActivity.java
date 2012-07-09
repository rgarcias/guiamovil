package guia.movil.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PresentationActivity extends Activity implements OnClickListener {
	public static boolean english;
	
	private  Dialog exitDialog;
    
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

	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PresentationActivity.this, CategoryActivity.class);
		if(view.getId() == R.id.spanishButton){
			english = false;
			if(isOnline()){
				this.startActivity(intent);
				this.finish();
			}
		}
		else if(view.getId() == R.id.englishButton){
			english = true;
			if(isOnline()){
				this.startActivity(intent);
				this.finish();
			}
		}
		if(view.getId()==R.id.cancelButton){
			exitDialog.dismiss();
		}
		if(view.getId()==R.id.acceptButton){
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			PresentationActivity.this.startActivityForResult(settingsIntent, 0);
			exitDialog.dismiss();
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
	    exitDialog = new Dialog(PresentationActivity.this, R.style.FullHeightDialog);
        exitDialog.setContentView(R.layout.exitdialog);
		if(PresentationActivity.english){
	        ImageButton accept = (ImageButton) exitDialog.findViewById(R.id.acceptButton);
	        accept.setImageResource(R.drawable.accept_button2);
	        ImageButton cancel = (ImageButton) exitDialog.findViewById(R.id.cancelButton);
	        cancel.setImageResource(R.drawable.cancel_button2);
	        
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogING);
	        accept.setOnClickListener(this);
	        cancel.setOnClickListener(this);
		}
		else{
			ImageButton accept = (ImageButton) exitDialog.findViewById(R.id.acceptButton);
	        accept.setImageResource(R.drawable.accept_button);
	        ImageButton cancel= (ImageButton) exitDialog.findViewById(R.id.cancelButton);
	        cancel.setImageResource(R.drawable.cancel_button);
	        TextView exitText = (TextView) exitDialog.findViewById(R.id.exitText);
	        exitText.setText(R.string.exitDialogESP);
	        accept.setOnClickListener(this);
	        cancel.setOnClickListener(this);
		}
		exitDialog.show(); 
	    return false;
	}
}