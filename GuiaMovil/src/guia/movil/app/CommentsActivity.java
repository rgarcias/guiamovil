package guia.movil.app;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsActivity extends Activity implements OnClickListener {
	protected static String nick;
	protected static String comment;
	
	private TextView title;
	private ListView commentaries;
	private serv.CommentResume[] datos= new serv.CommentResume[]{new serv.CommentResume("Juan", "Buen lugar")};
	private ImageButton comentar;
	protected static String placeID;
	private Dialog exitDialog;
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.comments);
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        title = (TextView)findViewById(R.id.comentaryTitle);
        comentar= (ImageButton)findViewById(R.id.comment);
        title.setText("Comentarios"); 
        if(PresentationActivity.english){
        	title.setText("Comments"); 
        	comentar.setImageResource(R.drawable.comment_button2);
        }
        else{
        	comentar.setImageResource(R.drawable.comment_button);
        }
        commentaries = (ListView) findViewById(R.id.commentaryList);
        String methodname = "getComment";
        String soap = "http://turismo/" + methodname;
        String methodnameID = "getPlaceID";
        String soapID = "http://turismo/" + methodnameID;
        
        placeID = Services.getPlaceID(methodnameID, soapID, "place", CategoryActivity.PLACE); 
        if(isOnline()){
        	String consulta =Services.getComments(methodname, soap, "placeID", placeID);
            procesarConsulta(consulta);
        }

        AdaptadorTitulares adaptador = new AdaptadorTitulares(this);
        commentaries.setAdapter(adaptador);
        commentaries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        final Dialog commentView = new Dialog(CommentsActivity.this, R.style.FullHeightDialog);
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				nick=datos[arg2].getNick();
				comment=datos[arg2].getComment();	
				commentView.setContentView(R.layout.commentview);
				commentView.setCancelable(true);
		        TextView nick = (TextView) commentView.findViewById(R.id.nickName);
		        TextView comment = (TextView) commentView.findViewById(R.id.commentSpace);      
		        nick.setText(CommentsActivity.nick);
		        comment.setText(CommentsActivity.comment);
		        ImageButton back = (ImageButton)commentView.findViewById(R.id.commentBack);
		        back.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	commentView.dismiss();
		            }});
		        commentView.show(); 	
			}}); 
        comentar.setOnClickListener(this); 
    }

	private void procesarConsulta(String consulta) {
		Gson gson = new Gson();
		ArrayList<serv.CommentResume> arreglo = new ArrayList<serv.CommentResume>();
        Type collectionType = new TypeToken<ArrayList<serv.CommentResume> >(){}.getType();
        arreglo = gson.fromJson(consulta, collectionType);

        datos=new serv.CommentResume[arreglo.size()];
        arreglo.toArray(datos);
	}

	public void onClick(View arg0) {
		if(arg0.getId() == R.id.comment){
			Intent intent = new Intent(CommentsActivity.this,CommentWriteActivity.class);
			finish();
			this.startActivity(intent);
		}
		if(arg0.getId()==R.id.cancelButton){
			exitDialog.dismiss();
		}
		if(arg0.getId()==R.id.acceptButton){
			Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
			settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			CommentsActivity.this.startActivityForResult(settingsIntent, 0);
			exitDialog.dismiss();
			finish();
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
	    exitDialog = new Dialog(CommentsActivity.this, R.style.FullHeightDialog);
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

	class AdaptadorTitulares extends ArrayAdapter<serv.CommentResume>{
	    Activity context;
	        
	    public AdaptadorTitulares(Activity context) {
	            super(context, R.layout.commentlistitem, datos);
	            this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {    	
	       	LayoutInflater inflater = context.getLayoutInflater();
	       	View item = inflater.inflate(R.layout.commentlistitem, null);
	       	TextView lblTitulo = (TextView)item.findViewById(R.id.LblTitulo);
	       	lblTitulo.setText(datos[position].getNick());
	 
	       	TextView lblSubtitulo = (TextView)item.findViewById(R.id.LblSubTitulo);
	       	if(datos[position].getComment().length()>20)
	       		lblSubtitulo.setText(datos[position].getComment().substring(0,20)+"...");
	       	else
	       		lblSubtitulo.setText(datos[position].getComment());
	       	item.setTag(position);
	       	return(item);
	    }	
	}
}