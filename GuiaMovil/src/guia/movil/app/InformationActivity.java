package guia.movil.app;

import java.util.ArrayList;

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
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.information);
        
        RatingBar rb = (RatingBar) this.findViewById(R.id.ratingBar1);
        
        TextView title = (TextView) this.findViewById(R.id.textView1);
        title.setText(CategoryActivity.PLACE);
        TextView text = (TextView) this.findViewById(R.id.textView2);
        String methodname = "getDescription";
        String soap = "http://turismo/" + methodname;
        Log.v("Place", CategoryActivity.PLACE);
        text.setText(Services.getDescription(methodname, soap, "place", CategoryActivity.PLACE));
        rb.setRating(4.2f);
        mContext=this;
        btnShare= (ImageButton) findViewById(R.id.shareButton); 
        btnTwitt= (ImageButton) findViewById(R.id.tweetButton);
        btnCheck= (ImageButton) findViewById(R.id.checkinButton);
        btnComment =(ImageButton) findViewById(R.id.commentsButton);
        btnStar=(ImageButton) findViewById(R.id.starButton);
        
        mFsqApp 		= new com.fsq.android.FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
        
        mAdapter 		= new com.fsq.android.NearbyAdapter(this);
        mNearbyList		= new ArrayList<com.fsq.android.FsqVenue>();
        mProgress		= new ProgressDialog(this);
        
        btnShare.setOnClickListener(this);
            
        
        btnTwitt.setOnClickListener(this);
        
        btnComment.setOnClickListener(this);
        btnStar.setOnClickListener(this);
        
        btnCheck.setOnClickListener(this); 
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
		if(v.getId()== R.id.commentsButton)
		{
			Intent intent = new Intent(InformationActivity.this,CommentsActivity.class);
			this.startActivity(intent);
		}
		else if(v.getId()==R.id.starButton)
		{
			final Dialog rankDialog = new Dialog(InformationActivity.this, R.style.FullHeightDialog);
	        rankDialog.setContentView(R.layout.stardialog);
	        rankDialog.setCancelable(true);
	        RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
	       
	       
	 
	        Button aceptar = (Button) rankDialog.findViewById(R.id.starAcept);
	        aceptar.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                rankDialog.dismiss();
	            }
	        });
	        
	        Button cancelar = (Button) rankDialog.findViewById(R.id.starCancel);
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
			mFsqApp.authorize();
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
			setConnection();
    		getID();
    		String text= "Radal fhghfjfhg tazas";
    		postOnWall(text);
		}
		else if(!isOnline())
		{
			Toast.makeText(this, "No conectado a internet", Toast.LENGTH_SHORT).show();
		}
		
	}
	public boolean isOnline() {
		Context context = getApplicationContext();
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectMgr != null) {
			NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
			if (netInfo != null) {
				for (NetworkInfo net : netInfo) {
					if (net.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} 
		else {
			Log.d("NETWORK", "No network available");
		}
		return false;
	}
}
