package guia.movil.app;

import java.util.ArrayList;




import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class InformationActivity extends FBConnectionActivity {
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
        
        rb.setRating(4.2f);
        mContext=this;
        btnShare= (ImageButton) findViewById(R.id.shareButton); 
        btnTwitt= (ImageButton) findViewById(R.id.tweetButton);
        btnCheck= (ImageButton) findViewById(R.id.checkinButton);
        
        mFsqApp 		= new com.fsq.android.FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
        
        mAdapter 		= new com.fsq.android.NearbyAdapter(this);
        mNearbyList		= new ArrayList<com.fsq.android.FsqVenue>();
        mProgress		= new ProgressDialog(this);
        
        btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            		
            		setConnection();
            		getID();
            		String text= "Radal fhghfjfhg tazas";
            		postOnWall(text);
            	
            	
            	
            }
        });
        
        
        btnTwitt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inicializar();
				if(accessToken!=null)
					enviaTweet();
				else
				{
					autoriza();
					
				
				}
			}
		});
        
        
        btnCheck.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		mFsqApp.authorize();
        	}
        }); 
    }
    
   
   private void inicializar()
   {
         
         accesPref = PreferenceManager.getDefaultSharedPreferences(mContext);
         String accesToken= accesPref.getString(ACCES, "");
         String SecretToken= accesPref.getString(SECRET, "");
         Toast.makeText(this, accesToken+"dsdss", Toast.LENGTH_LONG).show();
         if (!accesToken.equals(""))
        	 accessToken = new AccessToken(accesToken, SecretToken);
    }
    
    
    
    
    private void autoriza() {
		try {
			httpOauthConsumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			httpOauthprovider = new CommonsHttpOAuthProvider(REQUEST_URL, ACCESS_URL, AUTHORIZE_URL);
			String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, CALLBACK_URL);
	
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
		} catch (Exception e) {
			
			Log.v("autoriza",e.getMessage());
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		Toast.makeText(this, "onNewIntent", Toast.LENGTH_LONG).show();
		super.onNewIntent(intent);

		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
			String verifier = uri.getQueryParameter(oauth.signpost.OAuth.OAUTH_VERIFIER);
			try {
				httpOauthprovider.retrieveAccessToken(httpOauthConsumer, verifier);
				accessToken = new AccessToken(httpOauthConsumer.getToken(), httpOauthConsumer.getTokenSecret());
				accesPref = PreferenceManager.getDefaultSharedPreferences(mContext);
				accesPref.edit().putString(ACCES,httpOauthConsumer.getToken()).commit();
				accesPref.edit().putString(SECRET, httpOauthConsumer.getTokenSecret()).commit();
				System.out.println(httpOauthprovider);
				Log.v("tokentwitt",httpOauthConsumer.getToken());
			    
				
				
			} catch (Exception e) {
				Log.v("tokentwitt",e.getMessage());
				
			}
		}
	}
	
	private void enviaTweet() {
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(accessToken);
	
			// send the tweet
			twitter.updateStatus("Probando twitter desde aplicacion android para la muni");
	
			// feedback for the user...
			
		} catch(Exception e) {
			
		}
	}
	
	
	

    
}
