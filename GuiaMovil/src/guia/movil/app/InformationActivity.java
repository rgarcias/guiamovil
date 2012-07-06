package guia.movil.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.twitter.android.TwitterApp;
import com.twitter.android.TwitterApp.TwDialogListener;

import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.auth.AccessToken;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class InformationActivity extends FBConnectionActivity implements OnClickListener, OnItemClickListener {
	public static boolean ERROR = false;
	public static int RESULT_OK = 0;
	public static int RESULT_WRONG = 1;
	
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
	private TwitterApp mTwitter;
	
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
	private boolean usuarioTwitter;
	private MyAdapter myAdapter;
	private ArrayList<String> photos;
	private String description;
	
	private Dialog exitDialog;

	private ProgressDialog iProgress;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.information);
        
        usuarioTwitter = false;
        mTwitter = new TwitterApp(this, CONSUMER_KEY,CONSUMER_SECRET);	
        mTwitter.setListener(mTwLoginDialogListener);
        if (mTwitter.hasAccessToken()) {
			String username = mTwitter.getUsername();
			username		= (username.equals("")) ? "Unknown" : username;
        }
        rb = (RatingBar) this.findViewById(R.id.ratingBar1);
        rb.setRating(Float.valueOf(0)); 
        title = (TextView) this.findViewById(R.id.textView1);
        title.setText(CategoryActivity.PLACE);
        text = (TextView) this.findViewById(R.id.textView2);
        if(isOnline()){
        	iProgress = new ProgressDialog(this);
			iProgress.setCancelable(false);
			iProgress.setMessage("Cargando información");
			if(PresentationActivity.english){
				iProgress.setMessage("Loading information");
			}
			iProgress.show();
        	
        	new Thread() {
				@Override
	            public void run() {
	                int what = 0;
	                
	                try{   	
	                	String methodname = "getDescription";
		         	    String soap = "http://turismo/" + methodname;
		         	    description = Services.getDescription(methodname, soap, "place", CategoryActivity.PLACE, "english", new Boolean(PresentationActivity.english));
			         	String methodnameID = "getPlaceID";
			       	    String soapID = "http://turismo/" + methodnameID;
			       	    placeID = Services.getPlaceID(methodnameID, soapID, "place", CategoryActivity.PLACE); 
		                photos = procesarConsulta(Services.getPhotos("getPhotos", "http://turismo/getPhotos", "placeID", placeID));
	                }
	                catch(Exception e){
	                	what = 1;
	                }
	                cHandler.sendMessage(cHandler.obtainMessage(what));
	            }
	        }.start();
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
    
    private Handler cHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            iProgress.dismiss();
 
            if (msg.what == 0) {
                text.setText(description);
	        	refreshRat();

		        ImageTask iw = new ImageTask();
                iw.execute(photos.toArray(new String[photos.size()]));
		        
                if(PresentationActivity.english){
                	Toast.makeText(InformationActivity.this, "Successfully loading", Toast.LENGTH_SHORT).show();
                }
                else{
                	Toast.makeText(InformationActivity.this, "Cargado con éxito", Toast.LENGTH_SHORT).show();
                }
            }
            else{
            	setResult(InformationActivity.RESULT_WRONG, new Intent());
            	finish();
            }
        }
    };
    
    private void refreshRat()
    {
    	String methodnameAv = "getPlaceRatingAverage";
   	    String soapAv = "http://turismo/" + methodnameAv;
   	    rat = Services.getPlaceID(methodnameAv, soapAv, "placeId", placeID);
        rb.setRating(Float.valueOf(rat));
    }

	public void onClick(View v) {
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
	            public void onClick(View v) {
	            	if(this.isOnline()){
	            		String methodname = "sendRating";
		                String soap = "http://turismo/" + methodname;
		            	Services.addRating(methodname, soap, String.valueOf(ratingBar.getRating()),placeID );
		            	refreshRat(); 
	            	}
	            	rankDialog.dismiss();
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
	                exitDialog.setCanceledOnTouchOutside(true);
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
	        }); 
	        cancelar.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                rankDialog.dismiss();
	            }
	        });  
	        rankDialog.show();   
		}
		else if(v.getId()==R.id.checkinButton && isOnline())
		{
			Intent intent = new Intent(InformationActivity.this, FoursquareActivity.class);
        	this.startActivity(intent);
		}
		else if(v.getId()==R.id.tweetButton && isOnline())
		{
			if(mTwitter.hasAccessToken()){
				if(PresentationActivity.english){
					postReview("Hi!, I have found "+ CategoryActivity.PLACE +" in Guía Movil Curicó.");
					postToTwitter("Hi!, I have found "+ CategoryActivity.PLACE +" in Guía Movil Curicó.");
				}
				else{
					postReview("Hola!, he encontrado "+ CategoryActivity.PLACE +" en la aplicación Guía Movil Curicó.");
					postToTwitter("Hola!, he encontrado "+ CategoryActivity.PLACE +" en la aplicación Guía Movil Curicó.");
				}
			}
			else{
				onTwitterClick();
			}
		}
		else if(v.getId()==R.id.shareButton && isOnline())
		{
			String conect="Conectando";
			String published="Publicando mensaje";
			String yaPublicado="Mensaje ya publicado";
			String publicado="Mensaje  publicado";
			
			if(PresentationActivity.english)
			{
				conect="Connecting";
				published="Posting a message";
				yaPublicado="Message has been published";
				publicado="Post correctly";
	
			}

			if (!isLogged())
			{
				Toast.makeText(this, conect, Toast.LENGTH_LONG).show();
				conect();
			}
			else{
				conect();
				Toast.makeText(this, published, Toast.LENGTH_LONG).show();
				String text= this.text.getText().toString();
				String title = this.title.getText().toString();
				String response= postOnWall(title,text);
    		
				if(response.equals("{\"error\":{\"message\":\"(#506) Duplicate status message\",\"type\":\"OAuthException\",\"code\":506}}"))
					Toast.makeText(this, yaPublicado, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(this,publicado, Toast.LENGTH_LONG).show();
			}
		}
		else if(v.getId()==R.id.exitButton){
			this.finish();
			exitDialog.dismiss();
			Intent temp = new Intent(Intent.ACTION_MAIN);
			temp.addCategory(Intent.CATEGORY_HOME);
			startActivity(temp);
		}
	}
	
	public void refresh(){
		Intent intent = new Intent(InformationActivity.this, MainActivity.class);
		this.finish();
		startActivity(intent);
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
	    exitDialog = new Dialog(InformationActivity.this, R.style.FullHeightDialog);
        exitDialog.setContentView(R.layout.exitdialog);
        exitDialog.setCancelable(false);
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

	private ArrayList<String> procesarConsulta(String consulta) {
		Gson gson = new Gson();
		ArrayList<String> arreglo = new ArrayList<String>();
        Type collectionType = new TypeToken<ArrayList<String> >(){}.getType();
        arreglo = gson.fromJson(consulta, collectionType);
        
        return arreglo;
	}
	
	
	private void onTwitterClick() {
		if (mTwitter.hasAccessToken()) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setMessage("Delete current Twitter connection?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   mTwitter.resetAccessToken();
			        	   usuarioTwitter = false;
			           }
			       })
			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			                usuarioTwitter = true;
			           }
			       });
			final AlertDialog alert = builder.create();
			
			alert.show();
		} else {	
			mTwitter.authorize();
			usuarioTwitter = true;
		}
	}
	
	private final TwDialogListener mTwLoginDialogListener = new TwDialogListener() {
		public void onComplete(String value) {
			String username = mTwitter.getUsername();
			username = (username.equals("")) ? "No Name" : username;			
			Toast.makeText(InformationActivity.this, "Connected to Twitter as " + username, Toast.LENGTH_LONG).show();
		}
		
		public void onError(String value) {		
			Toast.makeText(InformationActivity.this, "Twitter connection failed", Toast.LENGTH_LONG).show();
		}
	};

	private void postReview(String review) {
		Toast.makeText(this, "Review posted", Toast.LENGTH_SHORT).show();
	}
	
	private void postToTwitter(final String review) {
		new Thread() {
			@Override
			public void run() {
				int what = 0;
				try {
					mTwitter.updateStatus(review);
				} catch (Exception e) {
					what = 1;
				}
				mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String text = (msg.what == 0) ? "Posted to Twitter" : "Post to Twitter failed";
			Toast.makeText(InformationActivity.this, text, Toast.LENGTH_SHORT).show();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.layout.ctxmenu2, menu);
	    if(PresentationActivity.english){
			menu.getItem(0).setTitle("Language");
			menu.getItem(1).setTitle("About");
	    }
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		   switch (item.getItemId()) {	           
		        case R.id.languageMenu2:
	        		if(PresentationActivity.english){
	        			PresentationActivity.english = false;
	        		}
	        		else{
	        			PresentationActivity.english = true;
	        		}
	        		refresh();
	        		CategoryActivity.changed = 1;
			      return true;     
		        case R.id.aboutMenu2:
		        	final Dialog commentView = new Dialog(InformationActivity.this, R.style.FullHeightDialog);
					commentView.setContentView(R.layout.about);
					commentView.setCancelable(true);
				    TextView name = (TextView) commentView.findViewById(R.id.aboutName);
				    TextView text = (TextView) commentView.findViewById(R.id.aboutText);

				    if(PresentationActivity.english)
				    {
				    	name.setText("About");
	        			text.setText(R.string.abouting);
				    }
				    ImageButton back = (ImageButton)commentView.findViewById(R.id.aboutBack);
				    back.setOnClickListener(new View.OnClickListener() {
				    public void onClick(View v) {
				           commentView.dismiss();
				        }
				    }); 
				    commentView.show();
				    return true;
		        default:
		           return super.onOptionsItemSelected(item);
		}
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent image = new Intent(InformationActivity.this, ImageActivity.class);
		ImageActivity.setImages(myAdapter, arg2);
		startActivity(image);
	}

	public class MyAdapter extends BaseAdapter {
		Context context;
		ArrayList<ImageItem> _arrayImageItem;
		  
		public MyAdapter(Context c){
		    context = c;
		    _arrayImageItem = new ArrayList<ImageItem>();
		}
		  
		public void addImageItem(ImageItem item){
			_arrayImageItem.add(item);
		}
		 
		public int getCount() {
			return _arrayImageItem.size();
		}
		 
		public Object getItem(int position) {
			return _arrayImageItem.get(position);
		}
		 
		public long getItemId(int position) {
			return position;
		}
		 
		public View getView(int position, View convertView, ViewGroup parent) {
			 ImageView imageView;
			 imageView = new ImageView(context);
			 
			 imageView.setLayoutParams(new Gallery.LayoutParams(150, 150));
			 imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			 imageView.setImageBitmap(_arrayImageItem.get(position).getImage());
			 
			 return imageView;
		}
	}
	
	public class ImageItem {
		Bitmap bitmapImage;
		  
		public ImageItem(Bitmap bm){
		   bitmapImage = bm;
		}
		   
		public ImageItem(String link) throws IOException{
		   try {
			   bitmapImage = getImageBitmap(link);
		   } 
		   catch (IOException e) {
			   throw e;
		   }
		}
		  
		public Bitmap getImage(){
		   return bitmapImage;
		}
		   
		public Bitmap getImageBitmap(String url) throws IOException { 
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
		    }
		    catch (IOException e) { 
		       throw e;
		    } 
		    bitmapImage = bm;
		    return bm; 
		} 
	}
	
	private class ImageTask extends AsyncTask<String, Void, ArrayList<Bitmap>> {
		ProgressBar progress;
		Gallery g;
		TextView tv;
		
		public ImageTask(){
			progress = (ProgressBar) findViewById(R.id.progressBar);
			tv = (TextView) findViewById(R.id.loadingText);
			if(PresentationActivity.english){
				tv.setText("Loading gallery");
			}
		}
		
		@Override
		protected ArrayList<Bitmap> doInBackground(String... urls) {
			ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
			progress.setVisibility(View.VISIBLE);
			
			for (String url : urls) {
				try{
		            URL aURL = new URL(url); 
		            URLConnection conn = aURL.openConnection(); 
		            conn.connect(); 
		            InputStream is = conn.getInputStream(); 
		            BufferedInputStream bis = new BufferedInputStream(is); 
		            bm.add(BitmapFactory.decodeStream(bis)); 
		            bis.close(); 
		            is.close(); 
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			return bm;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			progress.setVisibility(View.GONE);
			tv.setVisibility(View.GONE);
			g = (Gallery) findViewById(R.id.gallery);
	        myAdapter = new MyAdapter(InformationActivity.this);
	        
	        for(Bitmap e: result){
				myAdapter.addImageItem(new ImageItem(e));
			}
	        g.setOnItemClickListener(InformationActivity.this);
	        g.setAdapter(myAdapter);
	        g.setVisibility(View.VISIBLE);
		}
	}
}