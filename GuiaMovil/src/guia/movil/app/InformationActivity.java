package guia.movil.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends FBConnectionActivity implements OnClickListener {
	private ImageButton btnShare;
	private ImageButton btnTwitt;
	private ImageButton btnCheck;
	private ImageButton btnStar;
	private ImageButton btnComment;
	
	//foursquare
	public static final String CLIENT_ID = "QDLB4XF1BRCAP3X0KBOX04MQ43F1SJ0GPYYNX1H2A2EV11FX";
	public static final String CLIENT_SECRET = "AMII1DTTF4WCKMTTJ4JUN0XFBARKV1QY4IWTVG4QPMQFMLYW";
	
	//twitter
	private static final String CONSUMER_KEY = "l1s2kUwvs2Y3qrgLelGUsw";	
	private static final String CONSUMER_SECRET = "A8WBA5myV7ktHTRyXipOPHCh1moo9uhqgDUofkPO0";	
	private static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	private static final String CALLBACK_URL = "app://twitt";
	
	final String ACCES="accesToken", SECRET="secret", PREFERENCIAS="pref";
	
	private CommonsHttpOAuthConsumer httpOauthConsumer;
	private OAuthProvider httpOauthprovider;
	private AccessToken accessToken;
	
	private SharedPreferences accesPref;
	private Context mContext;
	
	private com.fsq.android.FoursquareApp mFsqApp;
	private ListView mListView;
	private com.fsq.android.NearbyAdapter mAdapter;
	private ArrayList<com.fsq.android.FsqVenue> mNearbyList;
	private ProgressDialog mProgress;
	private RatingBar ratingBar;
	private String placeID;
	private String rat;
	private RatingBar rb;
	private TextView text;
	private TextView title;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.information);
        
        rb = (RatingBar) this.findViewById(R.id.ratingBar1);
        rb.setRating(Float.valueOf(0)); 
        title = (TextView) this.findViewById(R.id.textView1);
        title.setText(CategoryActivity.PLACE);
        text = (TextView) this.findViewById(R.id.textView2);
        if(isOnline()){
	        /* get descriptions */
	        String methodname = "getDescription";
	        String soap = "http://turismo/" + methodname;
	        text.setText(Services.getDescription(methodname, soap, "place", CategoryActivity.PLACE, "english", new Boolean(PresentationActivity.english)));
	        
	        String methodnameID = "getPlaceID";
	        String soapID = "http://turismo/" + methodnameID;
	    	placeID = Services.getPlaceID(methodnameID, soapID, "place", CategoryActivity.PLACE);
	        
	        /*rating*/
	    	refreshRat();
	        rb.setRating(Float.valueOf(rat)); 
	        
	        /* photos */
	        ArrayList<String> photos = procesarConsulta(Services.getPhotos("getPhotos", "http://turismo/getPhotos", "placeID", placeID));
	        ImageView image = (ImageView) this.findViewById(R.id.imageView1);
	        image.setImageBitmap(this.getImageBitmap(photos.get(0)));
        }
        mContext=this;
        btnShare= (ImageButton) findViewById(R.id.shareButton); 
        btnTwitt= (ImageButton) findViewById(R.id.tweetButton);
        btnCheck= (ImageButton) findViewById(R.id.checkinButton);
        btnComment =(ImageButton) findViewById(R.id.commentsButton);
        btnStar=(ImageButton) findViewById(R.id.starButton);
        
       
        
        mFsqApp 		= new com.fsq.android.FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
        mAdapter 		= new com.fsq.android.NearbyAdapter(this);
        mNearbyList	= new ArrayList<com.fsq.android.FsqVenue>();
        mProgress		= new ProgressDialog(this);
        
        btnShare.setOnClickListener(this);
        btnTwitt.setOnClickListener(this);
        btnComment.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        btnCheck.setOnClickListener(this); 
    }
    
    private void refreshRat()
    {
    	String methodnameAv = "getPlaceRatingAverage";
        String soapAv = "http://turismo/" + methodnameAv;
    	rat = Services.getPlaceID(methodnameAv, soapAv, "placeId", placeID);
    	
        rb.setRating(Float.valueOf(rat));
    }
    
   
	
	private void enviaTweet() {
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(accessToken);
	
			// send the tweet
			twitter.updateStatus("Probando twitter desde aplicacion android para la muni");
		} catch(Exception e) {
			
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()== R.id.commentsButton && isOnline())
		{
			Intent intent = new Intent(InformationActivity.this,CommentsActivity.class);
			this.startActivity(intent);
		}
		else if(v.getId()==R.id.starButton && isOnline())
		{
			final Dialog rankDialog = new Dialog(InformationActivity.this, R.style.FullHeightDialog);
	        rankDialog.setContentView(R.layout.stardialog);
	        rankDialog.setCancelable(true);
	        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);

	        ImageButton aceptar = (ImageButton) rankDialog.findViewById(R.id.starAcept);
	        ImageButton cancelar = (ImageButton) rankDialog.findViewById(R.id.starCancel);
	        
	        if(PresentationActivity.english){
	        	aceptar.setImageResource(R.drawable.accept_button2);
	        	cancelar.setImageResource(R.drawable.cancel_button2);
	        }
	        else{
	        	aceptar.setImageResource(R.drawable.accept_button);
	        	cancelar.setImageResource(R.drawable.cancel_button);
	        }
	        
	        aceptar.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	String methodname = "sendRating";
	                String soap = "http://turismo/" + methodname;
	            	Services.addRating(methodname, soap, String.valueOf(ratingBar.getRating()),placeID );
	            	refreshRat();
	                rankDialog.dismiss();
	            }
	        }); 
	        cancelar.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                rankDialog.dismiss();
	            }
	        });
	        //now that the dialog is set up, it's time to show it    
	        rankDialog.show();   
		}
		else if(v.getId()==R.id.checkinButton && isOnline())
		{
			Intent intent = new Intent(InformationActivity.this, FoursquareActivity.class);
        	this.startActivity(intent);
		}
		else if(v.getId()==R.id.tweetButton && isOnline())
		{
			if(accessToken!=null)
				enviaTweet();
			else
			{
				
			}
		}
		else if(v.getId()==R.id.shareButton && isOnline())
		{
			

			Toast.makeText(this, "Publicando mensaje", Toast.LENGTH_LONG).show();
			conect();
    		String text= this.text.getText().toString();
    		String title = this.title.getText().toString();
    		String response= postOnWall(title,text);
    		
    		if(response.equals("{\"error\":{\"message\":\"(#506) Duplicate status message\",\"type\":\"OAuthException\",\"code\":506}}"))
    			Toast.makeText(this, "Mensaje ya publicado", Toast.LENGTH_LONG).show();
    		else
    			Toast.makeText(this, "Mensaje  publicado", Toast.LENGTH_LONG).show();
		}
		else if(v.getId()==R.id.exitButton){
			this.finish();
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
	    Dialog exitDialog = new Dialog(InformationActivity.this, R.style.FullHeightDialog);
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
	
	public static Bitmap getImageBitmap(String url) { 
        Bitmap bm = null; 
        try { 
            URL aURL = new URL(url); 
            URLConnection conn = aURL.openConnection(); 
            conn.connect(); 
            InputStream is = conn.getInputStream(); 
            BufferedInputStream bis = new BufferedInputStream(is); 
            bm = BitmapFactory.decodeStream(bis); 
            bis.close(); 
            is.close(); 
       } catch (IOException e) { 
           Log.e("1", "Error getting bitmap", e); 
       } 
       return bm; 
    } 
	
	private ArrayList<String> procesarConsulta(String consulta) {
		
		Gson gson = new Gson();
		ArrayList<String> arreglo = new ArrayList<String>();
        Type collectionType = new TypeToken<ArrayList<String> >(){}.getType();
        arreglo = gson.fromJson(consulta, collectionType);
        
        return arreglo;
	}
}
